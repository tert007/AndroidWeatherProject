package com.example.alexander.test2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.*;
import com.example.alexander.test2.dao.DaoException;
import com.example.alexander.test2.dao.file.FileDao;
import com.example.alexander.test2.service.ConnectionTracker;
import com.example.alexander.test2.service.CreateCityByLocationAsyncTaskResponse;
import com.example.alexander.test2.service.GeolocationTracker;
import com.example.alexander.test2.service.CreateCityByLocationAsyncTask;
import com.example.alexander.test2.service.UpdateForecastAsyncTask;
import com.example.alexander.test2.service.UpdateForecastAsyncTaskResponse;
import com.example.alexander.test2.view.adapter.ForecastListViewAdapter;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements CreateCityByLocationAsyncTaskResponse,
        SwipeRefreshLayout.OnRefreshListener, UpdateForecastAsyncTaskResponse {

    final String SWITCH_SETTINGS_FILE_PATH = "switch_status";

    MenuItem settingsMenuItem;

    TextView cityNameLabel;
    TextView countryNameLabel;
    TextView degreesLabel;
    TextView weatherDescriptionLabel;
    TextView humidityLabel;
    TextView windSpeedLabel;
    TextView updateTimeLabel;
    ImageView weatherIcon;

    ListView listView;

    SwipeRefreshLayout firstRefreshLayout;
    SwipeRefreshLayout secondRefreshLayout;

    GeolocationTracker geolocationTracker;
    ConnectionTracker connectionTracker;

    CreateCityByLocationAsyncTask createCityByLocationAsyncTask;
    UpdateForecastAsyncTask updateForecastAsyncTask;

    City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getTabIndicator(getApplicationContext(), R.string.weather));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getTabIndicator(getApplicationContext(), R.string.weather_week_forecast));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        firstRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.tab1_refresher);
        firstRefreshLayout.setColorSchemeResources(R.color.update_color);
        firstRefreshLayout.setOnRefreshListener(this);

        secondRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.tab2_refresher);
        secondRefreshLayout.setColorSchemeResources(R.color.update_color);
        secondRefreshLayout.setOnRefreshListener(this);

        cityNameLabel = (TextView) findViewById(R.id.cityNameLabel);
        countryNameLabel = (TextView) findViewById(R.id.countryNameLabel);
        degreesLabel = (TextView) findViewById(R.id.degreesLabel);
        weatherDescriptionLabel = (TextView) findViewById(R.id.weatherDescriptionLabel);
        humidityLabel = (TextView) findViewById(R.id.humidityLabel);
        windSpeedLabel = (TextView) findViewById(R.id.windSpeedLabel);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        updateTimeLabel = (TextView) findViewById(R.id.updateTimeLabel);
        settingsMenuItem = (MenuItem) findViewById(R.id.place_settings);

        listView = (ListView) findViewById(R.id.weekWeatherList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        geolocationTracker = new GeolocationTracker(getApplicationContext());
        connectionTracker = new ConnectionTracker(getApplicationContext());

        loadData();
    }

    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab, null);

        TextView tabTitle = (TextView) view.findViewById(R.id.tab_title);
        tabTitle.setText(title);
        return view;
    }

    private void loadData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean useCurrentPlace = preferences.getBoolean(SWITCH_SETTINGS_FILE_PATH, true);

        if (useCurrentPlace){
            if (connectionTracker.canConnect()){
                createCityByLocationAsyncTask = new CreateCityByLocationAsyncTask();
                createCityByLocationAsyncTask.delegate = this;

                createCityByLocationAsyncTask.execute(geolocationTracker.getLatitude(), geolocationTracker.getLongitude());
            } else {
                try {
                    City city = FileDao.loadTempCity(getApplicationContext());

                    if (city != null){
                        updateView(city);
                    } else {
                        Snackbar.make(findViewById(R.id.tabHost), getString(R.string.first_time_city_not_selected), Snackbar.LENGTH_LONG).show();
                    }

                } catch (DaoException e){
                    Snackbar.make(findViewById(R.id.tabHost), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        } else {
            try {
                City city = FileDao.loadFavoriteCity(getApplicationContext());

                if (city != null) {
                    if (connectionTracker.canConnect()){
                        updateForecastAsyncTask = new UpdateForecastAsyncTask();
                        updateForecastAsyncTask.delegate = this;

                        updateForecastAsyncTask.execute(city);
                    } else {
                        updateView(city);
                    }
                } else {
                    Snackbar.make(findViewById(R.id.tabHost), getString(R.string.city_not_selected), Snackbar.LENGTH_INDEFINITE).show();
                }
            } catch (DaoException e){
                Snackbar.make(findViewById(R.id.tabHost), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.place_settings :
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateView(City city){
        if (city != null) {
            ForecastListViewAdapter listViewAdapter = new ForecastListViewAdapter(getApplicationContext(), R.layout.weather_list_item, city.getForecast());
            listView.setAdapter(listViewAdapter);

            WeatherDescriptionHelper weatherDescriptionHelper = new WeatherDescriptionHelper();
            Weather weather = city.getForecast().get(0);

            cityNameLabel.setText(city.getName());
            countryNameLabel.setText(city.getCountry());

            degreesLabel.setText(weather.getTemperatureByCelsiusByString());
            weatherDescriptionLabel.setText(weatherDescriptionHelper.getWeatherDescription(weather.getWeatherDescriptionId()));
            weatherIcon.setImageResource(weatherDescriptionHelper.getWeatherIcon(weather.getWeatherDescriptionId()));

            String humidity = getString(R.string.humidity) + ' ' +  weather.getHumidity() + '%';
            String windSpeed = getString(R.string.wind_speed) + ' ' +  weather.getWindSpeed() + ' ' + getString(R.string.metric_speed_scale);
            String updateTime = getString(R.string.update_time) + ' ' +  city.getUpdateTimeByString();

            humidityLabel.setText(humidity);
            windSpeedLabel.setText(windSpeed);

            updateTimeLabel.setText(updateTime);
        }
    }

    @Override
    public void createCityByLocationAsyncTaskFinish(City city) {
        city.setUpdateTime((new Date().getTime()));
        this.city = city;
        updateView(city);
    }


    @Override
    public void onRefresh() {
        firstRefreshLayout.setRefreshing(false);
        secondRefreshLayout.setRefreshing(false);
        if (connectionTracker.canConnect()) {
            if (city == null) {
                createCityByLocationAsyncTask = new CreateCityByLocationAsyncTask();
                createCityByLocationAsyncTask.delegate = this;

                createCityByLocationAsyncTask.execute(geolocationTracker.getLatitude(), geolocationTracker.getLongitude());
            } else {
                updateForecastAsyncTask = new UpdateForecastAsyncTask();
                updateForecastAsyncTask.delegate = this;

                updateForecastAsyncTask.execute(city);
            }
        } else {
            Snackbar.make(findViewById(R.id.tabHost), getString(R.string.internet_connection_not_found), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateForecastAsyncTaskFinish(City city) {
        city.setUpdateTime((new Date().getTime()));
        this.city = city;
        updateView(city);
    }

    @Override
    protected void onPause() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean useCurrentPlace = preferences.getBoolean(SWITCH_SETTINGS_FILE_PATH, true);

        if(useCurrentPlace) {
            try {
                FileDao.saveTempCity(getApplicationContext(), city);
            } catch (DaoException e) {

            }
        }

        super.onPause();
    }
}
