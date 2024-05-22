const express = require('express');
const router = express.Router();

/**
 * @swagger
 * tags:
 *   name: Admin-home
 *   description: 관리자 메인 화면 조회
 */

/* 관리자 메인 화면 조회 */
/**
 * @swagger
 * paths:
 *   /admin/:
 *     get:
 *       summary: "관리자 메인 화면 조회"
 *       description: "관리자 메인 화면 조회 요청으로 메인 페이지를 렌더링"
 *       tags: [Admin-home]
 *       responses:
 *         "200":
 *           description: "관리자 메인 화면 조회 성공"
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

router.get("/", (req, res)=>{
    if(req.session.role){
        res.status(200).render('main', {    //페이지명 입력
            result_req: "관리자 메인 페이지 조회 성공"
        });
    } else {
        res.status(200).render('home', {    //페이지명 입력
            result_req: "관리자 메인 페이지 조회 성공"
        });
    }
});

module.exports = router;