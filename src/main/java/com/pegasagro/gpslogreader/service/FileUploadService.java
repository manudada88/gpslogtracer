package com.pegasagro.gpslogreader.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    public double storeAndProcessFile(MultipartFile file);
}
