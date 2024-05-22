const express = require("express");
const router = express.Router();

const app = express();


// 사용자 지문 유형 선택 화면 조회
const selectMytextCategory = (req, res) => {
    const { user_id } = req.params;
    const query = `
      SELECT DISTINCT category
      FROM text
      WHERE user_id = ?;
    `;
    const values = [user_id];

  req.conn.query(query, values, (err, categories) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch user categories from database' });
          return;
      }

      const categoryList = categories.map(category => category.category);
      res.json(categoryList); // 카테고리 리스트를 JSON 형태로 응답
  });
};

// 사용자 지문 선택 화면 조회
const selectMyText = (req, res) => {
  const { user_id, category } = req.params;

  const query = `
      SELECT text_id, title
      FROM text
      WHERE user_id = ? AND category = ?;
  `;
  const values = [user_id, category];

  req.conn.query(query, values, (err, MyTexts) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch user texts' });
          return;
      }

      res.json(MyTexts); // 조회된 지문 리스트를 JSON 형태로 응답
  });
};

// 사용자 지문 조회
const viewMyText = (req, res) => {
  const { user_id, category, text_id } = req.params; // URL 파라미터에서 category, text_id 추출

  const query = `
      SELECT title, contents
      FROM text
      WHERE user_id = ? AND category = ? AND text_id = ?;
  `;
  const values = [user_id, category, text_id];

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


router.get("/:user_id", selectMytextCategory);
router.get("/:user_id/:category", selectMyText);
router.get("/:user_id/:category/:text_id", viewMyText);

module.exports = router;