const jwt = require('jsonwebtoken');
const crypto = require ('crypto');

const ALIVE_TIME = 15 * 60;
const REFRESH_TIME = '7d';
const JWT_SECRETKEY = 'xH3nGhXtTs2b2sydVWJpxJBm';

function generateToken(user) {
    console.log('Generating token. User detail: ', user);
    const name = user.name;
    const role = user.role;
    const id = user.id;
    const type = 'access';

    const tokenPayload = { id, name , role, type };
    const token = jwt.sign(
        tokenPayload,
        JWT_SECRETKEY,
        { expiresIn: ALIVE_TIME }
    );
    return token;
}
function getRefreshToken(user) {
    const name = user.name;
    const role = user.role;
    const id = user.id;
    const password = user.password;
    const type = 'refresh';

    const key = genKey(name, password);

    const tokenPayload = { id, role, name, type, key };

    const refreshToken = jwt.sign(tokenPayload, JWT_SECRETKEY, { expiresIn: REFRESH_TIME });
    return refreshToken;
}
function hashHmacSha256(s) {
    return crypto
        .createHmac('sha256', JWT_SECRETKEY)
        .update(s)
        .digest('hex');
}
function genKey(id, password) {
    const rawKey = id + password;
    const key = hashHmacSha256(rawKey, JWT_SECRETKEY);
    return key;
}
function authenticationMiddleware(request, response, nextHandler) {
    const accessToken = getAccessTokenFromHeader(request, 'ems.auth');
    try {
        const tokenPayload = jwt.verify(accessToken, JWT_SECRETKEY);
        if (tokenPayload.type !== 'access') throw new Error('wrong token type');
        response.locals.user = tokenPayload;
        nextHandler();
    }
    catch (error) {
        response.status(401).send(error.message);
    }
}
function refreshTokenMiddleware(request, response, nextHandler) {
    const refreshToken = getAccessTokenFromHeader(request, 'ems.reauth');
    try {
        const tokenPayload = jwt.verify(refreshToken, JWT_SECRETKEY);
        if (tokenPayload.type !== 'refresh') throw new Error('wrong token type');
        response.locals.user = tokenPayload;
        nextHandler();
    }
    catch (error) {
        response.status(401).send(error.message);
    }
}
function getAccessTokenFromHeader(request, type) {
    const map = new Map();
    request.headers.cookie.split(';').forEach(cookie => {
        const temp = cookie.split('=');
        const key = temp[0];
        const value = temp[1];
        map.set(key, value);
    });
    return map.get(type);
}
exports.jwtUtil = { generateToken, getRefreshToken, authenticationMiddleware, refreshTokenMiddleware };