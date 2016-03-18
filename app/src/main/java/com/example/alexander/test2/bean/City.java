package com.example.alexander.test2.bean;

import java.util.List;

/**
 * Created by Alexander on 28.02.2016.
 */
public class City {
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    private List<Weather> weathers;
    private long unixUpdateTime;

    public City(String name, String country ,double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;    }


    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setForecast(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public List<Weather> getForecast() {
        return weathers;
    }


    public long getUnixUpdateTime() {
        return unixUpdateTime;
    }

    public void setUnixUpdateTime(long unixUpdateTime) {
        this.unixUpdateTime = unixUpdateTime;
    }

}
