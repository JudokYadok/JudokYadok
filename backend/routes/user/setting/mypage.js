const express = require("express");
const router = express.Router();

// 마이 페이지 조회
const viewMyPage = (req, res) => {
    const { user_id } = req.params; // URL 파라미터에서 user_id 추출
    let user_dday = null;
    const query = `
        SELECT *
        FROM user
        WHERE user_id = ?;
    `;
    const values = [user_id];
  
    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to fetch user info' });
            return;
        }
  
        if (result.length === 0) {
            res.status(404).json({ error: 'User not found' });
        } else {
            if(result[0].d_day){
                user_dday = {
                    year: result[0].d_day.getFullYear(),
                    month: result[0].d_day.getMonth() + 1,
                    date: result[0].d_day.getDate()
                }
            }else{
                user_dday = {
                    year: 0,
                    month: 0,
                    date: 0
                }
            }
            const userdata = {
                user_id: result[0].user_id,
                kakao_id: result[0].kakao_id,
                email: result[0].email,
                name: result[0].name,
                createdAt: result[0].createdAt,
                updatedAt: result[0].updatedAt,
                d_day: user_dday,
            }
            res.json(userdata); // 조회된 지문을 JSON 형태로 응답
        }
    });
  };

const updateMyPage = (req, res) => {
    const { name, email, d_day, user_id } = req.body;
  
    const query = `
        UPDATE user
        SET name = ?, email = ?, d_day = ?
        WHERE user_id = ?;
    `;

    const dday = d_day.year === 0 ? null : new Date(d_day.year, d_day.month-1, d_day.date, 0, 0);
    const values = [name, email, dday, user_id];
  
    req.conn.query(query, values, (err, userdata) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to fetch user info' });
            return;
        }
  
        res.status(200).json({ result_req: '회원 정보 수정 성공'})
    });
  };

const deleteUser = (req, res) => {
    const { user_id } = req.params; // URL 파라미터에서 user_id 추출
  
    const query = `
        DELETE
        FROM user
        WHERE user_id = ?;
    `;
    const values = [user_id];
  
    req.conn.query(query, values, (err, userdata) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to fetch user info' });
            return;
        }
  
        res.status(200).json({ result_req: '회원 탈퇴 성공'});
    });
  }

router.get('/:user_id', viewMyPage);

router.put('', updateMyPage);

router.delete('/:user_id', deleteUser);

module.exports = router;