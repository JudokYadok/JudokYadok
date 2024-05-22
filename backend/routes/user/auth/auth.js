const express = require('express');
const router = express.Router();
const axios = require('axios');
const jwt = require('../../../utils/jwt');

// 카카오 서버에서 사용자 정보 가져오기
const getKaKaoUserdata = async (kakao_token) => {
    try{
        const userdata = await axios({    // kakao 서버에서 사용자 정보 가져와 userinfo에 저장
            method:'get',
            url:'https://kapi.kakao.com/v2/user/me',
            headers:{
                Authorization: `Bearer ${kakao_token}`
            }
        });
        return userdata.data;
    } catch(err) {
        console.error(err);
    }
};

// DB에서 사용자 정보 가져오기
const getUserdata = async (req, res, kakao_id) => {
    const query = 'SELECT * FROM `user` WHERE kakao_id = ?';
    return new Promise((resolve, reject) => {
        req.conn.query(query, kakao_id, (err, results) => {
            if (err) {
                console.error(err);
                reject(err);
            } else {
                resolve(results);
            }
        });
    });
};

// 회원가입 처리
const signUp = async (req, res, values) => {
    const query = 'INSERT INTO `user` (kakao_id, name, email, d_day) VALUES (?, ?, ?, ?)';
    return new Promise((resolve, reject) => {
        req.conn.query(query, values, (err, results) => {
            if (err) {
                console.error(err);
                reject(err);
            } else {
                resolve(results);
            }
        });
    });
};

// token 테이블에 존재하는 refresh를 삭제
const checkRefresh = async (req, res, user_id) => {
    const query = 'DELETE FROM token WHERE user_id = ?';

    return new Promise((resolve, reject) => {
        req.conn.query(query, user_id, (err, results) => {
            if (err) {
                console.error(err);
                reject(err);
            } else {
                resolve(true);
            }
        });
    });
}

// JWT token 생성 및 token 테이블에 삽입
const makeToken = async (req, res, user_id) => {
    const access_token = jwt.sign(user_id);
    const refresh_token = jwt.refresh();
    const query = 'INSERT INTO token (user_id, refresh) VALUES (?, ?)';
    const values = [user_id, refresh_token];

    return new Promise((resolve, reject) => {
        req.conn.query(query, values, (err, results) => {
            if (err) {
                console.error(err);
                reject(err);
            } else {
                resolve({
                    access_token: access_token,
                    refresh_token: refresh_token,
                });
            }
        });
    });

}

// 토큰 생성
const sendData = async (res, userdata, token_data) => {
    // user_id, createdAt, access_token, refresh_token 전달
    res.status(200).send({
        user_id: userdata.user_id,
        createdAt: userdata.createdAt,
        access_token: token_data.access_token,
        refresh_token: token_data.refresh_token
    });
};

// 로그인
const login = async (req, res) => {
    try {
        const kakao_token = req.headers.authorization;
        console.log(kakao_token);
        const kakao_data = await getKaKaoUserdata(kakao_token);
        const kakao_id = kakao_data.id.toString();
        const user_name = kakao_data.properties.nickname;
        const user_email = kakao_data.kakao_account.email;
        const values = [kakao_id, user_name, user_email, new Date(2024, 10, 14)];

        let user_data = await getUserdata(req, res, kakao_id);

        if (user_data.length === 0) {
            // 계정 정보가 없으면 회원가입 처리
            await signUp(req, res, values);
            user_data = await getUserdata(req, res, kakao_id)
        }

        await checkRefresh(req, res, user_data[0].user_id);
        let token_data = await makeToken(req, res, user_data[0].user_id);
        await sendData(res, user_data[0], token_data);
    } catch (err) {
        console.error(err);
        res.status(500).json({
            result_req: err.message
        });
    }
};

// 로그인
router.get('/login', login);

// 자동로그인
router.get('/autologin', async (req, res) => {
    console.log('헤더: ', req.headers);
        
    const access_token = req.headers.access;
    const refresh_token = req.headers.refresh;

    if(access_token === undefined || refresh_token === undefined){
        res.status(401).json({
            result_req: "토큰이 존재하지 않음, 로그인 필요",
        });
        return;
    }

    const query = `SELECT user.user_id AS user_id, user.createdAt AS createdAt
                   FROM user
                   INNER JOIN token ON user.user_id = token.user_id
                   WHERE token.refresh = ?`;
    
    const refresh_decoded = jwt.refreshVerify(refresh_token);
    if(!refresh_decoded.ok){
        res.status(401).json({
            result_req: "refresh token 만료됨, 재로그인 필요",
        });
        return;
    }

    req.conn.query(query, [refresh_token], (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).json({
                result_req: err.message,
            });
            return;
        }

        if(results.length > 0){
            res.status(200).send({
                user_id: results[0].user_id,
                createdAt: results[0].createdAt,
                access_token: jwt.sign(results[0].user_id),
                refresh_token: refresh_token,
            });
            return;
        } else {
            res.status(401).json({
                result_req: "refresh token이 DB에 존재하지 않음, 재로그인 필요",
            });
            return;
        }
    });
});

module.exports = router;