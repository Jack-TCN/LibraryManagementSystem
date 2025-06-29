-- 简单查询（5个以上）

-- 1. 模糊查询：按书名查询图书
SELECT * FROM Book WHERE Title LIKE '%数据%';

-- 2. 模糊查询：按作者查询图书
SELECT * FROM Book WHERE Author LIKE '张%';

-- 3. 统计查询：统计各类图书数量
SELECT bt.TypeName, COUNT(b.BookID) AS BookCount, SUM(b.TotalStock) AS TotalStock
FROM Book b
JOIN BookType bt ON b.BookTypeID = bt.TypeID
GROUP BY bt.TypeName
ORDER BY BookCount DESC;

-- 4. 统计查询：按月统计借阅量
SELECT 
    YEAR(BorrowDate) AS Year,
    MONTH(BorrowDate) AS Month,
    COUNT(*) AS BorrowCount
FROM BorrowRecord
GROUP BY YEAR(BorrowDate), MONTH(BorrowDate)
ORDER BY Year DESC, Month DESC;

-- 5. 条件查询：查询逾期未还图书
SELECT 
    r.ReaderName,
    b.Title,
    br.BorrowDate,
    br.DueDate,
    DATEDIFF(DAY, br.DueDate, GETDATE()) AS OverdueDays
FROM BorrowRecord br
JOIN Reader r ON br.ReaderID = r.ReaderID
JOIN Book b ON br.BookID = b.BookID
WHERE br.Status = '借阅中' AND br.DueDate < GETDATE();

-- 6. 排序查询：热门图书排行（借阅次数最多）
SELECT 
    b.Title,
    b.Author,
    COUNT(br.RecordID) AS BorrowTimes
FROM Book b
LEFT JOIN BorrowRecord br ON b.BookID = br.BookID
GROUP BY b.BookID, b.Title, b.Author
ORDER BY BorrowTimes DESC;

-- 多表查询（3个以上）

-- 1. 查询读者借阅详情
SELECT 
    r.ReaderName,
    rt.TypeName AS ReaderType,
    b.Title,
    b.Author,
    br.BorrowDate,
    br.DueDate,
    br.Status
FROM BorrowRecord br
JOIN Reader r ON br.ReaderID = r.ReaderID
JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID
JOIN Book b ON br.BookID = b.BookID
ORDER BY br.BorrowDate DESC;

-- 2. 查询图书详细信息（包含出版社和类型）
SELECT 
    b.BookID,
    b.ISBN,
    b.Title,
    b.Author,
    p.PublisherName,
    bt.TypeName AS BookType,
    b.Price,
    b.Stock,
    b.TotalStock
FROM Book b
LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID
LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID;

-- 3. 查询各类读者借阅统计
SELECT 
    rt.TypeName AS ReaderType,
    COUNT(DISTINCT r.ReaderID) AS ReaderCount,
    COUNT(br.RecordID) AS TotalBorrow,
    COUNT(CASE WHEN br.Status = '借阅中' THEN 1 END) AS CurrentBorrow
FROM ReaderType rt
LEFT JOIN Reader r ON rt.TypeID = r.ReaderTypeID
LEFT JOIN BorrowRecord br ON r.ReaderID = br.ReaderID
GROUP BY rt.TypeID, rt.TypeName;

-- 子查询（2个以上）

-- 1. 查询借阅次数最多的图书
SELECT * FROM Book
WHERE BookID IN (
    SELECT TOP 5 BookID
    FROM BorrowRecord
    GROUP BY BookID
    ORDER BY COUNT(*) DESC
);

-- 2. 查询从未被借阅的图书
SELECT * FROM Book
WHERE BookID NOT IN (
    SELECT DISTINCT BookID
    FROM BorrowRecord
);

-- 3. 查询借书数量达到上限的读者
SELECT r.*, rt.BorrowCount AS MaxBorrow
FROM Reader r
JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID
WHERE (
    SELECT COUNT(*)
    FROM BorrowRecord br
    WHERE br.ReaderID = r.ReaderID AND br.Status = '借阅中'
) >= rt.BorrowCount;