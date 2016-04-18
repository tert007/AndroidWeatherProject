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

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;

        if (this == object)
            return true;

        if (!(object instanceof City))
            return false;

        City city = (City)object;

        if (!this.getName().equals(city.getName()))
            return false;

        if (!this.getCountry().equals(city.getCountry()))
            return false;

        if (this.getLatitude() != city.getLatitude() )
            return false;

        if (this.getLongitude() != city.getLongitude() )
            return false;

        if (this.getUnixUpdateTime() != city.getUnixUpdateTime() )
            return false;

        int weatherCount = this.getForecast().size();

        if (city.getForecast().size() != weatherCount)
            return false;

        for (int i = 0; i < weatherCount; i++){
            if (!this.getForecast().get(i).equals(city.getForecast().get(i)))
                return false;
        }

        return true;
    }

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
