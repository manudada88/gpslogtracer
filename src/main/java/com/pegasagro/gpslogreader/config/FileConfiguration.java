package com.pegasagro.gpslogreader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gps-log-reader")
public class FileConfiguration {
	private String filepath;
	private String destinationfilepath;
	
	public FileConfiguration() {
		
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getDestinationfilepath() {
		return destinationfilepath;
	}
	public void setDestinationfilepath(String destinationfilepath) {
		this.destinationfilepath = destinationfilepath;
	}
	
}
