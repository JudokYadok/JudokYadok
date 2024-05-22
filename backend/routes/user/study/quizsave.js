// quizsave.js
const express = require('express');
const pool = require('./db');

const router = express.Router();

router.post('/user/:user_id/:text_id/quiz/save', async (req, res) => {
    const { user_id, text_id } = req.params;
    const { quiz_list, user_answer_list, correct_answer_list } = req.body;

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // quiz_list 데이터를 직렬화
        const questions = JSON.stringify(quiz_list.question_list);
        const answers = JSON.stringify(quiz_list.answer_list);
        const user_answers = JSON.stringify(user_answer_list);
        const correct_answers = JSON.stringify(correct_answer_list);

        // 쿼리 실행
        const [result] = await connection.execute(
            `INSERT INTO quiz (user_id, text_id, questions, answers, user_answers, correct_answers)
            VALUES (?, ?, ?, ?, ?, ?)`,
            [user_id, text_id, questions, answers, user_answers, correct_answers]
        );

        await connection.commit();

        res.status(201).json({ message: 'Quiz saved successfully', quiz_id: result.insertId });
    } catch (error) {
        await connection.rollback();
        console.error(error);
        res.status(500).json({ message: 'Failed to save quiz' });
    } finally {
        connection.release();
    }
});

module.exports = router;
