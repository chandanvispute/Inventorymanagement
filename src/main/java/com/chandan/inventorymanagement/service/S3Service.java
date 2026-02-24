package com.chandan.inventorymanagement.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadImage(MultipartFile file);
}