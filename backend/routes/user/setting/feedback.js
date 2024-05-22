const express = require("express");
const router = express.Router();

// 앱 피드백 추가
const addFeedback = (req, res) => {
    const { user_id } = req.params;
    const { contents } = req.body; // 요청에서 JSON 데이터 추출

     const query = `
        INSERT INTO feedback (user_id, contents) 
        VALUES (?, ?);
    `;
     const values = [user_id, contents];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to add feedback to the library' });
            return;
        }

        // 새로 추가된 피드백의 ID를 포함하여 응답
        res.json({ feedback_id: result.insertId, user_id });
    });
};

router.post("/:user_id", addFeedback);

module.exports = router;