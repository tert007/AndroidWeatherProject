package com.example.alexander.test2.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alexander on 28.02.2016.
 */
public class Weather {
    private double temperature;
    private int humidity;
    private double windSpeed;
    private WeatherType weatherType;
    private long unixTime;


    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public String getDate() { /////////////////////////U
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        return sdf.format(date);
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTemperatureByCelsius() {
        return temperature;
    }

    public double getTemperatureByFahrenheit() {
        return temperature * 9 / 5 + 32;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
