const { initializeApp } = require("firebase/app");
const { getFirestore, collection, getDocs, addDoc, query, where } = require("firebase/firestore");
const express = require("express");
const router = express.Router();
const dotenv = require("dotenv");
const https = require("https");
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
  res.status(404).send("<div style='width: 100vw; height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center;'><h1>NOT FOUND!</h1> <h3>Nourri backend server by Nucizz</h3></div>");
});

// Firebase
const firebaseConfig = {
  apiKey: process.env.FIREBASE_API_KEY,
  authDomain: process.env.FIREBASE_AUTH_DOMAIN,
  projectId: process.env.FIREBASE_PROJECT_ID,
  storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.FIREBASE_APP_ID
};
const fb = initializeApp(firebaseConfig);
const db = getFirestore(fb);

// Router Functions and Queries
const ingredientDB = collection(db, "ingredient");
const recipeDB = collection(db, "recipe");

// router.post("/add-ingredient", async (req, res) => {
//   try {
//     const data = req.body; 
//     const result = await addDoc(ingredientDB, {
//       name: data.name,
//       ccal: data.ccal
//     })
//     res.status(200).send(result);
//   } catch (e) {
//     console.log(e);
//     res.status(403).send("Forbidden Request");
//   }
// });

router.get("/get-ingredient", async (req, res) => {
  try {
    const querySnapshot = await getDocs(ingredientDB)
    const result = querySnapshot.docs.map(doc => ({
      id: doc.ref,
      ...doc.data()
    }));
    res.status(200).send(result);
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.get("/get-ingredient-info/:name", async (req, res) => {
  try {
    const q = query(ingredientDB, where("name", "==", req.params.name));
    const querySnapshot = await getDocs(q);
    if (querySnapshot.empty) {
      res.status(404).send("Ingredient not found");
    } else {
      const doc = querySnapshot[0];
      const result = {
        id: doc.ref,
        ...doc.data()
      };
      res.status(200).send(result);
    }
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
    await addRecipeHistory(responseContent);
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

router.get("/get-all-recipe", async (req, res) => {
  try {
    const querySnapshot = await getDocs(recipeDB);
    const results = querySnapshot.docs.map(doc => ({
      id: doc.ref,
      ...doc.data()
    }));
    res.status(200).send(results);
  } catch (e) {
    console.log(e);
    res.status(403).send("Forbidden Request");
  }
});

// Non-Router Functions
async function addRecipeHistory(recipeContent) {
  try {
    const result = await addDoc(ingredientDB, {
      title: recipeContent.title,
      ingredients: recipeContent.ingredients,
      instructions: recipeContent.instructions,
      summary: recipeContent.summary,
      raw: recipeContent.raw
    })
    console.log("Added new recipe: " + result)
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
          console.log("ChatGPT API response:", response);

          if (response.choices && response.choices.length > 0) {
            const raw = response.choices[0].message.content;
            const title = extractSection(raw, "Title");
            const ingredients = extractSection(raw, "Ingredients");
            const instructions = extractSection(raw, "Instructions");
            const instruction = instructions; // For Android needs
            const summary = extractSection(raw, "Summary");

            resolve({
              title,
              ingredients,
              instructions,
              instruction,
              summary,
              raw
            });
          } else {
            console.error("Unexpected ChatGPT API response format:", response);
            reject(new Error("Unexpected ChatGPT API response format"));
          }
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
