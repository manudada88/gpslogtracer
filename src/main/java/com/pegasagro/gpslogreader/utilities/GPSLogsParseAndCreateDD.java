package com.pegasagro.gpslogreader.utilities;


import java.io.*;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pegasagro.gpslogreader.entity.Coordinates;

// this class will read each record of file search for $GPGGA records
// create data of longitude and latitude from it
// this data will then be used by Map creating API
public class GPSLogsParseAndCreateDD {
	
	private static Logger LOGGER = LoggerFactory.getLogger(GPSLogsParseAndCreateDD.class);
	
	private static final double EARTH_RADIUS = 6371.0;

    public static double readFileAndProcess(String filePath) {
       List<Coordinates> CoordinateLists = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("$GPGGA")) {
                    Coordinates coordinates = processLine(line);
                    if (coordinates != null) {
                        CoordinateLists.add(coordinates);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath2 = "src\\main\\resources\\static\\js\\script.js";
        // Process each string and write to file
        
        double distanceTravelled = 0;
        
        if(CoordinateLists.size()>=2) {
        for(int i = 0; i< CoordinateLists.size()-1;i++) {
        	distanceTravelled = distanceTravelled 
        			+ calculateDistanceBetweenToCoordinates(CoordinateLists.get(i),CoordinateLists.get(i+1));
        }}
        
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2))) {
                writer.write("""
                        let config = {
                          minZoom: 7,
                          maxZoom: 18,
                        };          
                        const zoom = 18;
                        """);
                writer.write("const lat =" + CoordinateLists.get(0).getLongitute() + ";");
                writer.newLine();
                writer.write("const lng = " + CoordinateLists.get(0).getLatitude() + ";");
                writer.newLine();
                writer.write("var polylinePoints = [" + CoordinateLists.get(0));
                writer.newLine();
                for (Coordinates c : CoordinateLists) {
                    writer.write("," + c.toString());
                    writer.newLine(); // Write each processed string on a new line
                }

                writer.write("""
                         ];
                        const map = L.map("map", config).setView([lat, lng], zoom);
                        var polyline = L.polyline(polylinePoints).addTo(map);
                        L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
                          attribution:
                            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
                        }).addTo(map);
                        """);
            writer.newLine();
            LOGGER.info("No of real records in the file: " + CoordinateLists.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distanceTravelled;
    }

    private static Coordinates processLine(String line) {
        String[] gpggaParts = line.split(",");
        // Extract latitude and longitude values and directions
        String latitudeValue = gpggaParts[2];
        String latitudeDirection = gpggaParts[3];
        String longitudeValue = gpggaParts[4];
        String longitudeDirection = gpggaParts[5];
        // Convert to decimal degrees
        if (latitudeValue.length() > 0 &&
                latitudeDirection.length() > 0 &&
                longitudeValue.length() > 0 &&
                longitudeDirection.length() > 0) {
            double latitude = convertToDecimalDegrees(latitudeValue, latitudeDirection);
            double longitude = convertToDecimalDegrees(longitudeValue, longitudeDirection);
            return new Coordinates(latitude, longitude);
        }
        return null;
    }

    // Method to convert latitude and longitude from GPGGA format to decimal degrees
    private static double convertToDecimalDegrees(String coordinate, String direction) {
        // Split degrees and minutes
        int degreeLength = (coordinate.contains(".") && coordinate.indexOf('.') > 4) ? 3 : 2;
        double degrees = Double.parseDouble(coordinate.substring(0, degreeLength));
        double minutes = Double.parseDouble(coordinate.substring(degreeLength));

        // Convert to decimal degrees
        double decimalDegrees = degrees + (minutes / 60);

        // If the direction is South or West, make the result negative
        if ("S".equals(direction) || "W".equals(direction)) {
            decimalDegrees *= -1;
        }
        return decimalDegrees;
    }
    
    public static double calculateDistanceBetweenToCoordinates(Coordinates a, Coordinates b) {

    	// put the latitude and longitude from the coordinates
    	double lat1=a.getLatitude();
    	double lon1=a.getLongitute();
    	double lat2=b.getLatitude();
    	double lon2=b.getLongitute();
    	
    	// Convert degrees to radians
    	double lat1Rad = Math.toRadians(lat1);
    	double lon1Rad = Math.toRadians(lon1);
    	double lat2Rad = Math.toRadians(lat2);
    	double lon2Rad = Math.toRadians(lon2);

    	// Haversine formula
    	double deltaLat = lat2Rad - lat1Rad;
    	double deltaLon = lon2Rad - lon1Rad;
    	double ab = Math.pow(Math.sin(deltaLat / 2), 2)
    	+ Math.cos(lat1Rad) * Math.cos(lat2Rad)
    	* Math.pow(Math.sin(deltaLon / 2), 2);
    	double c = 2 * Math.atan2(Math.sqrt(ab), Math.sqrt(1 - ab));

    	return EARTH_RADIUS * c;
    	}


}
