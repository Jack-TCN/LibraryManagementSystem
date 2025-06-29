-- 1. 计算逾期天数的函数
CREATE FUNCTION FN_GetOverdueDays
(
    @DueDate DATETIME,
    @ReturnDate DATETIME
)
RETURNS INT
AS
BEGIN
    DECLARE @Days INT;
    
    IF @ReturnDate IS NULL
        SET @ReturnDate = GETDATE();
    
    IF @ReturnDate > @DueDate
        SET @Days = DATEDIFF(DAY, @DueDate, @ReturnDate);
    ELSE
        SET @Days = 0;
    
    RETURN @Days;
END;
GO

-- 2. 检查读者是否可以借书的函数
CREATE FUNCTION FN_CanBorrow
(
    @ReaderID INT
)
RETURNS BIT
AS
BEGIN
    DECLARE @CanBorrow BIT = 0;
    
    -- 检查读者状态
    IF EXISTS (SELECT 1 FROM Reader WHERE ReaderID = @ReaderID AND Status = '正常')
    BEGIN
        -- 检查是否有逾期未还
        IF NOT EXISTS (
            SELECT 1 FROM BorrowRecord 
            WHERE ReaderID = @ReaderID 
            AND Status = '借阅中' 
            AND DueDate < GETDATE()
        )
        BEGIN
            -- 检查是否达到借阅上限
            DECLARE @BorrowLimit INT, @CurrentBorrow INT;
            
            SELECT @BorrowLimit = rt.BorrowCount
            FROM Reader r
            JOIN ReaderType rt ON r.ReaderTypeID = rt.TypeID
            WHERE r.ReaderID = @ReaderID;
            
            SELECT @CurrentBorrow = COUNT(*)
            FROM BorrowRecord
            WHERE ReaderID = @ReaderID AND Status = '借阅中';
            
            IF @CurrentBorrow < @BorrowLimit
                SET @CanBorrow = 1;
        END
    END
    
    RETURN @CanBorrow;
END;
GO
