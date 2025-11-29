package com.assignment.service;

import com.assignment.dto.ProductDto;
import com.assignment.dto.ProductResponse;
import com.assignment.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private FileStorageService fileStorageService;

    public List<Product> uploadProducts(MultipartFile[] files) throws IOException {
        List<Product> products = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Product product = fileStorageService.saveProduct(file);
                products.add(product);
            }
        }
        
        return products;
    }

    public ProductResponse getAllProducts(int page, int size) throws IOException {
        File[] productFolders = fileStorageService.getAllProductFolders();
        List<Product> allProducts = new ArrayList<>();

        for (File folder : productFolders) {
            String folderName = folder.getName();
            if (folderName.startsWith("product_")) {
                String productId = folderName.substring("product_".length());
                try {
                    Product product = fileStorageService.loadProductInfo(productId);
                    allProducts.add(product);
                } catch (IOException e) {
                    // Skip corrupted products
                    System.err.println("Error loading product " + productId + ": " + e.getMessage());
                }
            }
        }

        // Sort by creation date (newest first)
        allProducts.sort(Comparator.comparing(Product::getCreatedAt).reversed());

        // Calculate pagination
        int totalElements = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, totalElements);

        // Get page data
        List<Product> pageProducts = start < totalElements 
            ? allProducts.subList(start, end) 
            : new ArrayList<>();

        // Convert to DTOs
        List<ProductDto> productDtos = pageProducts.stream()
            .map(p -> new ProductDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getImageUrl(),
                p.getCreatedAt(),
                p.getFileSize(),
                p.getContentType()
            ))
            .collect(Collectors.toList());

        return new ProductResponse(productDtos, totalPages, totalElements, page, size);
    }
}

