-- =============================================
-- 01_create_database.sql - 创建数据库（修正版）
-- 使用项目目录下的database文件夹
-- =============================================
USE master;
GO

-- 如果数据库存在则删除
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'LibraryDB')
BEGIN
    ALTER DATABASE LibraryDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE LibraryDB;
END
GO

-- 创建数据库 - 使用项目目录下的database文件夹
CREATE DATABASE LibraryDB
ON PRIMARY
(
    NAME = 'LibraryDB',
    FILENAME = 'C:\Users\27476\Desktop\LibraryManagementSystem\database\LibraryDB.mdf',
    SIZE = 50MB,
    MAXSIZE = 500MB,
    FILEGROWTH = 10MB
)
LOG ON
(
    NAME = 'LibraryDB_log',
    FILENAME = 'C:\Users\27476\Desktop\LibraryManagementSystem\database\LibraryDB_log.ldf',
    SIZE = 10MB,
    MAXSIZE = 100MB,
    FILEGROWTH = 5MB
);
GO

-- 设置数据库属性
ALTER DATABASE LibraryDB SET RECOVERY SIMPLE;
GO

USE LibraryDB;
GO

PRINT '✅ 数据库 LibraryDB 创建完成！';
PRINT '📁 数据库文件位置：C:\Users\27476\Desktop\LibraryManagementSystem\database\';
PRINT '📄 数据文件：LibraryDB.mdf';
PRINT '📋 日志文件：LibraryDB_log.ldf';