const express = require('express');
const swaggerUi = require('swagger-ui-express');
const swaggerJsdoc = require('swagger-jsdoc');
const session = require('express-session');
const ejs = require('ejs');
const mysql = require('mysql');
const child_process = require('child_process');
const dbConfig = require('./config/dbconfig.js');
const path = require('path');

const app = express();
const port = 60023;

// 관리자 라우팅 모듈
const adminMainpageRouter = require('./routes/admin/home');
const adminAuthRouter = require('./routes/admin/auth');
const manageUserRouter = require('./routes/admin/users');
const manageTextRouter = require('./routes/admin/text');
const manageFeedbackRouter = require('./routes/admin/feedback');
const manageAiRouter = require('./routes/admin/ai');

// 사용자 라우팅 모듈
const userAuthRouter = require('./routes/user/auth/auth');
const userTextRouter = require('./routes/user/study/usertext');
const prevTextRouter = require('./routes/user/study/prevtext');
const prevtextQuizRouter = require('./routes/user/study/prevquiz');
const usertextQuizRouter = require('./routes/user/study/userquiz');
const myTextRouter = require('./routes/user/library/mytext');
const memoRouter = require('./routes/user/library/memo');
const quizLibRouter = require('./routes/user/library/quiz');
const myPageRouter = require('./routes/user/setting/mypage.js');
const testQuizRouter = require('./routes/user/study/testquiz');
const quizFeedbackRouter = require('./routes/user/study/quizfeedback');
const feedbackRouter = require('./routes/user/setting/feedback');

// 세션 사용 설정
app.use(session({
    resave: false,
    saveUninitialized: false,
    secret: 'keyboard cat'
    }));

// 데이터 파싱
app.use(express.json());
app.use(express.urlencoded({extended: true}));

// ejs 사용 설정
app.set("view engine", "ejs");
app.set('views', path.join(__dirname, 'views'));

app.use("/images", express.static(__dirname+"/public/images"));
app.use("/font", express.static(__dirname+"/public/font"));

// MySQL 연결 생성
const connection = mysql.createConnection(dbConfig);

// 연결 테스트
connection.connect((err) => {
    if (err) {
        console.error("Error connecting to MySQL:", err);
    } else {
        console.log("Connected to MySQL");
    }
});

app.use((req, res, next) => {
    req.conn = connection;
    next();
});

// 관리자 라우팅 (Web)
app.use('/admin', adminMainpageRouter);
app.use('/admin', adminAuthRouter);
app.use('/admin/users', manageUserRouter);
app.use('/admin/text', manageTextRouter);
app.use('/admin/feedback', manageFeedbackRouter);
app.use('/admin/ai', manageAiRouter);

// 사용자 라우팅 (App)
app.use('/user', userAuthRouter);
app.use('/user/study/prevtext', prevTextRouter);
app.use('/user/study/mytext', userTextRouter);
app.use('/user/library/mytext', myTextRouter);
app.use('/user/library/memo', memoRouter);
app.use('/user/library/quiz', quizLibRouter);
app.use('/user/setting/mypage', myPageRouter);
app.use('/user/test', testQuizRouter);
app.use('/user/study/prevtext', prevtextQuizRouter);
app.use('/user/study/mytext', usertextQuizRouter);
app.use('/user/study/feedback', quizFeedbackRouter);
app.use('/user/setting/feedback', feedbackRouter);

const swaggerOptions = {
    swaggerDefinition: {
        openapi: '3.0.0',
        info: {
            title: '주독야독 API',
            version: '1.0.0',
            description: '주독야독 API 문서',
        },
        servers: [
            {
                url: "http://ceprj.gachon.ac.kr:" + port,
            },
        ],
    },
    apis: [path.join(__dirname, '/routes/admin/*.js'), path.join(__dirname, '/routes/user/*.js')],  // 필요 시 배열 형식으로 파일 경로 추가
}

const swaggerSpec = swaggerJsdoc(swaggerOptions);

// Swagger UI 설정
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

// 서버 종료 시 MySQL 연결 종료
process.on("SIGINT", () => {
    connection.end();
    process.exit();
});

const server = app.listen(port, () => {
    const { address, port } = server.address();
    console.log(`Server is running on http://${address}:${port}`);
    console.log(`Swagger --> http://${address}:${port}/api-docs`);
});