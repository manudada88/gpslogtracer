package com.pegasagro.gpslogreader.entity;

import java.io.Serializable;

import static com.pegasagro.gpslogreader.utilities.GPSLogsParseAndCreateDD.EARTH_RADIUS;

public class Coordinates implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -6002231258219738843L;
	private Double longitute;
    private Double latitude;

    public Coordinates(Double longitute, Double latitude) {
        this.longitute = longitute;
        this.latitude = latitude;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitute() {
        return longitute;
    }

    public Double getLatitude() {
        return latitude;
    }

    public double distanceToCoordinate(Coordinates b){
        double distance = 0;
        if(b == null){
            return distance;
        }

        // Convert degrees to radians
        double lat1Rad = Math.toRadians(this.latitude);
        double lon1Rad = Math.toRadians(this.longitute);
        double lat2Rad = Math.toRadians(b.getLatitude());
        double lon2Rad = Math.toRadians(b.getLongitute());

        // Haversine formula
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;
        double ab = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(ab), Math.sqrt(1 - ab));
        return EARTH_RADIUS * c;
    }

    public String toString() {
        return "[" + longitute + "," + latitude + "]";
    }

}
