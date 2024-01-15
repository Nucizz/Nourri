const express = require("express");
const router = express.Router();
const mysql = require("mysql");
const dotenv = require("dotenv");
const https = require("https");
const fs = require("fs");
const os = require('os');

dotenv.config({ path: "./.env" });

//APP
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use("/", router);

app.use((req, res, next) => {
  console.log(`Received ${req.method} request for ${req.url}`);
  next();
});

app.use((err, req, res, next) => {
  console.error('Global Error Handler:', err);
  res.status(500).send('Internal Server Error');
});


app.listen(process.env.SERVER_PORT, () => {
  console.log(`Nourri server running on http://${getLocalIPv4Address()}:${process.env.SERVER_PORT}`);
});

app.use((req, res, next) => {
  res.status(404).send("Not Found");
});

//DB
const db = mysql.createConnection({
  host: process.env.DATABASE_HOST,
  user: process.env.DATABASE_USER,
  password: process.env.DATABASE_PASSWORD,
  database: process.env.DATABASE_NAME,
  port: process.env.DATABASE_PORT,
});

db.connect((e) => {
  e
    ? console.log(e)
    : console.log(
      `MySQL is now connected on port ${process.env.DATABASE_PORT}`
    );
});

// Router Functions and Queries
const TABLE_INGREDIENT = "ingredient";
const TABLE_RECIPE = "recipe";

router.post("/add-ingredient", (req, res) => {
  try {
    const data = req.body;
    if (!(data.name && data.ccal)) throw new Error("Data is NULL");
    const q = "INSERT INTO ?? (name, ccal) VALUES (?, ?)";
    const values = [TABLE_INGREDIENT, data.name, data.ccal];

    db.query(q, values, (e, result) => {
      if (e) throw e;
      res.status(200).send(result);
    });
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.get("/get-ingredient", (req, res) => {
  try {
    const q = "SELECT * FROM ??";
    const values = [TABLE_INGREDIENT];

    db.query(q, values, (e, result) => {
      if (e) throw e;
      res.send(result);
    });
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.get("/get-ingredient-info/:name", (req, res) => {
  try {
    const q = "SELECT * FROM ?? WHERE name = ?";
    const values = [TABLE_INGREDIENT, req.params.name];

    db.query(q, values, (e, result) => {
      if (e) throw e;
      res.status(200).send(result[0]);
    });
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.post("/get-recipe", async (req, res) => {
  try {
    const data = req.body;
    if (!data) throw new Error("Data is NULL");

    const ingredientsList = data
      .map((ingredient) => `- ${ingredient.name} with ${ingredient.ccal} calories/gram`)
      .join("\n");

    const message_template =
      "Generate a healthy high nutritious recipe from these available ingredients (don't add other ingredients outside the specified one excepts for seasonings), alongside the nutrition calculation from the data given below:\n" +
      ingredientsList + "\n" +
      "Place the information into 4 big sections like below:\n" +
      "- Ingredients, contains ingredients and nutrition calculation with layout (ingredient - nutrition) write list in points.\n" +
      "- Instructions, contains instructions how to cook given ingredients write steps in points.\n" +
      "- Summary, contains healthiness rating alongside the nutrition information write in paragraph.\n" +
      "- Title, contains food/recipe title.";

    const responseContent = await getChatGPTResponse(message_template);
    res.status(200).json(responseContent);
    addRecipeHistory(responseContent);
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.get("/get-all-recipe", async (req, res) => {
  try {
    const q = "SELECT * FROM ?? ORDER BY id DESC";
    const values = [TABLE_RECIPE];

    db.query(q, values, (e, result) => {
      if (e) throw e;
      res.status(200).send(result);
    });
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

// Non-Router Functions
async function addRecipeHistory(recipeContent) {
  try {
    if (!recipeContent) throw new Error("Data is NULL");
    const q = "INSERT INTO ?? (title, ingredients, instructions, summary, raw) VALUES (?, ?, ?, ?, ?)";
    const values = [TABLE_RECIPE, recipeContent.title, recipeContent.ingredients, recipeContent.instruction, recipeContent.summary, recipeContent.raw];

    db.query(q, values, (e, result) => {
      if (e) throw e;
      console.log(`${recipeContent.title} is saved. [${result}]`)
    });

    res.status(200)
  } catch (e) {
    console.log(e);
  }
}

async function getChatGPTResponse(message) {
  return new Promise((resolve, reject) => {
    const data = JSON.stringify({
      model: "gpt-3.5-turbo",
      messages: [
        {
          role: "system",
          content: "You are a helpful assistant.",
        },
        {
          role: "user",
          content: message,
        },
      ],
    });

    const options = {
      hostname: process.env.OPENAI_ENDPOINT,
      path: "/v1/chat/completions",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${process.env.OPENAI_KEY}`,
      },
    };

    const req = https.request(options, (res) => {
      let responseData = "";

      res.on("data", (chunk) => {
        responseData += chunk;
      });

      res.on("end", () => {
        try {
          const response = JSON.parse(responseData);

          // Extracting sections from the response
          const raw = response.choices[0].message.content;
          const title = extractSection(raw, "Title");
          const ingredients = extractSection(raw, "Ingredients");
          const instruction = extractSection(raw, "Instructions");
          const summary = extractSection(raw, "Summary");

          resolve({
            title,
            ingredients,
            instruction,
            summary,
            raw
          });

          res.status(200)
        } catch (error) {
          console.error("Error parsing ChatGPT API response:", error.message);
          reject(new Error("Error parsing ChatGPT API response"));
        }
      });
    });

    req.on("error", (error) => {
      reject(new Error(`Error calling ChatGPT API: ${error.message}`));
    });

    req.write(data);
    req.end();
  });
}

function extractSection(content, sectionTitle) {
  const sectionStart = content.indexOf(`${sectionTitle}:`);

  if (sectionStart !== -1) {
    const nextSectionTitles = ["Title:", "Ingredients:", "Summary:", "Instructions:"];
    let sectionEnd = content.length;

    for (const nextTitle of nextSectionTitles) {
      const nextTitleIndex = content.indexOf(nextTitle, sectionStart + sectionTitle.length);
      if (nextTitleIndex !== -1 && nextTitleIndex < sectionEnd) {
        sectionEnd = nextTitleIndex;
      }
    }

    const sectionContent = content.substring(sectionStart + sectionTitle.length + 1, sectionEnd).trim();
    return sectionContent;
  } else {
    return `Unable to extract ${sectionTitle}`;
  }
}

function getLocalIPv4Address() {
  const networkInterfaces = os.networkInterfaces();

  for (const interfaceName of Object.keys(networkInterfaces)) {
    const networkInterface = networkInterfaces[interfaceName];

    for (const { address, family } of networkInterface) {
      if (family === 'IPv4' && !address.startsWith('127.')) {
        return address;
      }
    }
  }

  return 'Unable to determine local IPv4 address';
}
