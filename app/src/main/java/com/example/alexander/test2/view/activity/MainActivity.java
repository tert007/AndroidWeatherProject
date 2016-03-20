package com.example.alexander.test2.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.*;
import com.example.alexander.test2.dao.*;
import com.example.alexander.test2.dao.file.FileDao;
import com.example.alexander.test2.service.AsyncTaskResponse;
import com.example.alexander.test2.service.ConnectionTracker;
import com.example.alexander.test2.service.ForecastAsyncTask;
import com.example.alexander.test2.view.adapter.ForecastListViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskResponse {

    MenuItem settingsMenuItem;
    TextView cityNameLabel;
    TextView countryNameLabel;
    TextView degreesLabel;
    TextView weatherDescriptionLabel;
    ListView listView;

    ConnectionTracker connectionTracker;
    ForecastAsyncTask forecastAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Погода");
        tabSpec.setContent(R.id.linearLayout1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Недельный прогноз");
        tabSpec.setContent(R.id.linearLayout2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        cityNameLabel = (TextView) findViewById(R.id.cityNameLabel);
        countryNameLabel = (TextView) findViewById(R.id.countryNameLabel);
        degreesLabel = (TextView) findViewById(R.id.degreesLabel);
        weatherDescriptionLabel = (TextView) findViewById(R.id.weatherDescriptionLabel);
        settingsMenuItem = (MenuItem) findViewById(R.id.place_settings);

        listView = (ListView) findViewById(R.id.weekWeatherList);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        connectionTracker = new ConnectionTracker(getApplicationContext());

        forecastAsyncTask = new ForecastAsyncTask();
        forecastAsyncTask.delegate = this;


        if (connectionTracker.canConnectToNetwork() || connectionTracker.canConnectToGps()) {
            connectionTracker.sendGeolocationRequest();

            forecastAsyncTask.execute(connectionTracker.getLatitude(), connectionTracker.getLongitude());
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "GPS или х проблем", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.place_settings :
                Intent intent = new Intent(this, CityListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void asyncTaskFinish(City city) {
        if (city != null) {

            ForecastListViewAdapter listViewAdapter = new ForecastListViewAdapter(getApplicationContext(), R.layout.weather_list_item, city.getForecast());
            listView.setAdapter(listViewAdapter);

            WeatherDescriptionHelper weatherDescriptionHelper = new WeatherDescriptionHelper();

            Weather weather = city.getForecast().get(0);

            ArrayList<City> l = new ArrayList<>();
            l.add(city);

            try{
                FileDao.saveCities(getApplicationContext(), l);
                City a = FileDao.loadCities(getApplicationContext()).get(0);
                Toast toast = Toast.makeText(getApplicationContext(), a.getCountry(), Toast.LENGTH_SHORT);
                toast.show();
            } catch (DaoException ex) {
                Toast t = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                t.show();
            }

            cityNameLabel.setText(city.getName());
            countryNameLabel.setText(city.getCountry());

            degreesLabel.setText(String.valueOf(weather.getTemperatureByCelsiusByString()));
            weatherDescriptionLabel.setText(weatherDescriptionHelper.getWeatherDescription(weather.getWeatherDescriptionId()));

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Что-то не так!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
