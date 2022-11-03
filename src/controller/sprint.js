const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');

// Get all sprints with params
router.get('/', async (request, response) => {
    const data = await db.collection('sprint').find({...request.query}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        sprints: data
    });
});
// Get single sprint
router.get('/:name', async (request, response) => {
    const data = await db.collection('sprint').find({id: request.params.name}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        sprint: data
    });
});
router.put('/', async (request, response) => {
    const result = await db.collection('sprint').insertOne(request.body)
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'sprint added successfully.'
    });

});
router.post('/delete', async (request, response) => {
    const sprints = request.body;
    sprints.forEach(sprint => {
        db.collection('sprint').deleteOne({id: sprint.id});
    });
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Deleted successfully.'
    });
})

module.exports = router;