package com.example.alexander.test2.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander on 28.02.2016.
 */
public class Weather {
    private double temperature;
    private int humidity;
    private int weatherDescriptionId;
    private double windSpeed;
    private long unixTime;


    public int getWeatherDescriptionId() {
        return weatherDescriptionId;
    }

    public void setWeatherDescriptionId(int weatherDescriptionId) {
        this.weatherDescriptionId = weatherDescriptionId;
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

    public String getDate() {
        Date date = new Date(unixTime * 1000L);

        Locale locale = new Locale("ru");

        SimpleDateFormat sdf = new SimpleDateFormat("E d MMM", locale);

        return sdf.format(date);
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTemperatureByCelsius() {
        return temperature;
    }

    public String getTemperatureByCelsiusByString() {
        return String.format(Math.round(temperature) + "°C");
    }

    public double getTemperatureByFahrenheit() {
        return temperature * 9 / 5 + 32;
    }

    public String getTemperatureByFahrenheitByString() {
        return String.format(Math.round(getTemperatureByFahrenheit()) + "°F");
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;

        if (this == object)
            return true;

        if (!(object instanceof Weather))
            return false;

        Weather weather = (Weather)object;

        if (this.getHumidity() != weather.getHumidity())
            return false;

        if (this.getWindSpeed() != weather.getWindSpeed())
            return false;

        if (this.getUnixTime() != weather.getUnixTime())
            return false;

        if (this.getWeatherDescriptionId() != weather.getWeatherDescriptionId())
            return false;

        if (this.getTemperatureByCelsius() != weather.getTemperatureByCelsius())
            return false;

        return true;
    }
}
