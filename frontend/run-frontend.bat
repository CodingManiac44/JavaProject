@echo off
echo Starting Next.js Frontend...
cd /d "%~dp0"
call npm install
if %ERRORLEVEL% NEQ 0 (
    echo npm install failed. Please check Node.js installation.
    pause
    exit /b 1
)
call npm run dev
pause

