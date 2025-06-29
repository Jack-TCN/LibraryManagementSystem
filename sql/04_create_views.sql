-- =============================================
-- 04_create_views.sql - 创建视图（修正版）
-- =============================================

USE LibraryDB;
GO

-- 删除现有视图（如果存在）
IF OBJECT_ID('V_ReaderBorrowInfo', 'V') IS NOT NULL
    DROP VIEW V_ReaderBorrowInfo;
GO

IF OBJECT_ID('V_BookStockStatus', 'V') IS NOT NULL
    DROP VIEW V_BookStockStatus;
GO

IF OBJECT_ID('V_OverdueBorrow', 'V') IS NOT NULL
    DROP VIEW V_OverdueBorrow;
GO

-- 1. 读者借阅信息视图（修正版）
CREATE VIEW V_ReaderBorrowInfo
AS
SELECT 
    r.ReaderID,
    r.ReaderName,
    rt.TypeName AS ReaderType,
    b.Title AS BookTitle,
    b.Author,
    br.BorrowDate,
    br.DueDate,
    br.ReturnDate,
    br.Status,
    CASE 
        WHEN br.ReturnDate IS NULL AND br.DueDate < GETDATE() 
        THEN DATEDIFF(DAY, br.DueDate, GETDATE())
        ELSE 0
    END AS OverdueDays
FROM BorrowRecord br
    INNER JOIN Reader r ON br.ReaderID = r.ReaderID
    INNER JOIN Book b ON br.BookID = b.BookID
    INNER JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID;
GO

-- 2. 图书库存状态视图（修正版）
CREATE VIEW V_BookStockStatus
AS
SELECT 
    b.BookID,
    b.ISBN,
    b.Title,
    b.Author,
    ISNULL(p.PublisherName, '未知出版社') AS PublisherName,
    ISNULL(bt.TypeName, '未分类') AS BookType,
    b.Price,
    b.TotalStock,
    b.Stock AS AvailableStock,
    (b.TotalStock - b.Stock) AS BorrowedCount
FROM Book b
    LEFT JOIN Publisher p ON b.PublisherID = p.PublisherID
    LEFT JOIN BookType bt ON b.BookTypeID = bt.TypeID;
GO

-- 3. 逾期借阅视图（修正版）
CREATE VIEW V_OverdueBorrow
AS
SELECT 
    r.ReaderID,
    r.ReaderName,
    ISNULL(r.Phone, '') AS Phone,
    b.BookID,
    b.Title,
    br.BorrowDate,
    br.DueDate,
    DATEDIFF(DAY, br.DueDate, GETDATE()) AS OverdueDays,
    CAST(DATEDIFF(DAY, br.DueDate, GETDATE()) * 0.5 AS DECIMAL(10,2)) AS Fine
FROM BorrowRecord br
    INNER JOIN Reader r ON br.ReaderID = r.ReaderID
    INNER JOIN Book b ON br.BookID = b.BookID
WHERE br.Status = '借阅中' 
    AND br.DueDate < GETDATE();
GO

-- 验证视图创建成功
PRINT '=== 视图创建完成 ===';

-- 测试读者借阅信息视图
SELECT 'V_ReaderBorrowInfo' AS ViewName, COUNT(*) AS RecordCount 
FROM V_ReaderBorrowInfo;

-- 测试图书库存状态视图
SELECT 'V_BookStockStatus' AS ViewName, COUNT(*) AS RecordCount 
FROM V_BookStockStatus;

-- 测试逾期借阅视图
SELECT 'V_OverdueBorrow' AS ViewName, COUNT(*) AS RecordCount 
FROM V_OverdueBorrow;

PRINT '✅ 所有视图创建并测试完成！';