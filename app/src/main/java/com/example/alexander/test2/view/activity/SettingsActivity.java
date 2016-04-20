package com.example.alexander.test2.view.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.dao.DaoException;
import com.example.alexander.test2.dao.file.FileDao;
import com.example.alexander.test2.service.ConnectionTracker1;
import com.example.alexander.test2.service.SearchCityAsyncTaskResponce;
import com.example.alexander.test2.service.SearchCitiesAsyncTask;
import com.example.alexander.test2.service.UpdateForecastAsyncTask;
import com.example.alexander.test2.service.UpdateForecastAsyncTaskResponse;
import com.example.alexander.test2.view.AddCityOnClickListener;
import com.example.alexander.test2.view.FavoriteCityOnClickListener;
import com.example.alexander.test2.view.RemoveCityOnClickListener;
import com.example.alexander.test2.view.adapter.SavedCityListViewAdapter;
import com.example.alexander.test2.view.adapter.SearchCityListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchCityAsyncTaskResponce, AddCityOnClickListener,
        RemoveCityOnClickListener, FavoriteCityOnClickListener,
        CompoundButton.OnCheckedChangeListener, UpdateForecastAsyncTaskResponse {

    final String SWITCH_SETTINGS_FILE_PATH = "switch_status";

    ListView savedCityListView;
    ListView searchCityListView;
    TextView savedCityStatusTextView;
    TextView searchCityStatusTextView;
    SwitchCompat switchCompat;
    Toolbar toolbar;
    SearchView searchView;

    Menu menu;

    List<City> savedCity;
    List<City> searchCity;
    City favoriteCity;

    ConnectionTracker1 connectionTracker;

    SearchCityListViewAdapter searchCityAdapter;
    SavedCityListViewAdapter savedCityAdapter;

    SearchCitiesAsyncTask searchCitiesAsyncTask;
    UpdateForecastAsyncTask updateForecastAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savedCityListView = (ListView) findViewById(R.id.saved_city_list_view);
        searchCityListView = (ListView) findViewById(R.id.search_city_list_view);
        savedCityStatusTextView = (TextView) findViewById(R.id.saved_city_status);
        searchCityStatusTextView = (TextView) findViewById(R.id.search_city_status);

        savedCityListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        switchCompat = (SwitchCompat) findViewById(R.id.place_switch);
        switchCompat.setOnCheckedChangeListener(this);

        connectionTracker = new ConnectionTracker1(getApplicationContext());

        try {
            savedCity = FileDao.loadCities(getApplicationContext());
            favoriteCity = FileDao.loadFavoriteCity(getApplicationContext());

            SavedCityListViewAdapter.setFavoriteCity(favoriteCity);

            if (savedCity == null) {
                savedCity = new ArrayList<>();
            }
            savedCityListUpdate();

        } catch (DaoException ex){

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (switchCompat.isChecked()){

            searchCityStatusTextView.setVisibility(View.INVISIBLE);
            savedCityStatusTextView.setVisibility(View.INVISIBLE);
            searchCityListView.setVisibility(View.INVISIBLE);
            savedCityListView.setVisibility(View.INVISIBLE);

            searchView.setVisibility(View.INVISIBLE);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);

        } else {
            searchCityStatusTextView.setVisibility(View.VISIBLE);
            savedCityStatusTextView.setVisibility(View.VISIBLE);
            searchCityListView.setVisibility(View.VISIBLE);
            savedCityListView.setVisibility(View.VISIBLE);

            searchView.setVisibility(View.VISIBLE);

            MenuItem searchItem =  menu.findItem(R.id.action_search);
            searchItem.setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_city_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        this.menu = menu;

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchStatus = preference.getBoolean(SWITCH_SETTINGS_FILE_PATH, true);
        switchCompat.setChecked(switchStatus);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (connectionTracker.canConnect()){
            searchCitiesAsyncTask = new SearchCitiesAsyncTask();
            searchCitiesAsyncTask.delegate = this;
            searchCitiesAsyncTask.execute(query);
        } else {
            Snackbar.make(findViewById(R.id.settings_layout), getString(R.string.internet_connection_not_found), Snackbar.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchCityListView.setAdapter(null);
        searchCityStatusTextView.setText(null);

        return false;
    }

    @Override
    public void searchCityAsyncTaskFinish(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            searchCityStatusTextView.setText(getString(R.string.cities_not_found));
        } else {
            searchCity = cities;

            searchCityStatusTextView.setText(getString(R.string.search_result));
            searchCityAdapter = new SearchCityListViewAdapter(getApplicationContext(), R.id.search_city_list_view, cities);
            searchCityAdapter.addButtonDelegate = this;
            searchCityListView.setAdapter(searchCityAdapter);
        }
    }

    private void savedCityListUpdate() {
        if (savedCity == null || savedCity.isEmpty()) {
            savedCityStatusTextView.setText(getString(R.string.saved_cities_not_found));
            savedCityListView.setAdapter(null);
        } else{
            savedCityStatusTextView.setText(getString(R.string.saved_cities));
            savedCityAdapter = new SavedCityListViewAdapter(getApplicationContext(), R.id.saved_city_list_view, savedCity);
            savedCityAdapter.removeButtonDelegate = this;
            savedCityAdapter.favoriteButtonDelegate = this;
            savedCityListView.setAdapter(savedCityAdapter);
         }
    }

    @Override
    public void addCityOnClickListener(City city) {
        if (savedCity.contains(city)){
            Snackbar.make(findViewById(R.id.settings_layout), getString(R.string.duplicate_city_save), Snackbar.LENGTH_LONG).show();
        } else {
            updateForecastAsyncTask = new UpdateForecastAsyncTask();
            updateForecastAsyncTask.delegate = this;
            updateForecastAsyncTask.execute(city);
        }
    }

    @Override
    public void favoriteCityOnClickListener(City city) {
        favoriteCity = city;
        savedCityAdapter.removeButtonDelegate = this;
        savedCityAdapter.favoriteButtonDelegate = this;
        savedCityListView.setAdapter(savedCityAdapter);
    }

    @Override
    public void removeCityOnClickListener(City city) {
        savedCity.remove(city);

        if (city.equals(favoriteCity)){
            favoriteCity = null;
        }

        savedCityListUpdate();
    }

    @Override
    public void updateForecastAsyncTaskFinish(City city) {
        savedCity.add(city);
        savedCityListUpdate();
    }


    @Override
    protected void onPause() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SWITCH_SETTINGS_FILE_PATH, switchCompat.isChecked());
        editor.apply();

        try {
            FileDao.saveCities(getApplicationContext(), savedCity);
            FileDao.saveFavoriteCity(getApplicationContext(), favoriteCity);
        } catch (DaoException e) {

        }

        super.onPause();
    }

}
