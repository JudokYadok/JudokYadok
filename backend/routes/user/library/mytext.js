const express = require("express");
const router = express.Router();

// 추가한 사용자 지문 목록 조회
const viewMytextList = (req, res) => {
  const { user_id } = req.params; // 사용자 ID

  const query = `
      SELECT category, title, text_id
      FROM text
      WHERE user_id = ?;
  `;
  const values = [user_id];

  req.conn.query(query, values, (err, MyTexts) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch user texts' });
          return;
      }

      console.log(query);
      res.json(MyTexts); // 조회된 지문 리스트를 JSON 형태로 응답
  });
};

// 추가한 사용자 지문 조회
const viewMytext = (req, res) => {
  const { user_id, text_id } = req.params; // URL 파라미터에서 text_id 추출

  const query = `
      SELECT title, contents
      FROM text
      WHERE user_id = ? AND text_id = ?;
  `;
  const values = [user_id, text_id];

  req.conn.query(query, values, (err, MyText) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch user texts' });
          return;
      }

      if (MyText.length === 0) {
          res.status(404).json({ error: 'Usertext not found' }); // 해당 text_id에 해당하는 Mytext가 없는 경우
      } else {
          res.json(MyText[0]); // 조회된 지문을 JSON 형태로 응답
      }
  });
};

// 사용자 지문 추가
const addMytext = (req, res) => {
    const { user_id } = req.params;
    const { category, title, contents } = req.body; // 요청에서 JSON 데이터 추출

     const query = `
        INSERT INTO text (user_id, category, title, contents) 
        VALUES (?, ?, ?, ?);
    `;
     const values = [user_id, category, title, contents];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to add text to the library' });
            return;
        }

        // 새로 추가된 텍스트의 ID를 포함하여 응답
        res.json({ text_id: result.insertId, category, title, contents });
    });
};

// 사용자 지문 수정
const modifyMytext = (req, res) => {
    const { user_id, text_id } = req.params; // URL 파라미터에서 text_id 추출
    const { category, title, contents } = req.body; // 요청에서 JSON 데이터 추출

    const query = `
        UPDATE text 
        SET category = ?, title = ?, contents = ? 
        WHERE user_id = ? AND text_id = ?;
    `;
    const values = [category, title, contents, user_id, text_id];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to modify text' });
            return;
        }

        if (result.affectedRows === 0) {
            res.status(404).json({ error: 'Text not found' });
        } else {
            res.json({ message: 'Text modified successfully' });
        }
    });
};

// 사용자 지문 삭제
const deleteMyText = (req, res) => {
    const { user_id, text_id } = req.params;

    const query = `
        DELETE FROM text
        WHERE user_id = ? AND text_id = ?;
    `;
    const values = [user_id, text_id];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to delete text' });
            return;
        }

        if (result.affectedRows !== 0) {
            res.status(200).json({ message: 'Text deleted successfully' }); 
        } else {
            res.status(404).json({ error: 'Text not found' }); 
        }
    });
}

router.get("/:user_id", viewMytextList);
router.get("/:user_id/:text_id", viewMytext);
router.post("/:user_id", addMytext);
router.put("/:user_id/:text_id", modifyMytext);
router.delete("/:user_id/:text_id", deleteMyText);

module.exports = router;