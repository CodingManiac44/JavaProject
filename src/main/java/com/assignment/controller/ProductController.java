package com.assignment.controller;

import com.assignment.dto.ProductResponse;
import com.assignment.model.Product;
import com.assignment.service.FileStorageService;
import com.assignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadProducts(
            @RequestParam("files") MultipartFile[] files) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (files == null || files.length == 0) {
                response.put("success", false);
                response.put("message", "No files provided");
                return ResponseEntity.badRequest().body(response);
            }

            List<Product> products = productService.uploadProducts(files);
            
            response.put("success", true);
            response.put("message", "Successfully uploaded " + products.size() + " product(s)");
            response.put("products", products);
            response.put("count", products.size());
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error uploading products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            ProductResponse response = productService.getAllProducts(page, size);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images/{productId}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String productId) {
        try {
            Resource resource = fileStorageService.loadProductImage(productId);
            String contentType = "application/octet-stream";
            
            // Try to determine content type from file
            try {
                String filename = resource.getFilename();
                if (filename != null) {
                    if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                        contentType = "image/jpeg";
                    } else if (filename.toLowerCase().endsWith(".png")) {
                        contentType = "image/png";
                    } else if (filename.toLowerCase().endsWith(".gif")) {
                        contentType = "image/gif";
                    } else if (filename.toLowerCase().endsWith(".webp")) {
                        contentType = "image/webp";
                    }
                }
            } catch (Exception e) {
                // Use default content type
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

