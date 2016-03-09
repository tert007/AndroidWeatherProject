package com.example.alexander.test2.dao;

import com.example.alexander.test2.bean.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 28.02.2016.
 */
public class JSONCityParser {

    private static City createCity(JSONObject jsonObject) {
        try {
            JSONArray addressComponets = jsonObject.getJSONArray("address_components");

            String cityName = addressComponets.getJSONObject(addressComponets.length() - 2).getString("long_name");
            String country = addressComponets.getJSONObject(addressComponets.length() - 1).getString("long_name");
            double latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            return new City(cityName, country, latitude, longitude);
        } catch (Exception ex) {
            return null;
        }

    }

    public static List<City> findCity(String request) {
        try {
            JSONObject jsonObject = new JSONObject(request);

            if (!jsonObject.getString("status").equals("OK")) {
                return null; ///////////////////////////////////////////////////
            }

            JSONArray results = jsonObject.getJSONArray("results");

            List<City> cities = new ArrayList<>();

            for (int i = 0; i < results.length(); i++){
                cities.add(createCity(results.getJSONObject(i)));
            }

            return cities;
        } catch (Exception ex) {
            return null;
        }
    }
}
