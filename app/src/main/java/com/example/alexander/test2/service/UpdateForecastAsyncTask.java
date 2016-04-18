package com.example.alexander.test2.service;

import android.os.AsyncTask;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.dao.DaoException;
import com.example.alexander.test2.dao.weather.JSONWeatherParser;
import com.example.alexander.test2.dao.weather.WeatherRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.04.2016.
 */
public class UpdateForecastAsyncTask extends AsyncTask<City, Void, City> {

    public UpdateForecastAsyncTaskResponse delegate;

    @Override
    protected City doInBackground(City... params) {
        try {
            City city = params[0];
            List<Weather> weathers = new ArrayList<>();

            String findWeekForecastRequest = WeatherRequestBuilder.createWeekForecastWeatherRequest(city.getLatitude(), city.getLongitude());
            weathers = JSONWeatherParser.createWeekForecast(findWeekForecastRequest);

            String findDayForecastRequest = WeatherRequestBuilder.createDayForecastWeatherRequest(city.getLatitude(), city.getLongitude());
            weathers.set(0, JSONWeatherParser.createDayForecast(findDayForecastRequest));

            city.setForecast(weathers);

            return city;

        } catch (DaoException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(City city) {
        delegate.updateForecastAsyncTaskFinish(city);
    }

}
