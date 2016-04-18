package com.example.alexander.test2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.bean.WeatherDescriptionHelper;

import java.util.List;

/**
 * Created by Alexander on 13.03.2016.
 */

public class ForecastListViewAdapter extends ArrayAdapter<Weather> {

    private WeatherDescriptionHelper helper =  new WeatherDescriptionHelper();

    public ForecastListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ForecastListViewAdapter(Context context, int resource, List<Weather> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.weather_list_item, null);
        }

        Weather weather = getItem(position);

        if (weather != null) {
            TextView weatherDateLabel = (TextView) view.findViewById(R.id.firstLine);
            TextView weatherDegreesLabel = (TextView) view.findViewById(R.id.secondLine);

            if (weatherDateLabel != null) {
                weatherDateLabel.setText(weather.getDate());
            }

            if (weatherDegreesLabel != null) {
                weatherDegreesLabel.setText(weather.getTemperatureByCelsiusByString());
            }
        }

        return view;
    }

}