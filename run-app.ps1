Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Application" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Start Backend
Write-Host "Starting Backend (Spring Boot)..." -ForegroundColor Yellow
Start-Process cmd -ArgumentList "/k", "cd /d C:\Users\Hp\JavaProject && C:\Users\Hp\Apache\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run" -WindowStyle Normal

Start-Sleep -Seconds 5

# Start Frontend
Write-Host "Starting Frontend (Next.js)..." -ForegroundColor Yellow
Start-Process cmd -ArgumentList "/k", "cd /d C:\Users\Hp\JavaProject\frontend && npm install && npm run dev" -WindowStyle Normal

Write-Host ""
Write-Host "Both services are starting in separate windows!" -ForegroundColor Green
Write-Host ""
Write-Host "Please wait 20-30 seconds for services to start." -ForegroundColor Yellow
Write-Host ""
Write-Host "Backend:  http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""

Start-Sleep -Seconds 25

# Open browser
Write-Host "Opening browser..." -ForegroundColor Green
Start-Process "http://localhost:3000"

Write-Host ""
Write-Host "Done! Check the command windows for any errors." -ForegroundColor Green
Write-Host ""


