const express = require('express');
const router = express.Router();
// const crypto = require('crypto');

/**
 * @swagger
 * tags:
 *   name: Admin-auth
 *   description: 관리자 인증
 */

/* 로그인 페이지 조회 */
/**
 * @swagger
 * paths:
 *   /admin/login:
 *     get:
 *       summary: "관리자 로그인 페이지 조회"
 *       description: "관리자 로그인 화면 조회 요청으로 로그인 페이지를 렌더링"
 *       tags: [Admin-auth]
 *       responses:
 *         "200":
 *           description: "관리자 로그인 페이지 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *         "500":
 *           description: "오류 발생"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "오류 메시지"
 */
router.get("/login", (req, res, next)=>{
    try{
        res.status(200).render('login', {
            result_req: "로그인 페이지 조회 성공"
        });
    } catch(err) {
        res.status(500).json({
            result_req: err.message
        })
    }
});

/* 로그인 */
/**
 * @swagger
 * paths:
 *   /admin/login:
 *     post:
 *       summary: "관리자 로그인"
 *       description: "관리자 로그인 처리"
 *       tags: [Admin-auth]
 *       requestBody:
 *         required: true
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 admin_id:
 *                   type: string
 *                   description: "관리자 아이디"
 *                 admin_pw:
 *                   type: string
 *                   description: "관리자 비밀번호"
 *       responses:
 *         "200":
 *           description: "관리자 로그인 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *         "500":
 *           description: "서버 오류 발생 또는 아이디/비밀번호 오류"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "오류 메시지"
 */
router.post("/login", (req, res)=>{
    const admin_id = req.body.username;
    const admin_pw = req.body.password;
    const query = "SELECT * FROM admin where name = ?";

    req.conn.query(query, admin_id, (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).json({
                result_req: err.message
            });
            return;
        }

        if(results.length === 0){
            res.status(500).json({
                result_req: '존재하지 않는 아이디입니다.'
            });
        } else if (results[0].pw !== admin_pw) {
            res.status(500).json({
                result_req: '비밀번호가 일치하지 않습니다.'
            });
        } else {
            req.session.user_id = results[0].id;
            req.session.role = 'admin';
            res.redirect('/admin/');
        }
        
    });
});

/* 로그아웃 */
/**
 * @swagger
 * paths:
 *   /admin/logout:
 *     get:
 *       summary: "관리자 로그아웃"
 *       description: "관리자 로그아웃 처리"
 *       tags: [Admin-auth]
 *       responses:
 *         "200":
 *           description: "로그아웃 성공"
 *         "500":
 *           description: "오류 발생"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "오류 메시지"
 */
router.get("/logout", (req, res)=>{
    try{
        req.session.destroy();
        res.redirect('/admin/');
    } catch(err) {
        res.status(500).json({
            result_req: err.message
        })
    }
});

module.exports = router;