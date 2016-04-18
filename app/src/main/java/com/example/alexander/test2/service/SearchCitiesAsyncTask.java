package com.example.alexander.test2.service;

import android.os.AsyncTask;

import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.dao.DaoException;
import com.example.alexander.test2.dao.city.CityRequestBuilder;
import com.example.alexander.test2.dao.city.JSONCityParser;

import java.util.List;

/**
 * Created by Alexander on 19.03.2016.
 */
public class SearchCitiesAsyncTask extends AsyncTask<String, Void, List<City>> {

    public SearchCityAsyncTaskResponce delegate = null;

    @Override
    protected List<City> doInBackground(String... params) {
        try {
            String findCityRequest = CityRequestBuilder.createFindCityByNameRequest(params[0]);

            return JSONCityParser.findCityByName(findCityRequest);
        } catch (DaoException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        delegate.searchCityAsyncTaskFinish(cities);
    }
}