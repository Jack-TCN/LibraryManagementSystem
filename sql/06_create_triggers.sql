-- 1. 借书时检查并更新逾期状态的触发器
CREATE TRIGGER TR_UpdateOverdueStatus
ON BorrowRecord
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE BorrowRecord
    SET Status = '已逾期'
    WHERE Status = '借阅中' 
    AND DueDate < GETDATE()
    AND ReturnDate IS NULL;
END;
GO

-- 2. 删除读者前检查是否有未还图书的触发器
CREATE TRIGGER TR_CheckBeforeDeleteReader
ON Reader
INSTEAD OF DELETE
AS
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM BorrowRecord br
        JOIN deleted d ON br.ReaderID = d.ReaderID
        WHERE br.Status = '借阅中'
    )
    BEGIN
        RAISERROR('该读者有未归还的图书，无法删除', 16, 1);
        ROLLBACK TRANSACTION;
    END
    ELSE
    BEGIN
        DELETE FROM Reader WHERE ReaderID IN (SELECT ReaderID FROM deleted);
    END
END;
GO
