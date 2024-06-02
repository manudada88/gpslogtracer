package com.pegasagro.gpslogreader.utilities;


import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import com.pegasagro.gpslogreader.exceptions.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pegasagro.gpslogreader.entity.Coordinates;

// this class will read each record of file search for $GPGGA records
// create data of longitude and latitude from it
// this data will then be used by Map creating API
public class GPSLogsParseAndCreateDD {
	
	private static Logger LOGGER = LoggerFactory.getLogger(GPSLogsParseAndCreateDD.class);
	
	public static final double EARTH_RADIUS = 6371.0;

    public static double readFileAndProcess(String filePath) {
       List<Coordinates> CoordinateLists = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("$GPGGA")) {
                    Coordinates coordinates = processLine(line);
                    if (coordinates != null) {
                        //saving coordinates into a list to calculate distance and make map
                        CoordinateLists.add(coordinates);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("no of records generated into file: {} ", CoordinateLists.size());
        LOGGER.info("Calculating distance from records");
        double distanceTravelled = 0;
        if (CoordinateLists.size() >= 2) {
            distanceTravelled = IntStream.range(0, CoordinateLists.size() - 1).
                    mapToDouble(i-> CoordinateLists.get(i)
                            .distanceToCoordinate(CoordinateLists.get(i+1))).parallel()
                    .reduce(0, Double::sum);
        }else{
            LOGGER.info("Not Enough records to calculate distance ");
            throw new RecordNotFoundException("Not enough records to calculate distance");
        }
        writeJSFileForMap(CoordinateLists);
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

    public static void writeJSFileForMap(List<Coordinates> CoordinateLists) {
        String generatedJSFilePath = "src\\main\\resources\\static\\js\\script.js";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(generatedJSFilePath))) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
