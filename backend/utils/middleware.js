const jwt = require('./jwt');

function adminRequire(req, res, next){
    if(req.session.role) {
        next();
    }else{
        res.redirect('/admin/login');
    }
}

function tokenRequire(req, res, next) {
    const access_token = req.headers["authorization"];

    const access_result = jwt.verify(access_token);
    const userId = access_result.id;
    if(access_result.ok){   // 액세스 토큰 유효함
        next(userId);
    } else {
        res.status(401).send({
            result_req: "토큰 만료됨, 재로그인 필요"
        });
    }
}

module.exports = { adminRequire, tokenRequire };