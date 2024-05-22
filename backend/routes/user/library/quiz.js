const { text } = require('body-parser');
const express = require('express');
const router = express.Router();

// 퀴즈 목록 조회
const selectSavedQuiz = (req, res) => {
    const { user_id } = req.params;

    const query = `
        SELECT 
            quiz.quiz_id,
            quiz.text_id, 
            quiz.updatedAt, 
            text.title
        FROM 
            quiz
        INNER JOIN 
            text 
        ON 
            quiz.text_id = text.text_id
        WHERE 
            quiz.user_id = ?
    `;
    const values = [user_id];

    req.conn.query(query, values, (err, results) => {
        if (err) {
            console.error('Error executing query:', err);
            return res.status(500).json({ message: 'Failed to retrieve quizzes' });
        }

        if (results.length === 0) {
            return res.status(404).json({ message: 'No quizzes found for this user' });
        }

        console.log(results);
        res.status(200).json(results);
    });
}

// 퀴즈 조회
const viewSavedQuiz = (req, res) => {
    const { user_id, quiz_id } = req.params;

    const query = `
        SELECT 
            quiz.quiz_id,
            quiz.user_id,
            quiz.text_id,
            quiz.questions,
            quiz.answers,
            quiz.user_answers,
            quiz.correct_answers,
            quiz.createdAt,
            text.title,
            text.contents
        FROM 
            quiz
        INNER JOIN 
            text 
        ON 
            quiz.text_id = text.text_id
        WHERE 
            quiz.quiz_id = ?
    `;
    const values = [user_id, quiz_id];

    req.conn.query(query, values, (err, results) => {
        if (err) {
            console.error('Error executing query:', err);
            return res.status(500).json({ message: 'Failed to retrieve quiz' });
        }

        if (results.length === 0) {
            return res.status(404).json({ message: 'Quiz not found' });
        }

        const quiz = results[0];
        console.log(quiz);
        res.status(200).json({
            user_id: quiz.user_id,
            text_id: quiz.text_id,
            questions: JSON.parse(quiz.questions),
            answers: JSON.parse(quiz.answers),
            user_answers: JSON.parse(quiz.user_answers),
            correct_answers: JSON.parse(quiz.correct_answers),
            createdAt: quiz.createdAt,
            text_title: quiz.title,
            text_contents: quiz.contents
        });
    });
};

// 퀴즈 삭제
const deleteSavedQuiz = (req, res) => {
    const { user_id, quiz_id } = req.params;

    const query = `
        DELETE FROM quiz
        WHERE user_id = ? AND quiz_id = ?;
    `;
    const values = [user_id, quiz_id];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to delete quiz' });
            return;
        }

        if (result.affectedRows !== 0) {
            res.status(200).json({ message: 'Quiz deleted successfully' }); 
        } else {
            res.status(404).json({ error: 'Quiz not found' }); 
        }
    });
}

router.get("/:user_id", selectSavedQuiz);
router.get("/:user_id/:quiz_id", viewSavedQuiz);
router.delete("/:user_id/:quiz_id", deleteSavedQuiz);

module.exports = router;