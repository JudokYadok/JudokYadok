const express = require("express");
const router = express.Router();

// 메모 목록 조회
const viewMemoList = (req, res) => {
    const { user_id } = req.params;
    const query = `
        SELECT memo.memo_id, memo.updatedAt, memo.title
        FROM memo
        WHERE user_id = ?;
    `;
    const values = [user_id];

  req.conn.query(query, values, (err, memos) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch memo list' });
          return;
      }

      const memoList = memos.map(memo => ({
          memo_id: memo.memo_id,
          title: memo.title,
          updatedAt: memo.updatedAt
      }));
      res.status(200).json(memoList); // 조회된 메모 리스트를 JSON 형태로 응답
  });
};

// 메모 조회
const viewMemo = (req, res) => {
  const { user_id, memo_id } = req.params; // URL 파라미터에서 memo_id 추출

  const query = `
      SELECT memo_id, title, contents
      FROM memo
      WHERE user_id = ? AND memo_id = ?;
  `;
  const values = [user_id, memo_id];

  req.conn.query(query, values, (err, memo) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to fetch memo' });
          return;
      }

      if (memo.length === 0) {
          res.status(404).json({ error: 'Memo not found' }); // 해당 memo_id에 해당하는 Memo가 없는 경우
      } else {
          res.status(200).json(memo[0]); // 조회된 메모를 JSON 형태로 응답
      }
  });
};

// 메모 추가
const addMemo = (req, res) => {
    const { user_id } = req.params;
    const { title, contents } = req.body; // 요청에서 JSON 데이터 추출

    const query = `
        INSERT INTO memo (user_id, title, contents)
        VALUES (?, ?, ?);
    `;
    const values = [user_id, title, contents];

    req.conn.query(query, values, (err, result) => {
        if (err) {
            console.error(err);
            res.status(500).json({ error: 'Failed to add memo to the library' });
            return;
        }

        // 새로 추가된 텍스트의 ID를 포함하여 응답
        res.status(200).json({ memo_id: result.insertId, title, contents });
    });
};

// 메모 수정
const modifyMemo = (req, res) => {
  const { user_id, memo_id } = req.params; // URL 파라미터에서 memo_id 추출
  const { contents } = req.body; // 요청에서 JSON 데이터 추출

  const query = `
      UPDATE memo
      SET contents = ?
      WHERE user_id = ? AND memo_id = ?;
  `;
  const values = [contents, user_id, memo_id];

  req.conn.query(query, values, (err, result) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to modify memo' });
          return;
      }

      if (result.affectedRows !== 0) {
          res.status(200).json({ message: 'Memo modified successfully' }); // 수정 성공 메시지 응답
      } else {
          res.status(404).json({ error: 'Memo not found' }); // 해당 memo_id에 해당하는 Memo가 없는 경우
      }
  });
};

// 메모 삭제
const deleteMemo = (req, res) => {
  const { user_id, memo_id } = req.params; // URL 파라미터에서 memo_id 추출

  const query = `
      DELETE FROM memo
      WHERE user_id = ? AND memo_id = ?;
  `;
  const values = [user_id, memo_id];

  req.conn.query(query, values, (err, result) => {
      if (err) {
          console.error(err);
          res.status(500).json({ error: 'Failed to delete memo' });
          return;
      }

      if (result.affectedRows !== 0) {
          res.status(200).json({ message: 'Memo deleted successfully' }); // 삭제 성공 메시지 응답
      } else {
          res.status(404).json({ error: 'Memo not found' }); // 해당 memo_id에 해당하는 Memo가 없는 경우
      }
  });
};


router.get("/:user_id", viewMemoList);
router.get("/:user_id/:memo_id", viewMemo);
router.post("/:user_id", addMemo);
router.put("/:user_id/:memo_id", modifyMemo);
router.delete("/:user_id/:memo_id", deleteMemo);

module.exports = router;