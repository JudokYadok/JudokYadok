const fs = require('fs');
const { spawn } = require('child_process');

const aiTest = () => {
    const pythonProcess = spawn('python', ['/home/t24123/src/v0.9src/ai/content_match.py']);

    let result = "";

    pythonProcess.stdout.on('data', (data) => {
        result += data.toString();
    });

    pythonProcess.stderr.on('data', (data) => {
        console.error(`stderr: ${data}`);
    });

    pythonProcess.on('close', (code) => {
        console.log(`child process 종료 : ${code}`);
        console.log('Received Data: \n', result); // 데이터 출력

        try {
            const parsedResponse = JSON.parse(result);
            console.log(parsedResponse);

            fs.writeFile('result8.json', result, (err) => {
                if(err) {
                    console.error('JSON file writing Error', err);
                } else {
                    console.log('Result Saved');
                }
            });
        } catch (error) {
            console.error('JSON parsing Error', error);
        }
    });
};

aiTest();
