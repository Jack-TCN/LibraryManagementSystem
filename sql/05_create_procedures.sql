@@ -54,50 +54,98 @@ BEGIN
        DECLARE @BorrowDays INT;
        SELECT @BorrowDays = rt.BorrowDays
        FROM Reader r
        JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID
        WHERE r.ReaderID = @ReaderID;
        
        -- 插入借阅记录
        INSERT INTO BorrowRecord (ReaderID, BookID, BorrowDate, DueDate, Status)
        VALUES (@ReaderID, @BookID, GETDATE(), DATEADD(DAY, @BorrowDays, GETDATE()), '借阅中');
        
        -- 更新图书库存
        UPDATE Book SET Stock = Stock - 1 WHERE BookID = @BookID;
        
        SET @Result = 1;
        SET @Message = '借书成功';
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        SET @Result = -99;
        SET @Message = ERROR_MESSAGE();
        ROLLBACK TRANSACTION;
    END CATCH
END;
GO

-- 3. 图书销售存储过程
CREATE PROCEDURE SP_SellBook
    @ReaderID INT,
    @BookID INT,
    @Quantity INT,
    @Result INT OUTPUT,
    @Message NVARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @Price DECIMAL(10,2), @Stock INT;
        SELECT @Price = Price, @Stock = Stock FROM Book WHERE BookID = @BookID;

        IF @Price IS NULL
        BEGIN
            SET @Result = -1;
            SET @Message = '图书不存在';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @Stock < @Quantity
        BEGIN
            SET @Result = -2;
            SET @Message = '图书库存不足';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        INSERT INTO SaleRecord (ReaderID, BookID, Quantity, SaleDate, TotalPrice)
        VALUES (@ReaderID, @BookID, @Quantity, GETDATE(), @Price * @Quantity);

        UPDATE Book SET Stock = Stock - @Quantity WHERE BookID = @BookID;

        SET @Result = 1;
        SET @Message = '购买成功';
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        SET @Result = -99;
        SET @Message = ERROR_MESSAGE();
        ROLLBACK TRANSACTION;
    END CATCH
END;
GO

-- 2. 还书存储过程
CREATE PROCEDURE SP_ReturnBook
    @RecordID INT,
    @Result INT OUTPUT,
    @Message NVARCHAR(200) OUTPUT,
    @Fine DECIMAL(10,2) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    
    BEGIN TRY
        -- 检查借阅记录
        IF NOT EXISTS (SELECT 1 FROM BorrowRecord WHERE RecordID = @RecordID AND Status = '借阅中')
        BEGIN
            SET @Result = -1;
            SET @Message = '借阅记录不存在或已归还';
            SET @Fine = 0;
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- 计算罚款
        DECLARE @DueDate DATETIME, @BookID INT;
        SELECT @DueDate = DueDate, @BookID = BookID
