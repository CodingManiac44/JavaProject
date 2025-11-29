# Bulk Product Upload API

A full-stack application for uploading multiple product images with a REST API backend (Spring Boot) and a modern frontend (Next.js).

## Project Overview

This project implements a bulk product upload system where:
- Multiple product images can be uploaded in a single API request
- Each product gets its own folder on the server
- Product information is stored as JSON files within each product folder
- Products can be retrieved with pagination
- Images are accessible via direct URLs that display in the browser

## Architecture & Design Decisions

### Backend (Spring Boot)

**Technology Stack:**
- **Spring Boot 3.2.0**: Modern Java framework for building REST APIs
- **Maven**: Dependency management and build tool
- **Java 17**: Latest LTS version for better performance and features

**Key Design Decisions:**

1. **Folder-based Storage Structure**
   - Each product is stored in its own folder: `products/product_{productId}/`
   - This approach provides:
     - Easy product isolation
     - Simple backup and deletion of individual products
     - Clear organization of product data and images
   - Product information is stored as `product-info.json` within each folder

2. **RESTful API Design**
   - `POST /api/products/upload`: Accepts multiple files in a single request
   - `GET /api/products`: Returns paginated product list
   - `GET /api/products/images/{productId}`: Serves product images with proper content types

3. **File Storage Service**
   - Centralized service (`FileStorageService`) handles all file operations
   - Automatic folder creation for each product
   - JSON serialization for product metadata
   - Image file detection and serving with proper MIME types

4. **Pagination**
   - Server-side pagination to handle large product catalogs
   - Configurable page size (default: 10 items per page)
   - Returns total count and page information

5. **CORS Configuration**
   - Enabled CORS for frontend integration
   - Allows cross-origin requests from Next.js application

### Frontend (Next.js)

**Technology Stack:**
- **Next.js 14**: React framework with App Router
- **TypeScript**: Type safety and better developer experience
- **Axios**: HTTP client for API calls
- **Modern CSS**: Custom styling with gradients and animations

**Key Design Decisions:**

1. **Upload UI Features**
   - Drag-and-drop file upload support
   - Multiple file selection
   - Real-time upload progress bar with percentage
   - File list preview before upload

2. **Product Display**
   - Grid layout for product cards
   - Clickable images that open in new tab
   - Product metadata display (name, description, date, file size)
   - Responsive design

3. **Pagination**
   - Client-side pagination controls
   - Page navigation (Previous/Next buttons)
   - Current page and total pages display

4. **User Experience**
   - Loading states during API calls
   - Success/error message notifications
   - Auto-refresh after successful upload
   - Error handling with user-friendly messages

## Project Structure

```
JavaProject/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── assignment/
│       │           ├── BulkProductUploadApplication.java
│       │           ├── config/
│       │           │   └── CorsConfig.java
│       │           ├── controller/
│       │           │   └── ProductController.java
│       │           ├── dto/
│       │           │   ├── ProductDto.java
│       │           │   └── ProductResponse.java
│       │           ├── model/
│       │           │   └── Product.java
│       │           └── service/
│       │               ├── FileStorageService.java
│       │               └── ProductService.java
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── app/
│   │   ├── layout.tsx
│   │   ├── page.tsx
│   │   ├── globals.css
│   │   └── api/
│   │       └── route.ts
│   ├── package.json
│   ├── tsconfig.json
│   └── next.config.js
├── pom.xml
└── README.md
```

## Prerequisites

### Backend
- Java 17 or higher
- Maven 3.6 or higher

### Frontend
- Node.js 18 or higher
- npm or yarn

## How to Run the Project

### Backend Setup

1. **Navigate to the project root:**
   ```bash
   cd JavaProject
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

4. **Verify the API is running:**
   - Open `http://localhost:8080/api/products` in your browser
   - You should see an empty product list (or products if any exist)

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

4. **Open in browser:**
   - Navigate to `http://localhost:3000`
   - You should see the upload interface

### Production Build (Optional)

**Backend:**
```bash
mvn clean package
java -jar target/bulk-product-upload-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
npm start
```

## API Endpoints

### Upload Products
```
POST /api/products/upload
Content-Type: multipart/form-data

Request Body:
- files: Array of image files

Response:
{
  "success": true,
  "message": "Successfully uploaded X product(s)",
  "products": [...],
  "count": X
}
```

### Get All Products (Paginated)
```
GET /api/products?page=0&size=10

Response:
{
  "products": [...],
  "totalPages": X,
  "totalElements": Y,
  "currentPage": 0,
  "pageSize": 10
}
```

### Get Product Image
```
GET /api/products/images/{productId}

Response: Image file with appropriate content-type
```

## Configuration

### Backend Configuration (`application.properties`)

- `server.port`: Server port (default: 8080)
- `product.storage.path`: Path where products are stored (default: `./products`)
- `product.image.url.base`: Base URL for product images
- `spring.servlet.multipart.max-file-size`: Maximum file size (default: 10MB)
- `spring.servlet.multipart.max-request-size`: Maximum request size (default: 50MB)

### Frontend Configuration

- API base URL is set in `app/page.tsx` as `API_BASE_URL = 'http://localhost:8080/api/products'`
- Modify this if your backend runs on a different port or host

## Features Implemented

✅ **Backend Requirements:**
- REST API endpoint for bulk product image upload
- Separate folder creation for each product
- JSON file storage for product information
- Product list API with pagination
- Image serving with proper URLs
- Spring Boot framework

✅ **Frontend Requirements (Full-Stack):**
- File upload UI with multiple image support
- Upload progress bar with percentage
- Product list display with pagination
- Clickable image links that open in browser

## Testing the Application

1. **Start both backend and frontend** (follow instructions above)

2. **Upload Products:**
   - Go to `http://localhost:3000`
   - Drag and drop multiple images or click to select
   - Click "Upload Products"
   - Watch the progress bar
   - See success message

3. **View Products:**
   - After upload, products appear in the grid
   - Click on any product image to view it in a new tab
   - Use pagination to navigate through pages

4. **Verify Storage:**
   - Check the `products/` folder in the project root
   - Each product should have its own folder
   - Each folder contains the image and `product-info.json`

## Troubleshooting

**Backend won't start:**
- Ensure Java 17+ is installed: `java -version`
- Check if port 8080 is available
- Verify Maven is installed: `mvn -version`

**Frontend won't start:**
- Ensure Node.js 18+ is installed: `node -v`
- Delete `node_modules` and run `npm install` again
- Check if port 3000 is available

**Images not displaying:**
- Ensure backend is running on port 8080
- Check CORS configuration
- Verify the `products` folder exists and has proper permissions

**Upload fails:**
- Check file size limits in `application.properties`
- Ensure sufficient disk space
- Verify file permissions for the `products` folder

## Future Enhancements

- Database integration for better product management
- Image compression and optimization
- Product editing and deletion
- Search and filter functionality
- User authentication
- Image thumbnail generation
- Product categories and tags

## License

This project is created for assignment purposes.

## Author

Created as part of a backend development assessment.

