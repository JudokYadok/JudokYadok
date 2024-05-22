const express = require("express");
const router = express.Router();

const app = express();

// 기출 지문 유형 선택 화면 조회
const selectPrevtextCategory = (req, res) => {
  const query = `
      SELECT DISTINCT category
      FROM text
      WHERE user_id = 0;
  `;

  req.conn.query(query, (err, categories) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch previous categories from database' });
          return;
      }

      const categoryList = categories.map(category => category.category);
      res.json(categoryList); // 카테고리 리스트를 JSON 형태로 응답
  });
};

// 기출 지문 선택 화면 조회
const selectPrevText = (req, res) => {
  const category = req.params.category; // URL 파라미터에서 category 추출

  const query = `
      SELECT text_id, title
      FROM text
      WHERE user_id = 0
      AND category = ?;
  `;
  const values = [category];

  req.conn.query(query, values, (err, prevTexts) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch previous texts' });
          return;
      }

      res.json(prevTexts); // 조회된 지문 리스트를 JSON 형태로 응답
  });
};

// 기출 지문 조회
const viewPrevText = (req, res) => {
  const { category, text_id } = req.params; // URL 파라미터에서 category, text_id 추출

  const query = `
      SELECT title, contents
      FROM text
      WHERE user_id = 0
      AND category = ?
      AND text_id = ?;
  `;
  const values = [category, text_id];

  req.conn.query(query, values, (err, prevText) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch previous texts' });
          return;
      }

      if (prevText.length === 0) {
          res.status(404).json({ error: 'Prevtext not found' }); // 해당 text_id에 해당하는 Prevtext가 없는 경우
      } else {
          res.json(prevText[0]); // 조회된 지문을 JSON 형태로 응답
      }
  });
};


router.get("/", selectPrevtextCategory);
router.get("/:category", selectPrevText);
router.get("/:category/:text_id", viewPrevText);

module.exports = router;