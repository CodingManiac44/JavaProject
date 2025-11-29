package com.assignment.service;

import com.assignment.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${product.storage.path}")
    private String storagePath;

    @Value("${product.image.url.base}")
    private String imageUrlBase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Product saveProduct(MultipartFile file) throws IOException {
        // Generate unique product ID
        String productId = UUID.randomUUID().toString();
        
        // Create product folder
        String productFolderName = "product_" + productId;
        Path productFolder = Paths.get(storagePath, productFolderName);
        Files.createDirectories(productFolder);

        // Save image file
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String imageFileName = "image" + fileExtension;
        Path imagePath = productFolder.resolve(imageFileName);
        Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Create product object
        Product product = new Product();
        product.setId(productId);
        product.setName(originalFilename != null ? originalFilename.replace(fileExtension, "") : "Product " + productId);
        product.setDescription("Product uploaded from image: " + originalFilename);
        product.setFolderPath(productFolder.toString());
        product.setImageUrl(imageUrlBase + "/" + productId);
        product.setFileSize(file.getSize());
        product.setContentType(file.getContentType());
        product.setCreatedAt(LocalDateTime.now());

        // Save product info as JSON
        Path jsonPath = productFolder.resolve("product-info.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonPath.toFile(), product);

        return product;
    }

    public Resource loadProductImage(String productId) throws IOException {
        Path productFolder = Paths.get(storagePath, "product_" + productId);
        
        if (!Files.exists(productFolder)) {
            throw new IOException("Product folder not found: " + productId);
        }

        // Find image file in the folder
        File folder = productFolder.toFile();
        File[] files = folder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".jpg") || 
            name.toLowerCase().endsWith(".jpeg") || 
            name.toLowerCase().endsWith(".png") || 
            name.toLowerCase().endsWith(".gif") ||
            name.toLowerCase().endsWith(".webp")
        );

        if (files == null || files.length == 0) {
            throw new IOException("Image file not found for product: " + productId);
        }

        Path imagePath = files[0].toPath();
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("Could not read image file: " + imagePath);
        }

        return resource;
    }

    public Product loadProductInfo(String productId) throws IOException {
        Path jsonPath = Paths.get(storagePath, "product_" + productId, "product-info.json");
        
        if (!Files.exists(jsonPath)) {
            throw new IOException("Product info not found: " + productId);
        }

        return objectMapper.readValue(jsonPath.toFile(), Product.class);
    }

    public File[] getAllProductFolders() {
        Path storage = Paths.get(storagePath);
        if (!Files.exists(storage)) {
            return new File[0];
        }
        File storageDir = storage.toFile();
        return storageDir.listFiles(File::isDirectory);
    }
}

