package com.example.alexander.test2.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Alexander on 28.02.2016.
 */
public class CityRequestBuilder extends Connector {

    private static final String findCityUrl = "http://maps.google.com/maps/api/geocode/json?address=";
    private static final String findLocationUrl = "http://maps.google.com/maps/api/geocode/json?latlng=";
    private static final String settingsPartOfUrl = "&language=ru&sensor=false";

    public static String createFindCityByNameRequest(String cityName) throws DaoException {
        try {
            String request =  findCityUrl + URLEncoder.encode(cityName, "utf-8") + settingsPartOfUrl;
            return executeRequest(request);
        } catch (UnsupportedEncodingException ex){
            throw new DaoException(ex); //
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public static String createFindPlaceByGeolocationRequest(double latitude, double longitude) throws DaoException {
        String request = findLocationUrl + latitude + "," + longitude + settingsPartOfUrl;

        try {
            return executeRequest(request);
        } catch (DaoException ex) {
            throw ex;
        }
    }
}