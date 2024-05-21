package com.pegasagro.gpslogreader.service;


import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pegasagro.gpslogreader.config.FileConfiguration;
import com.pegasagro.gpslogreader.utilities.GPSLogsParseAndCreateDD;
import com.pegasagro.gpslogreader.utilities.UnzipFileUtility;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	@Autowired
	FileConfiguration fileConfiguration;

    private static Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    public double storeAndProcessFile(MultipartFile file){
    	
        String FILE_PATH = fileConfiguration.getFilepath();
        String DEST_DIR = fileConfiguration.getDestinationfilepath();
        
        String filePath = FILE_PATH + file.getOriginalFilename();
        try {
			file.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        double distanceTravelled = 0;
        if(FilenameUtils.getExtension(filePath).equals("zip")){
            LOGGER.info("File {} is zip file, required to unzip first", filePath);
            // unzip file
            String fileName = UnzipFileUtility.unzip(filePath, DEST_DIR);
            // New file process
            LOGGER.info("File {} is unzipped file, required to unzip first", fileName);
            distanceTravelled = GPSLogsParseAndCreateDD.readFileAndProcess(fileName);
            LOGGER.info("total distance Travelled {} ", distanceTravelled);
        }else{
            // process the file
            LOGGER.info("File is normal file");
            distanceTravelled = GPSLogsParseAndCreateDD.readFileAndProcess(filePath);
            LOGGER.info("total distance Travelled {} ", distanceTravelled);
        }


        return distanceTravelled;
    }
}
