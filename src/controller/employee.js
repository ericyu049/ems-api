const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');
const jwtUtil = require('../util/jwtUtil').jwtUtil;

// Get all employees
router.get('/', async (request, response) => {
    const data = await db.collection('employee').find({}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        employees: data
    });
});
// Get single employee
router.get('/:id', async (request, response) => {
    const data = await db.collection('employee').find({id: request.params.id}).toArray();
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Success.',
        employee: data
    });
});
router.put('/', async (request, response) => {
    const result = await db.collection('employee').insertOne(request.body)
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Employee added successfully.'
    });

});
router.post('/delete', async (request, response) => {
    const employees = request.body;
    employees.forEach(employee => {
        db.collection('employee').deleteOne({id: employee.id});
    });
    response.status(200).json({
        rspCde: 0,
        rspMsg: 'Deleted successfully.'
    });
})
router.post('/myInfo', jwtUtil.authenticationMiddleware, async (request, response) => {
    if (response.locals.user.id) {
        const data = await db.collection('employee').find({id: response.locals.user.id}).toArray();
        response.status(200).json({
            rspCde: 0,
            rspMsg: 'Success.',
            info: data[0]
        });
    }
    else return response.status(403);
})
module.exports = router;