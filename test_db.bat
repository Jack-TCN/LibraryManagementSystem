@echo off
REM ===========================================
REM 数据库连接测试脚本
REM test_db.bat
REM ===========================================

echo 🔍 测试数据库连接...
echo.

REM 设置变量
set PROJECT_ROOT=%cd%
set SRC_DIR=%PROJECT_ROOT%\src
set BUILD_DIR=%PROJECT_ROOT%\build\classes
set LIB_DIR=%PROJECT_ROOT%\lib

REM 检查文件
if not exist "%LIB_DIR%\mssql-jdbc-*.jar" (
    echo ❌ JDBC驱动未找到
    pause
    exit /b 1
)

REM 创建目录
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"

REM 编译DBUtil
echo 编译数据库工具类...
for /f %%i in ('dir /b "%LIB_DIR%\mssql-jdbc-*.jar"') do set JDBC_JAR=%%i
javac -d "%BUILD_DIR%" -cp "%LIB_DIR%\%JDBC_JAR%;%SRC_DIR%" -encoding UTF-8 "%SRC_DIR%\com\library\util\DBUtil.java"

if errorlevel 1 (
    echo ❌ 编译失败
    pause
    exit /b 1
)

REM 复制配置文件
if exist "%SRC_DIR%\resources\db.properties" (
    copy "%SRC_DIR%\resources\db.properties" "%BUILD_DIR%\" >nul
)

REM 测试连接
echo.
echo 🔗 开始测试数据库连接...
echo ========================================
java -cp "%BUILD_DIR%;%LIB_DIR%\%JDBC_JAR%" com.library.util.DBUtil
echo ========================================
echo.

pause