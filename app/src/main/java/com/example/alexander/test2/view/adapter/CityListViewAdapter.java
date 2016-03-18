package com.example.alexander.test2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.bean.Weather;
import com.example.alexander.test2.dao.WeatherDescriptionHelper;

import java.util.List;

/**
 * Created by Alexander on 16.03.2016.
 */
public class CityListViewAdapter extends ArrayAdapter<City> {

    public CityListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CityListViewAdapter(Context context, int resource, List<City> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.city_list_item, null);
        }

        City city = getItem(position);

        if (city != null) {
            TextView cityNameItem = (TextView) view.findViewById(R.id.cityNameItem);

            if (cityNameItem != null) {
                cityNameItem.setText(city.getName());
            }
        }

        return view;
    }

}