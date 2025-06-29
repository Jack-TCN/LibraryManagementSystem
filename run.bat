@echo off
REM 重新编译Main类并运行系统

echo 🔧 重新编译Main类...

REM 检查Main.java是否存在
if not exist "src\com\library\Main.java" (
    echo ❌ Main.java文件不存在: src\com\library\Main.java
    pause
    exit /b 1
)
echo ✅ 找到Main.java文件

REM 检查JDBC驱动
for /f %%i in ('dir /b lib\mssql-jdbc-*.jar 2^>nul') do set JDBC_JAR=%%i
if "%JDBC_JAR%"=="" (
    echo ❌ JDBC驱动未找到
    pause
    exit /b 1
)
echo ✅ JDBC驱动: %JDBC_JAR%

REM 创建编译目录
if not exist "build\classes" mkdir "build\classes"

REM 重新编译所有Java文件（包括Main.java）
echo [1/3] 编译所有Java文件...
javac -d build\classes -cp "lib\%JDBC_JAR%;src" -encoding UTF-8 ^
    src\com\library\Main.java ^
    src\com\library\entity\*.java ^
    src\com\library\dao\*.java ^
    src\com\library\service\*.java ^
    src\com\library\util\*.java ^
    src\com\library\view\*.java ^
    src\com\library\view\panel\*.java ^
    src\com\library\view\dialog\*.java

if errorlevel 1 (
    echo ❌ 编译失败
    pause
    exit /b 1
)
echo ✅ 编译成功

REM 复制配置文件
echo [2/3] 复制配置文件...
copy src\resources\db.properties build\classes\ >nul
echo ✅ 配置文件复制完成

REM 检查Main.class是否存在
if exist "build\classes\com\library\Main.class" (
    echo ✅ Main.class编译成功
) else (
    echo ❌ Main.class未找到
    pause
    exit /b 1
)

REM 启动系统
echo [3/3] 启动图书管理系统...
echo ========================================
echo 🚀 系统启动中...
echo 📋 管理员账户：admin / admin123
echo ========================================
echo.

java -cp "build\classes;lib\%JDBC_JAR%" com.library.Main

pause