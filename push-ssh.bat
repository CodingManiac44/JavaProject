@echo off
echo ========================================
echo Pushing to GitHub via SSH
echo ========================================
echo.

"C:\Program Files\Git\bin\git.exe" push -u origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo SUCCESS! Project pushed to GitHub!
    echo ========================================
    echo Repository URL: https://github.com/CodingManiac44/bulk-product-upload
) else (
    echo.
    echo Push failed. Please check:
    echo 1. SSH key is properly configured
    echo 2. Repository exists on GitHub
    echo 3. SSH agent is running (if required)
)

echo.
pause


