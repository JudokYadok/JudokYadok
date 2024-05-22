const { spawn } = require("child_process");
const express = require('express');
const router = express.Router();

// 퀴즈 생성 (테스트)
const createTestQuiz = (req, res) => {
    // content_match.py 실행
    const query = `
      SELECT contents
      FROM text
      WHERE text_id = 2
  `;
    let text = '';

    // 데이터베이스에서 쿼리 실행
    req.conn.query(query, (err, textfile) => {
        if (err) {
            console.error('Error executing query:', err);
            return;
        }
        // 쿼리 결과에서 텍스트 가져오기
        text = textfile[0].contents;

        const pythonProcess = spawn('python', ['/home/t24123/src/v0.9src/ai/content_match.py', text]);

        let quiz = '';
        let errorOccurred = false; // 에러 발생 여부를 나타내는 변수

        pythonProcess.stdout.on('data', (data) => {
            quiz += data.toString();
        });

        pythonProcess.on('close', (code) => {
            console.log(`child process exited with code ${code}`);
            console.log('Received Data: \n', quiz);
            console.log(errorOccurred);
            // 파이썬 프로세스가 종료된 후에 에러가 발생하지 않았다면 응답을 보냄
            if (!errorOccurred) {
                try {
                    const parsedResponse = JSON.parse(quiz);
                    console.log(parsedResponse);

                    res.status(200).json(parsedResponse);
                } catch (error) {
                    console.error('JSON parsing Error', error);
                }
            }
        });

        pythonProcess.stderr.on('data', (data) => {
            const errorMessage = data.toString();
            if (errorMessage.includes('LangChainDeprecationWarning')) {
                // LangChainDeprecationWarning과 같은 경고 메시지는 무시
                return;
            }

            console.error(`stderr: ${errorMessage}`);
            // 에러가 발생했음을 표시
            errorOccurred = true;
        });
    });
};

// 퀴즈 저장 (테스트)
const saveTestQuiz = (req, res) => {
    const { user_id, text_id } = req.params;
    const { quiz_list, user_answer_list, correct_answer_list } = req.body;

    const connection = req.conn;

    connection.beginTransaction((err) => {
        if (err) {
            console.error('Error starting transaction:', err);
            return res.status(500).json({ message: 'Failed to start transaction' });
        }

        // quiz_list 데이터를 직렬화
        const questions = JSON.stringify(quiz_list.question_list);
        const answers = JSON.stringify(quiz_list.answer_list);
        const user_answers = JSON.stringify(user_answer_list);
        const correct_answers = JSON.stringify(correct_answer_list);

        // 쿼리 실행
        const query = `
            INSERT INTO quiz (user_id, text_id, questions, answers, user_answers, correct_answers)
            VALUES (?, ?, ?, ?, ?, ?)
        `;
        const values = [user_id, text_id, questions, answers, user_answers, correct_answers];

        connection.query(query, values, (err, result) => {
            if (err) {
                console.error('Error executing query:', err);
                connection.rollback(() => {});
                return res.status(500).json({ message: 'Failed to save quiz' });
            }

            connection.commit((err) => {
                if (err) {
                    console.error('Error committing transaction:', err);
                    connection.rollback(() => {});
                    return res.status(500).json({ message: 'Failed to commit transaction' });
                }

                console.log(values, result);
                res.status(201).json({ message: 'Quiz saved successfully', quiz_id: result.insertId });
            });
        });
    });
};

// 퀴즈 조회 (테스트)
const viewSavedQuiz = (req, res) => {
    const { user_id, quiz_id } = req.params;

    const query = `
        SELECT user_id, text_id, questions, answers, user_answers, correct_answers, createdAt
        FROM quiz
        WHERE user_id = ? AND quiz_id = ?
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
            createdAt: quiz.createdAt
        });
    });
};

router.post("/", createTestQuiz);
router.post("/:user_id/:text_id/save", saveTestQuiz);
router.get("/:user_id/:quiz_id", viewSavedQuiz);

module.exports = router;
