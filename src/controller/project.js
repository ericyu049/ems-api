const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');

// Get all projects with params
router.get('/', async (request, response) => {
    const data = await db.collection('project').find({ ...request.query }).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        project: data
    });
});
router.patch('/', async (request, response) => {
    const data = await db.collection('project').findOneAndUpdate(
        { pid: '1234567' },
        { $set: { name: request.body.name } },
        { upsert: true }
    );
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Updated successfully'
    })
})

module.exports = router;