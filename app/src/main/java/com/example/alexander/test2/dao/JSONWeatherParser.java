package com.example.alexander.test2.dao;

import com.example.alexander.test2.bean.Weather;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 28.02.2016.
 */
public class JSONWeatherParser {

    private static Weather createWeather(JSONObject jsonObject) {
        try {
            Weather weather = new Weather();

            double temperature = jsonObject.getJSONObject("temp").getDouble("day");
            weather.setTemperature(temperature);

            //String weatherType = jsonObject.getJSONObject("weather").getString("description");
            //

            int humidity = jsonObject.getInt("humidity");
            weather.setHumidity(humidity);

            double windSpeed = jsonObject.getDouble("speed");
            weather.setWindSpeed(windSpeed);

            long date = jsonObject.getLong("dt");
            weather.setUnixTime(date);

            return weather;
        } catch (Exception ex){
            return null;
        }

    }


    public static List<Weather> createWeekForecast(String request)
    {
        try {
            List<Weather> weathers = new ArrayList<>(7); ///Const

            JSONObject jsonObject = new JSONObject(request);
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++) {
                weathers.add(createWeather((JSONObject) jsonArray.get(i)));
            }

            return weathers;
        } catch (Exception ex)
        {
            return null;
        }

    }

    public static Weather createDayForecast(String request)
    {
        try {
            Weather weather = new Weather();

            JSONObject jsonObject = new JSONObject(request);

            double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            weather.setTemperature(temperature);

            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            weather.setHumidity(humidity);

            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            weather.setWindSpeed(windSpeed);

            String weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            //

            long date = jsonObject.getLong("dt");
            weather.setUnixTime(date);

            return weather;
        } catch (Exception ex) {
            return null;
        }


    }

}
