const express = require('express')
const router = express.Router()
const mysql = require('mysql');
const dotenv = require('dotenv');
const https = require('https');
const fs = require('fs');

dotenv.config({ path: './.env' })


//APP
const app = express()

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/', router)

const options = {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.cert')
};

https.createServer(options, app).listen(process.env.SERVER_PORT, () => {
    console.log(`Nourri server running on port ${process.env.SERVER_PORT}`)
});

app.use((req, res, next) => {
    res.status(404).send('Not Found');
});


//DB
const db = mysql.createConnection({
    host: process.env.DATABASE_HOST,
    user: process.env.DATABASE_USER,
    password: process.env.DATABASE_PASSWORD,
    database: process.env.DATABASE_NAME,
});

db.connect((e) => {
    e ? console.log(e) : console.log("MySQL is now connected");
});


//INGREDIENT
const TABLE_INGREDIENT = 'ingredient'

router.post('/add-ingredient', (req, res) => {
    try {
        const data = req.body;
        if (!(data.name && data.ccal)) throw new Error("Data is NULL");
        const q = 'INSERT INTO ?? (name, ccal) VALUES (?, ?)';
        const values = [TABLE_INGREDIENT, data.name, data.ccal];

        db.query(q, values, (e, result) => {
            if (e) throw e;
            res.send(result);
        });
    } catch (e) {
        console.log(e);
        res.status(403).send('Forbidden Request');
    }

});

router.get('/get-ingredient', (req, res) => {
    try {
        const q = 'SELECT * FROM ??';
        const values = [TABLE_INGREDIENT];

        db.query(q, values, (e, result) => {
            if (e) throw e;
            res.send(result);
        });
    } catch (e) {
        console.log(e);
        res.status(403).send('Forbidden Request');
    }
});

router.get('/get-ingredient-info/:name', (req, res) => {
    try {
        const q = 'SELECT * FROM ?? WHERE name = ?';
        const values = [TABLE_INGREDIENT, req.params.name];

        db.query(q, values, (e, result) => {
            if (e) throw e;
            res.send(result[0]);
        });
    } catch (e) {
        console.log(e);
        res.status(403).send('Forbidden Request');
    }
});