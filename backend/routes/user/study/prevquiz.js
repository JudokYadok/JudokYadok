const { spawn } = require("child_process");
const express = require("express");
const router = express.Router();

// 기출 지문 퀴즈 생성
const createPrevtextQuiz = (req, res) => {
    const { category, text_id } = req.params;
    const { quiz_type } = req.body;

    const query = `
      SELECT contents
      FROM text
      WHERE user_id = 0 AND category = ?
      AND text_id = ?;
  `;
    const values = [category, text_id];
    console.log(values);

    let text = '';

    // 데이터베이스에서 쿼리 실행
    req.conn.query(query, values, (err, textfile) => {
        if (err) {
            console.error('Error executing query:', err);
            return res.status(500).json({ error: 'Internal server error'});
        }

        // 쿼리 결과가 없으면 404 에러를 보냄
        if (textfile.length === 0) {
            return res.status(404).json({ error: 'Text not found'});
        }


        // 쿼리 결과에서 텍스트 가져오기
        console.log(textfile);
        text = textfile[0].contents;
        console.log(text);

        let pythonScript;
        switch (quiz_type) {
            case 'content_match':
                pythonScript = '/home/t24123/src/v0.9src/ai/content_match.py';
                break;
            case 'content_pattern':
                pythonScript = '/home/t24123/src/v0.9src/ai/content_pattern.py';
                break;
            case 'content_understanding':
                pythonScript = '/home/t24123/src/v0.9src/ai/content_understanding.py';
                break;
            case 'target_comparison':
                pythonScript = '/home/t24123/src/v0.9src/ai/target_comparison.py';
                break;
            case 'all_types':
                pythonScript = '/home/t24123/src/v0.9src/ai/all_types.py';
                break;
            default:
                return res.status(400).json({ error: 'Invalid quiz_type' });
        }

        const pythonProcess = spawn('python', [pythonScript, text]);

        let result = '';
        let errorOccurred = false; // 에러 발생 여부를 나타내는 변수

        pythonProcess.stdout.on('data', (data) => {
            result += data.toString();
        });

        pythonProcess.on('close', (code) => {
            console.log(`child process exited with code ${code}`);
            console.log('Received Data: \n', result);
            console.log(errorOccurred);
            // 파이썬 프로세스가 종료된 후에 에러가 발생하지 않았다면 응답을 보냄
            if (!errorOccurred) {
                try {
                    const parsedResponse = JSON.parse(result);
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

// 기출 지문 퀴즈 저장
const savePrevtextQuiz = (req, res) => {
    const { text_id, user_id } = req.params;
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

// 채점 시 요약 제공
const summerizePrevtext = (req, res) => {
    const { category, text_id } = req.params;

    const query = `
      SELECT contents
      FROM text
      WHERE category = ?
      AND text_id = ?;
  `;
    const values = [category, text_id];
    console.log(values);

    let text = '';

    // 데이터베이스에서 쿼리 실행
    req.conn.query(query, values, (err, textfile) => {
        if (err) {
            console.error('Error executing query:', err);
            return res.status(500).json({ error: 'Internal server error'});
        }

        // 쿼리 결과가 없으면 404 에러를 보냄
        if (textfile.length === 0) {
            return res.status(404).json({ error: 'Text not found'});
        }


        // 쿼리 결과에서 텍스트 가져오기
        console.log(textfile);
        text = textfile[0].contents;

        const pythonProcess = spawn('python', ['/home/t24123/src/v0.9src/ai/summarize.py', text]);

        let result = '';
        let errorOccurred = false; // 에러 발생 여부를 나타내는 변수

        pythonProcess.stdout.on('data', (data) => {
            result += data.toString();
        });

        pythonProcess.on('close', (code) => {
            console.log(`child process exited with code ${code}`);
            console.log('Received Data: \n', result);
            console.log(errorOccurred);
            // 파이썬 프로세스가 종료된 후에 에러가 발생하지 않았다면 응답을 보냄
            if (!errorOccurred) {
                try {
                    const parsedResponse = JSON.parse(result);
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
}

router.post("/:category/:text_id/quiz", createPrevtextQuiz);
router.post("/:category/:text_id/quiz/save/:user_id", savePrevtextQuiz);
router.post("/:category/:text_id/quiz/mark", summerizePrevtext);

module.exports = router;
