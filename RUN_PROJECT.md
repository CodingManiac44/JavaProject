# How to Run the Project

## Prerequisites Installation

### 1. Install Java 17 or higher
- Download from: https://www.oracle.com/java/technologies/downloads/
- Or use OpenJDK: https://adoptium.net/
- After installation, verify: `java -version`

### 2. Install Maven
- Download from: https://maven.apache.org/download.cgi
- Extract and add to PATH
- Verify: `mvn --version`

### 3. Install Node.js 18 or higher
- Download from: https://nodejs.org/
- This includes npm
- Verify: `node --version` and `npm --version`

## Running the Project

### Step 1: Start Backend (Spring Boot)

Open a terminal/command prompt and run:

```bash
cd C:\Users\Hp\JavaProject
mvn spring-boot:run
```

Wait for: "Started BulkProductUploadApplication" message
Backend will run on: http://localhost:8080

### Step 2: Start Frontend (Next.js)

Open a NEW terminal/command prompt and run:

```bash
cd C:\Users\Hp\JavaProject\frontend
npm install
npm run dev
```

Wait for: "Ready on http://localhost:3000"
Frontend will run on: http://localhost:3000

### Step 3: Use the Application

1. Open browser: http://localhost:3000
2. Upload multiple product images
3. View the product list with pagination
4. Click images to view them

## Quick Start Scripts (Windows)

### run-backend.bat
```batch
@echo off
cd /d C:\Users\Hp\JavaProject
mvn spring-boot:run
pause
```

### run-frontend.bat
```batch
@echo off
cd /d C:\Users\Hp\JavaProject\frontend
call npm install
call npm run dev
pause
```

## Troubleshooting

- **Port 8080 already in use**: Change port in `application.properties`
- **Port 3000 already in use**: Next.js will suggest another port
- **Maven not found**: Add Maven bin directory to PATH
- **Java not found**: Add Java bin directory to PATH

