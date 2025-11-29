@echo off
echo ========================================
echo Testing Application
echo ========================================
echo.

echo [1/3] Checking Backend (http://localhost:8080)...
timeout /t 2 /nobreak >nul
curl -s http://localhost:8080/api/products >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Backend is RUNNING!
) else (
    echo Backend is NOT running. Starting backend...
    start "Backend" cmd /k "cd /d %~dp0 && C:\Users\Hp\Apache\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run"
    echo Waiting 15 seconds for backend to start...
    timeout /t 15 /nobreak
)

echo.
echo [2/3] Checking Frontend (http://localhost:3000)...
curl -s http://localhost:3000 >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Frontend is RUNNING!
) else (
    echo Frontend is NOT running. Starting frontend...
    start "Frontend" cmd /k "cd /d %~dp0\frontend && npm install && npm run dev"
    echo Waiting 10 seconds for frontend to start...
    timeout /t 10 /nobreak
)

echo.
echo [3/3] Opening browser...
timeout /t 2 /nobreak
start http://localhost:3000

echo.
echo ========================================
echo Test Complete!
echo ========================================
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Press any key to exit...
pause >nul


