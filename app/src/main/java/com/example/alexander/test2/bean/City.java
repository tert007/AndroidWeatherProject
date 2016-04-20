package com.example.alexander.test2.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexander on 28.02.2016.
 */
public class City {
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    private List<Weather> weathers;
    private long updateTime;

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

        if (this.getUpdateTime() != city.getUpdateTime() )
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

    public long getUpdateTime() {
        return updateTime;
    }

    public String getUpdateTimeByString() {
        Date date = new Date(updateTime);

        Locale locale = new Locale("ru");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss", locale);

        return sdf.format(date);
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
