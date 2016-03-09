package com.example.alexander.test2;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.test2.bean.*;
import com.example.alexander.test2.dao.*;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    AsyncTask<Double, Void, Void> mTask;

    GPSTracker gps;

    double lon, lag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка 1");
        tabSpec.setContent(R.id.linearLayout1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Влававдка 2");
        tabSpec.setContent(R.id.linearLayout2);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag2");

        final TextView cityNameLabel = (TextView) findViewById(R.id.cityNameLabel);
        final TextView countryNameLabel = (TextView) findViewById(R.id.countryNameLabel);
        final TextView degreesLabel = (TextView) findViewById(R.id.degreesLabel);



        mTask = new AsyncTask<Double, Void, Void>() {
            Weather weather;
            City city;

            @Override
            protected Void doInBackground(Double... params) {
                try {

                    //if (gps.canGetLocation()) {
                    String request = CityRequestBuilder.createFindPlaceByGeolocationRequest(params[0], params[1]);

                    List<City> cities =  JSONCityParser.findCity(request);
                    if (cities != null) {
                        city = cities.get(0);
                    } else {
                        return null;
                    }

                    String request1 = WeatherRequestBuilder.createDayForecastWeatherRequest(params[0], params[1]);
                    weather = JSONWeatherParser.createDayForecast(request1);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                if (city != null && weather != null) {
                    cityNameLabel.setText(city.getName());
                    countryNameLabel.setText(city.getCountry());

                    degreesLabel.setText(String.valueOf(weather.getTemperatureByCelsius()));

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Что-то не так!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        };

        gps = new GPSTracker(getApplicationContext());

        if(gps.canGetLocation()) {
            mTask.execute(gps.getLatitude(), gps.getLongitude());
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Что-то не так!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.button: resutTextView.setText("ТЕКСТ!");
              //  break;

        }
    }

}
