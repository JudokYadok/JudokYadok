const jwt = require('jsonwebtoken');
const secret = require('../config/jwtconfig').JWT_SECRET_KEY;
const refresh_secret = require('../config/jwtconfig').REFRESH_SECRET_KEY;

module.exports = {

    sign: (user_id) => { // access token 발급
        const payload = { // access token에 들어갈 payload
        id: user_id,
        };

        return jwt.sign(payload, secret, { // secret으로 sign하여 발급하고 return
            algorithm: 'HS256', // 암호화 알고리즘
            expiresIn: '6h', 	  // 유효기간
        });
    },

    verify: (token) => { // access token 검증
        let decoded = null;
        try {
            decoded = jwt.verify(token, secret);
            return {
                ok: true,
                id: decoded.id,
            };
        } catch (err) {
            return {
                ok: false,
                message: err.message,
            };
        }
    },

    refresh: () => { // refresh token 발급
        return jwt.sign({}, refresh_secret, {
            algorithm: 'HS256',
            expiresIn: '14d',
        });
    },

    refreshVerify: (token) => {
        let decoded = null;
        try {
            decoded = jwt.verify(token, refresh_secret);
            return {
                ok: true,
            };
        } catch (err) {
            return {
                ok: false,
                message: err.message,
            };
        }

    },
};