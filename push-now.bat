@echo off
echo ========================================
echo Pushing to GitHub...
echo ========================================
echo.
echo Repository: https://github.com/CodingManiac44/bulk-product-upload
echo.
echo When prompted:
echo - Username: CodingManiac44
echo - Password: Your GitHub Personal Access Token
echo.
pause

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
    echo 1. Repository exists on GitHub
    echo 2. Token has correct permissions
    echo 3. Internet connection
)

echo.
pause


