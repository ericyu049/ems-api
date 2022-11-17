const express = require('express');
const router = express.Router();
const { MongoClient } = require("mongodb");
const url = 'mongodb://192.168.1.57:27017/';
const client = new MongoClient(url);
const db = client.db('emsv2');
const jwtUtil = require('../util/jwtUtil').jwtUtil;


router.post('/login', async (request, response) => {
    const username = request.body.username;
    const password = request.body.password;
    const user = await db.collection('employee').findOne({name: { '$regex' : username, $options: 'i' } });
    if (!user) {
        response.status(400).send('User not found');
        return;
    }
    const isPasswordCorrect = password === 'password'
    if (!isPasswordCorrect) {
        response.status(400).send('Incorrect password');
        return;
    }
    const token = jwtUtil.generateToken(user);
    const refreshToken = jwtUtil.getRefreshToken(user);
    response.cookie("ems.auth", token, { httpOnly: true, secure: true, expire: new Date() + 900000 });
    response.cookie("ems.reauth", refreshToken, { httpOnly: true, secure: true, expire: new Date() + 259200000  });
    response.status(200).json({ rspCde: 0, rspMsg: 'Success' });
});
router.post('/isLoggedin', jwtUtil.authenticationMiddleware, (request, response) => {
    if (response.locals.user) {
        response.status(200).json({ rspCde: 0, rspMsg: 'Logged in.' });
        return;
    }
    else {
        response.status(401).send('User not authorized.');
        return;
    }
});
router.post('/isAdmin', jwtUtil.authenticationMiddleware, (request, response) => {
    if (response.locals.user.role.includes('Manager')) {
        response.status(200).json({ rspCde: 0, rspMsg: 'User has admin access' });
        return;
    }
    else {
        response.status(401).send('User not authorized.');
        return;
    }
});
router.post('/logout', jwtUtil.logoutMiddleware, async (request, response) => {
    response.status(200).json({rspCde: 0, rspMsg: 'Logged out.'});
});
router.post('/refreshToken', jwtUtil.refreshTokenMiddleware, async (request, response) => {
    const refreshToken = request.body.refreshToken;
    try {
        const tokenPayload = jwt.verify(refreshToken, JWT_SECRET_KEY);
        if (tokenPayload.type !== 'refresh') throw new Error('wrong token type');

        const userId = tokenPayload.userId;
        const user = await userService.getUserByUsername(username);
        const password = user.password;

        const keyToCompare = jwtUtil.genKey(userId, password);
        if (keyToCompare !== tokenPayload.key) {
            throw new Error('password changed');
        }

        const newAccessToken = jwtUtil.genAccessToken(userInDb);
        response.cookie("ems.auth", newAccessToken, { httpOnly: true, secure: true, expire: new Date() + 900000 });
        response.status(200).json({ rspCde: 0, rspMsg: 'Success' });

    } catch (error) {
        response.status(401).send(error.message);
    }
});
module.exports = router;
