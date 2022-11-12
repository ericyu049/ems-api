const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');
const jwtUtil = require('../util/jwtUtil').jwtUtil;

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
router.patch('/:rid', async (request, response) => {
    const data = await db.collection('review').findOneAndUpdate(
        { rid: request.params.rid },
        { $set: { completed: true } },
        { upsert: true }
    );
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Updated successfully'
    })
})
router.post('/getAssignedReviews', jwtUtil.authenticationMiddleware, async(request, response) => {
    if (response.locals.user.id) {
        const data = await db.collection('review').find({participants: {$regex: response.locals.user.name}, completed: false}).toArray();
        response.status(200).json({
            rspCde: 0,
            rspMsg: 'Success.',
            reviews: data
        });
    }
    else return response.status(403);
})
module.exports = router;