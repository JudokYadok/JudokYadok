const express = require('express');
const router = express.Router();
const { adminRequire } = require('../../utils/middleware');


/**
 * @swagger
 * tags:
 *   name: Admin-ai
 *   description: AI 관리
 */

/* ai 관리 페이지 조회 */
/**
 * @swagger
 * paths:
 *   /admin/ai:
 *     get:
 *       summary: "ai 관리 페이지 조회"
 *       description: "ai 관리 페이지 렌더링"
 *       tags: [Admin-ai]
 *       responses:
 *         "200":
 *           description: "ai 관리 페이지 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 */
router.get("", adminRequire, (req, res)=>{
    res.status(200).render('ai_list', {
        result_req: "ai 관리 페이지 조회 성공"
    });
});

/* 토큰 관리 페이지 조회 */
/**
 * @swagger
 * paths:
 *   /admin/ai/token:
 *     get:
 *       summary: "토큰 관리 페이지 조회"
 *       description: "토큰 관리 페이지 렌더링"
 *       tags: [Admin-ai]
 *       responses:
 *         "200":
 *           description: "토큰 관리 페이지 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *                   prompt_tokens:
 *                     type: number
 *                     description: "프롬프트가 사용한 토큰 수"
 *                   completion_tokens:
 *                     type: number
 *                     description: "완료까지 사용한 토큰 수"
 *                   total_cost:
 *                     type: string
 *                     description: "사용 요금"
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
router.get("/token", adminRequire, (req, res)=>{
    // ai에 요청 보내는 부분 추가
    res.status(200).render('ai_token', {
        result_req: "토큰 관리 페이지 조회 성공",
        prompt_tokens: 2348, // 임시 값
        completion_tokens: 1213, // 임시 값
        total_cost: "$0.14322" // 임시 값
    });
});

/* 모델 정보 페이지 조회 */
/**
 * @swagger
 * paths:
 *   /admin/ai/answer:
 *     get:
 *       summary: "실시간 답변 조회"
 *       description: "실시간 답변 조회 페이지 렌더링"
 *       tags: [Admin-ai]
 *       responses:
 *         "200":
 *           description: "실시간 답변 조회 성공"
 *           content:
 *             application/json:
 *               schema:
 *                 type: object
 *                 properties:
 *                   result_req:
 *                     type: string
 *                     description: "결과 메시지"
 *                   answer:
 *                     type: string
 *                     description: "ai 실시간 답변"
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
router.get("/answer", adminRequire, (req, res)=>{
    res.status(200).render('ai_answer', {
        result_req: "모델 정보 조회 성공",
        answer: `모델명: gpt-4-1106-preview <br><br>
        type: constructor <br><br>
        id: [ "langchain", "prompt", "chat", "ChatPromptValue" ] <br>
        `

    });
});

module.exports = router;