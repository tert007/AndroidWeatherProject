package com.example.alexander.test2.service;

import android.os.AsyncTask;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.dao.DaoException;
import com.example.alexander.test2.dao.city.CityRequestBuilder;
import com.example.alexander.test2.dao.city.JSONCityParser;
import com.example.alexander.test2.dao.weather.JSONWeatherParser;
import com.example.alexander.test2.dao.weather.WeatherRequestBuilder;

import java.util.List;

/**
 * Created by Alexander on 11.03.2016.
 */
public class ForecastAsyncTask extends AsyncTask<Double, Void, City> {

    public AsyncTaskResponse delegate = null;

    private List<Weather> weathers;
    private City city;

    @Override
    protected City doInBackground(Double... params) {
        try {
            String findCityRequest = CityRequestBuilder.createFindPlaceByGeolocationRequest(params[0], params[1]);
            city =  JSONCityParser.findCityByGeolocation(findCityRequest);

            if (city == null) {
                return null; //
            }

            String findWeekForecastRequest = WeatherRequestBuilder.createWeekForecastWeatherRequest(params[0], params[1]);
            weathers = JSONWeatherParser.createWeekForecast(findWeekForecastRequest);

            String findDayForecastRequest = WeatherRequestBuilder.createDayForecastWeatherRequest(params[0], params[1]);
            weathers.set(0, JSONWeatherParser.createDayForecast(findDayForecastRequest));

            city.setForecast(weathers);

            return city;

        } catch (DaoException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(City city) {
        delegate.asyncTaskFinish(city);
    }
}
