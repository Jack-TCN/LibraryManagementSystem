-- 1. 创建读者类型表
CREATE TABLE ReaderType
(
    TypeID INT PRIMARY KEY IDENTITY(1,1),
    TypeName NVARCHAR(50) NOT NULL,
    BorrowDays INT NOT NULL DEFAULT 30,        -- 可借天数
    BorrowCount INT NOT NULL DEFAULT 5,        -- 可借数量
    CreateTime DATETIME DEFAULT GETDATE()
);

-- 2. 创建读者表
CREATE TABLE Reader
(
    ReaderID INT PRIMARY KEY IDENTITY(1001,1),
    ReaderName NVARCHAR(50) NOT NULL,
    Gender NVARCHAR(2) CHECK(Gender IN ('男','女')),
    ReaderTypeID INT NOT NULL,
    Phone VARCHAR(20),
    Email VARCHAR(100),
    RegDate DATETIME DEFAULT GETDATE(),
    Status VARCHAR(20) DEFAULT '正常' CHECK(Status IN ('正常','挂失','注销')),
    Password VARCHAR(50) DEFAULT '123456'
);

-- 3. 创建图书类型表
CREATE TABLE BookType
(
    TypeID INT PRIMARY KEY IDENTITY(1,1),
    TypeName NVARCHAR(50) NOT NULL,
    Description NVARCHAR(200)
);

-- 4. 创建出版社表
CREATE TABLE Publisher
(
    PublisherID INT PRIMARY KEY IDENTITY(1,1),
    PublisherName NVARCHAR(100) NOT NULL,
    Phone VARCHAR(20),
    Address NVARCHAR(200)
);

-- 5. 创建图书表
CREATE TABLE Book
(
    BookID INT PRIMARY KEY IDENTITY(10001,1),
    ISBN VARCHAR(20) UNIQUE,
    Title NVARCHAR(100) NOT NULL,
    Author NVARCHAR(50),
    PublisherID INT,
    BookTypeID INT,
    Price DECIMAL(10,2) CHECK(Price >= 0),
    Stock INT DEFAULT 1 CHECK(Stock >= 0),     -- 库存数量
    TotalStock INT DEFAULT 1,                  -- 总库存
    InDate DATETIME DEFAULT GETDATE()
);

-- 6. 创建借阅记录表
CREATE TABLE BorrowRecord
(
    RecordID INT PRIMARY KEY IDENTITY(1,1),
    ReaderID INT NOT NULL,
    BookID INT NOT NULL,
    BorrowDate DATETIME DEFAULT GETDATE(),
    DueDate DATETIME NOT NULL,                 -- 应还日期
    ReturnDate DATETIME,                       -- 实际归还日期
    RenewCount INT DEFAULT 0,                  -- 续借次数
    Status VARCHAR(20) DEFAULT '借阅中' CHECK(Status IN ('借阅中','已归还','已逾期')),
    Fine DECIMAL(10,2) DEFAULT 0               -- 罚款金额
);

-- 7. 创建管理员表
CREATE TABLE Admin
(
    AdminID INT PRIMARY KEY IDENTITY(1,1),
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL,
    AdminName NVARCHAR(50),
    PermissionLevel INT DEFAULT 1              -- 权限级别
);
-- 8. 创建销售记录表
CREATE TABLE SaleRecord
(
    SaleID INT PRIMARY KEY IDENTITY(1,1),
    ReaderID INT,
    BookID INT NOT NULL,
    Quantity INT DEFAULT 1 CHECK(Quantity > 0),
    SaleDate DATETIME DEFAULT GETDATE(),
    TotalPrice DECIMAL(10,2) NOT NULL
);
