const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');
const jwtUtil = require('../util/jwtUtil').jwtUtil;

// Get all employees
router.get('/:rid', async (request, response) => {
    const data = await db.collection('feedback').find({ rid: request.params.rid }).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        feedbacks: data
    });

});
router.put('/', jwtUtil.authenticationMiddleware, async (request, response) => {
    if (response.locals.user) {
        request.body.from = response.locals.user.id;
        const result = await db.collection('feedback').insertOne(request.body);
        response.status(200).json({
            rspCde: 0,
            rspMsg: 'Feedback added successfully.'
        });
    }
    else response.status(403).send();


});
module.exports = router;