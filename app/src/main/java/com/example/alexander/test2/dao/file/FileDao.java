package com.example.alexander.test2.dao.file;

import android.content.Context;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.dao.DaoException;

import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 19.03.2016.
 */
public class FileDao {

    private static final String SAVED_CITIES_FILE_NAME = "cities";
    private static final String TEMP_CITY_FILE_NAME = "temp";
    private static final String FAVORITE_CITY_FILE_NAME = "favorite";

    private static City createCityFromJson(JSONObject cityObject) throws DaoException {
        try {
            String name = cityObject.getString("name");
            String country = cityObject.getString("country");
            double latitude = cityObject.getDouble("latitude");
            double longitude = cityObject.getDouble("longitude");
            long updateTime = cityObject.getLong("time");

            List<Weather> forecast = new ArrayList<Weather>();
            JSONArray forecastArray = cityObject.getJSONArray("forecast");

            for (int j = 0; j < forecastArray.length(); j++) {
                JSONObject weatherObject = forecastArray.getJSONObject(j);

                double temperature = weatherObject.getDouble("temperature");
                double windSpeed = weatherObject.getDouble("wind_speed");
                int humidity = weatherObject.getInt("humidity");
                long time = weatherObject.getLong("time");
                int weatherDescriptionId = weatherObject.getInt("weather_description_id");

                Weather weather = new Weather();

                weather.setWindSpeed(windSpeed);
                weather.setUnixTime(time);
                weather.setTemperature(temperature);
                weather.setHumidity(humidity);
                weather.setWeatherDescriptionId(weatherDescriptionId);

                forecast.add(weather);
            }


            City city = new City(name, country, latitude, longitude);
            city.setUnixUpdateTime(updateTime);
            city.setForecast(forecast);

            return city;

        }
        catch (JSONException e) {
            throw new DaoException(e);
        }
    }
    private static JSONObject createJsonFromCity(City city) throws DaoException {
        try {
            JSONObject cityObject = new JSONObject();

            cityObject.put("name", city.getName());
            cityObject.put("country", city.getCountry());
            cityObject.put("latitude", city.getLatitude());
            cityObject.put("longitude", city.getLongitude());
            cityObject.put("time", city.getUnixUpdateTime());

            JSONArray forecast = new JSONArray();

            for (Weather weather: city.getForecast()) {
                JSONObject weatherResult = new JSONObject();

                weatherResult.put("temperature", weather.getTemperatureByCelsius());
                weatherResult.put("time", weather.getUnixTime());
                weatherResult.put("wind_speed", weather.getWindSpeed());
                weatherResult.put("humidity", weather.getHumidity());
                weatherResult.put("weather_description_id", weather.getWeatherDescriptionId());

                forecast.put(weatherResult);
            }
            cityObject.put("forecast", forecast);

            return cityObject;
        } catch (JSONException e){
            throw new DaoException(e);
        }
    }

    private static void saveFile(Context context, String fileName, String content) throws DaoException {

        File file = new File(context.getFilesDir(), fileName);

        try {
            FileOutputStream stream = new FileOutputStream(file);

            if (content == null){
                return;
            }

            stream.write(content.getBytes());
        } catch (IOException e){
            throw new DaoException(e);
        }
    }
    private static String loadFile(Context context, String fileName) throws DaoException {

        File file = new File(context.getFilesDir(), fileName);

        try {
            int length = (int) file.length();

            if (length == 0)
                return null;

            byte[] bytes = new byte[length];

            FileInputStream inputStream = new FileInputStream(file);
            try {
                inputStream.read(bytes);
            } finally {
                inputStream.close();
            }
            String contents = new String(bytes);

            return contents;
        } catch (IOException e){
            throw new DaoException(e);
        }
    }

    public static City loadFavoriteCity(Context context) throws DaoException {

        String content = loadFile(context, FAVORITE_CITY_FILE_NAME);

        if (content == null) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(content);
            return createCityFromJson(jsonObject);
        } catch (JSONException e){
            throw new DaoException(e);
        }
    }

    public static City loadTempCity(Context context) throws DaoException {

        String content = loadFile(context, TEMP_CITY_FILE_NAME);

        if (content == null) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(content);
            return createCityFromJson(jsonObject);
        } catch (JSONException e){
            throw new DaoException(e);
        }
    }

    public static void saveFavoriteCity(Context context, City city) throws DaoException {
        if (city == null){
            saveFile(context, FAVORITE_CITY_FILE_NAME, null);
        } else {
            String content = createJsonFromCity(city).toString();

            saveFile(context, FAVORITE_CITY_FILE_NAME, content);
        }
    }

    public static void saveTempCity(Context context, City city) throws DaoException {
        if (city == null) {
            saveFile(context, TEMP_CITY_FILE_NAME, null);
        } else {
            String content = createJsonFromCity(city).toString();

            saveFile(context, TEMP_CITY_FILE_NAME, content);
        }
    }

    public static void saveCities(Context context, List<City> cities) throws DaoException {
        try {
            if (cities == null || cities.isEmpty()) {
                saveFile(context, SAVED_CITIES_FILE_NAME, null);
                return;
            }

            JSONObject resultObject = new JSONObject();

            JSONArray cityArray = new JSONArray();

            for (int i = 0; i < cities.size(); i++) {
                JSONObject cityObject = createJsonFromCity(cities.get(i));

                cityArray.put(i, cityObject);
            }
            resultObject.put("cities", cityArray);

            saveFile(context, SAVED_CITIES_FILE_NAME, resultObject.toString());

        } catch (JSONException ex ) {
            throw new DaoException("JSON " + ex);
        }
    }

    public static List<City> loadCities(Context context) throws DaoException{
        String contents = loadFile(context, SAVED_CITIES_FILE_NAME);

        if (contents == null)
            return null;

        try {
            JSONObject contentsObject = new JSONObject(contents);
            JSONArray cityArray =  contentsObject.getJSONArray("cities");

            List<City> cities = new ArrayList<>(cityArray.length());

            for (int i = 0; i < cityArray.length(); i++) {
                City city = createCityFromJson(cityArray.getJSONObject(i));

               cities.add(city);
            }

            return cities;

        } catch (JSONException ex){
            throw new DaoException("Load " + ex);
        }
    }
}
