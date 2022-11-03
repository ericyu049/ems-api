const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');

// Get all employees
router.get('/:rid', async (request, response) => {
    const data = await db.collection('feedback').find({rid: request.params.rid}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        feedbacks: data
    });

});
router.put('/', async (request, response) => {
    const result = await db.collection('feedback').insertOne(request.body);
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Feedback added successfully.'
    });

});
module.exports = router;