package com.pegasagro.gpslogreader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pegasagro.gpslogreader.service.FileUploadService;

@Controller
public class FileUploadControllerImpl implements FileUploadController{
 
    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping("/getGpsPoints")
    public String uploadAndProcessGpsFile(@RequestParam("file") MultipartFile file, Model theModel) {
    	double distanceTravelled=0;
    	distanceTravelled = fileUploadService.storeAndProcessFile(file);
        theModel.addAttribute("distance", distanceTravelled + " Km");
        return "displayMap";
    }
    
    @GetMapping("/")
    public String showForm() {
        return "index";
    }
    
    
    @GetMapping("/getGpsPoints")
    public String showFormAgain() {
        return "index";
    }

}
