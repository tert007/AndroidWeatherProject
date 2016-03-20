package com.example.alexander.test2.dao.city;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.dao.DaoException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 28.02.2016.
 */
public class JSONCityParser {

    private static City createCity(JSONObject jsonObject) throws DaoException{
        try {
            JSONArray addressComponents = jsonObject.getJSONArray("address_components");

            String cityName = "Хуй";
            String country = "ХУЙ";

            for (int i = 0; i < addressComponents.length(); i++) {
                JSONArray addressTypeArray = addressComponents.getJSONObject(i).getJSONArray("types");
                if (addressTypeArray.get(0).equals("locality"))   {
                    cityName = addressComponents.getJSONObject(i).getString("long_name");
                }
                if (addressTypeArray.get(0).equals("country"))   {
                    country = addressComponents.getJSONObject(i).getString("long_name");
                }
            }
            //String cityName = addressComponents.getJSONObject(addressComponents.length() - 2).getString("long_name");
            //String country = addressComponents.getJSONObject(addressComponents.length() - 1).getString("long_name");
            double latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            return new City(cityName, country, latitude, longitude);
        } catch (JSONException ex) {
            throw new DaoException(ex);
        }
    }

    public static List<City> findCityByName(String request) throws DaoException {
        try {
            JSONObject jsonObject = new JSONObject(request);

            if (jsonObject.getString("status").equals("ZERO_RESULTS")) {
                return null;
            }

            if (!jsonObject.getString("status").equals("OK")) {
                throw new DaoException("Google JSON rofl");
            }

            JSONArray results = jsonObject.getJSONArray("results");

            List<City> cities = new ArrayList<>();

            for (int i = 0; i < results.length(); i++){
                cities.add(createCity(results.getJSONObject(i)));
            }

            return cities;
        } catch (JSONException ex) {
            throw new DaoException(ex);
        }
    }

    public static City findCityByGeolocation(String request) throws DaoException {
        try {
            JSONObject jsonObject = new JSONObject(request);

            if (jsonObject.getString("status").equals("ZERO_RESULTS")) {
                return null;
            }

            if (!jsonObject.getString("status").equals("OK")) {
                throw new DaoException("Google JSON rofl");
            }

            JSONArray results = jsonObject.getJSONArray("results");

            return createCity(results.getJSONObject(0));
        } catch (JSONException ex) {
            throw new DaoException(ex);
        }
    }
}
