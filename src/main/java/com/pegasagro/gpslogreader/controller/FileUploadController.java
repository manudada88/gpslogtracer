package com.pegasagro.gpslogreader.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadController {
    public String uploadAndProcessGpsFile(@RequestParam("file") MultipartFile file, Model theModel);

}
