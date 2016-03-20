package com.example.alexander.test2.dao.file;

import android.content.Context;
import android.util.JsonWriter;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.dao.DaoException;

import org.json.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 19.03.2016.
 */
public class FileDao {

    private static final String FILE_NAME = "cities";

    public static void saveCities(Context context, List<City> cities) throws DaoException {
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            JSONObject resultObject = new JSONObject();

            JSONArray cityArray = new JSONArray();

            for (int i = 0; i < cities.size(); i++) {
                JSONObject cityObject = new JSONObject();

                cityObject.put("name", cities.get(0).getName());
                cityObject.put("country", cities.get(0).getCountry());
                cityObject.put("latitude", cities.get(0).getLatitude());
                cityObject.put("longitude", cities.get(0).getLongitude());
                cityObject.put("time", cities.get(0).getUnixUpdateTime());

                JSONArray forecast = new JSONArray();

                for (Weather weather: cities.get(0).getForecast()) {
                    JSONObject weatherResult = new JSONObject();

                    weatherResult.put("temperature", weather.getTemperatureByCelsius());
                    weatherResult.put("time", weather.getUnixTime());
                    weatherResult.put("wind_speed", weather.getWindSpeed());
                    weatherResult.put("humidity", weather.getHumidity());
                    weatherResult.put("weather_description_id", weather.getWeatherDescriptionId());

                    forecast.put(weatherResult);
                }
                cityObject.put("forecast", forecast);

                cityArray.put(i, cityObject);
            }

            resultObject.put("cities", cityArray);

            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(resultObject.toString().getBytes());
            } finally {
                stream.close();
            }
        } catch (JSONException ex ) {
            throw new DaoException("JSON " + ex);
        } catch (IOException ex) {
            throw new DaoException("SAVE " + ex);
        }
    }

    public static List<City> loadCities(Context context) throws DaoException{
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            FileInputStream inputStream = new FileInputStream(file);
            try {
                inputStream.read(bytes);
            }
            finally {
                inputStream.close();
            }
            String contents = new String(bytes);

            JSONObject contentsObject = new JSONObject(contents);
            JSONArray cityArray =  contentsObject.getJSONArray("cities");

            List<City> cities = new ArrayList<>(cityArray.length());

            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject cityObject = cityArray.getJSONObject(i);

                String name = cityObject.getString("name");
                String country = cityObject.getString("country");
                double latitude = cityObject.getDouble("latitude");
                double longitude = cityObject.getDouble("longitude");
                long updateTime = cityObject.getLong("time");

                List<Weather> forecast = new ArrayList<Weather>(7); ///////////////////////////
                JSONArray forecastArray = cityObject.getJSONArray("forecast");

                for (int j=0; j < forecastArray.length(); j++) {
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


                City  city = new City(name, country, latitude, longitude);
                city.setUnixUpdateTime(updateTime);
                city.setForecast(forecast);


                cities.add(city);
            }

            return cities;

        } catch (IOException | JSONException ex){
            throw new DaoException("Load " + ex);
        }
    }
}
