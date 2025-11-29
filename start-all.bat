@echo off
echo ========================================
echo Starting Full Application
echo ========================================
echo.

echo [1/2] Starting Backend (Spring Boot)...
start "Backend - Spring Boot" cmd /k "cd /d %~dp0 && if exist C:\Users\Hp\Apache\apache-maven-3.8.8\bin\mvn.cmd (C:\Users\Hp\Apache\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run) else (mvn spring-boot:run)"

echo Waiting 3 seconds...
timeout /t 3 /nobreak >nul

echo [2/2] Starting Frontend (Next.js)...
start "Frontend - Next.js" cmd /k "cd /d %~dp0\frontend && npm install && npm run dev"

echo.
echo ========================================
echo Both services are starting!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Waiting 25 seconds for services to start...
timeout /t 25 /nobreak >nul

echo Opening browser...
start http://localhost:3000

echo.
echo Done! Check the opened command windows.
echo Press any key to exit this window...
pause >nul

