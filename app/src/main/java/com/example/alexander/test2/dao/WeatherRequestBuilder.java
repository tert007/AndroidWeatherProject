package com.example.alexander.test2.dao;

/**
 * Created by Alexander on 21.02.2016.
 */
public class WeatherRequestBuilder extends Connector {

    private static final String url = "http://api.openweathermap.org/data/2.5/";
    private static final String apiKey = "931232b4146274addeead2a9242a138a";

    public static String createDayForecastWeatherRequest(double latitude, double longitude) throws DaoException {
        String request = url + "weather" + "?lat=" + latitude + "&lon=" + longitude + "&units=metric&APPID=" + apiKey;

        try {
            return executeRequest(request);
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public static String createWeekForecastWeatherRequest(double latitude, double longitude) throws DaoException {
        String request = url + "forecast/daily" + "?lat=" + latitude + "&lon=" + longitude + "&units=metric&cnt=7&APPID=" + apiKey;

        try {
            return Connector.executeRequest(request);
        } catch (DaoException ex) {
            throw ex;
        }
    }
}
