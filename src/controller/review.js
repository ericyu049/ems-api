const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');

// Get all employees
router.get('/:id', async (request, response) => {
    const data = await db.collection('review').find({targetId: request.params.id}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        reviews: data
    });

});
router.put('/:id', async (request, response) => {
    const result = await db.collection('review').insertOne(request.body);
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Review added successfully.'
    });

});
module.exports = router;