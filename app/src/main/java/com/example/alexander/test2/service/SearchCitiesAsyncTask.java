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
 * Created by Alexander on 19.03.2016.
 */
public class SearchCitiesAsyncTask extends AsyncTask<String, Void, List<City>> {

    public SearchAsyncTaskResponce delegate = null;

    private List<City> cities;

    @Override
    protected List<City> doInBackground(String... params) {
        try {

            String findCityRequest = CityRequestBuilder.createFindCityByNameRequest(params[0]);
            cities =  JSONCityParser.findCityByName(findCityRequest);

            return cities;

            /*

            String findWeekForecastRequest = WeatherRequestBuilder.createWeekForecastWeatherRequest(params[0], params[1]);
            weathers = JSONWeatherParser.createWeekForecast(findWeekForecastRequest);

            String findDayForecastRequest = WeatherRequestBuilder.createDayForecastWeatherRequest(params[0], params[1]);
            weathers.set(0, JSONWeatherParser.createDayForecast(findDayForecastRequest));

            city.setForecast(weathers);

            return city;
            */
        } catch (DaoException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        delegate.asyncTaskFinish(cities);
    }
}