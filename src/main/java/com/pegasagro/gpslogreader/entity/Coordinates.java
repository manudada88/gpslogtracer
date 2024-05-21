package com.pegasagro.gpslogreader.entity;

import java.io.Serializable;

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

    public String toString() {
        return "[" + longitute + "," + latitude + "]";
    }

}
