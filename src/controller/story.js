const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');
const jwtUtil = require('../util/jwtUtil').jwtUtil;

// Get all storys with params
router.get('/', async (request, response) => {
    const data = await db.collection('story').find({ ...request.query }).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        stories: data
    });
});
// Get single story
router.get('/:id', async (request, response) => {
    const data = await db.collection('story').find({ id: request.params.id }).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        stories: data
    });
});
//get assigned stories
router.post('/getUserStory', jwtUtil.authenticationMiddleware, async (request, response) => {
    if (response.locals.user.id) {
        const data = await db.collection('story').find({ "employee.id" : response.locals.user.id }).toArray();
        response.status(200).json({
            rspCde: 0,
            rspMsg: 'Success.',
            stories: data
        });
    }
    else return response.status(403);
});
router.put('/', async (request, response) => {
    const result = await db.collection('story').insertOne(request.body)
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'story added successfully.'
    });

});
router.post('/delete', async (request, response) => {
    const storys = request.body;
    storys.forEach(story => {
        db.collection('story').deleteOne({ id: story.id });
    });
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Deleted successfully.'
    });
});
router.patch('/', async (request, response) => {
    const data = await db.collection('story').findOneAndUpdate(
        { sid: request.body.sid },
        { $set: { status: request.body.status } },
        { upsert: true }
    );
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Updated successfully'
    })
})

module.exports = router;