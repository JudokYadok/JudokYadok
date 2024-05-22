const express = require('express');
const router = express.Router();
const { adminRequire } = require('../../utils/middleware');

/**
 * @swagger
 * tags:
 *   name: Admin-user
 *   description: 회원 관리
 */

/* 회원 관리 페이지 조회 */
/**
 * @swagger
 * paths:
 *   /admin/users:
 *     get:
 *       summary: "회원 관리 페이지 조회"
 *       description: "회원 관리 페이지 렌더링"
 *       tags: [Admin-user]
 *       responses:
 *         "200":
 *           description: "회원 관리 페이지 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *                   user_list:
 *                     type: object
 *                     description: "유저 목록"
 *                     example:
 *                       [
 *                         { "user_id": 1000, "name": "회원1"}
 *                       ]
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
router.get("", adminRequire, (req, res)=>{
    const query = 'SELECT user_id, name FROM user';

    req.conn.query(query, (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).json({
                result_req: err.message
            });
            return;
        }

        res.status(200).render('user_list', {    //
            result_req: "회원 관리 페이지 조회 성공",
            user_list: results
        });
    });
});

/* 특정 회원 정보 조회 */
/**
 * @swagger
 * paths:
 *   /admin/users/{user_id}:
 *     get:
 *       summary: "특정 회원 조회"
 *       description: "회원 정보 조회"
 *       tags: [Admin-user]
 *       parameters:
 *         - in: path
 *           name: user_id
 *           required: true
 *           description: "조회할 회원 번호"
 *           schema:
 *             type: number
 *       responses:
 *         "200":
 *           description: "회원 정보 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *                   user_list:
 *                     type: object
 *                     description: "유저 목록"
 *                     example:
 *                       [
 *                         { "user_id": 1000, "name": "회원1"}
 *                       ]
 *                   user_id:
 *                     type: number
 *                     description: 회원 번호
 *                   user_email:
 *                     type: string
 *                     description: 회원 이메일
 *                   user_name:
 *                     type: string
 *                     description: 회원 이름
 *                   user_createdAt:
 *                     type: string
 *                     description: 가입 날짜
 *                   user_d_day:
 *                     type: string
 *                     description: 사용자 설정 D-day
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
router.get("/:user_id", adminRequire, (req, res)=>{
    const user_id = req.params.user_id;
    const query = 'SELECT * FROM user WHERE user_id = ?';
    const query2 = 'SELECT user_id, name FROM user';

    req.conn.query(query, user_id, (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).json({
                result_req: err.message
            });
            return;
        }

        if(results.length > 0){
            const user_data = results[0];

            req.conn.query(query2, (err, results2) => {
                if (err) {
                    console.error(err);
                    res.status(500).json({
                        result_req: err.message
                    });
                    return;
                }

                res.status(200).render('user', {
                    result_req: "회원 정보 조회 성공",
                    user_list: results2,
                    user_id: user_data.user_id,
                    user_email: user_data.email,
                    user_name: user_data.name,
                    user_createdAt: user_data.createdAt ? 
                                    user_data.createdAt.getFullYear() + '-' +
                                    (user_data.createdAt.getMonth()+1) + '-' +
                                    user_data.createdAt.getDate() :
                                    null,
                    user_d_day: user_data.d_day ? 
                                `${user_data.d_day.getFullYear()}-${user_data.d_day.getMonth()}-${user_data.d_day.getDate()}` :
                                null,
                });
            })
        } else {
            res.status(500).json({
                result_req: "회원 정보가 존재하지 않습니다."
            });
        }

    });
});

/* 회원 정보 삭제 */
/**
 * @swagger
 * paths:
 *   /admin/users/{user_id}:
 *     delete:
 *       summary: "회원 정보 삭제"
 *       description: "회원 정보 삭제 처리"
 *       tags: [Admin-user]
 *       parameters:
 *         - in: path
 *           name: user_id
 *           required: true
 *           description: "삭제할 회원 번호"
 *           schema:
 *             type: number
 *       responses:
 *         "200":
 *           description: "회원 정보 삭제 성공"
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
router.delete("/:user_id", adminRequire, (req, res)=>{
    const user_id = req.params.user_id;
    const query = 'DELETE FROM user WHERE user_id = ?';

    req.conn.query(query, user_id, (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).json({
                result_req: err.message
            });
            return;
        }
        
        res.status(200).json({
            result_req: '회원 삭제 성공'
        });
    });
})

module.exports = router;