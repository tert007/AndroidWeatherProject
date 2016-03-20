package com.example.alexander.test2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;

import java.util.List;

/**
 * Created by Alexander on 16.03.2016.
 */
public class SavedCityListViewAdapter extends ArrayAdapter<City> {

    public SavedCityListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SavedCityListViewAdapter(Context context, int resource, List<City> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.saved_city_list_item, null);
        }

        City city = getItem(position);

        if (city != null) {
            TextView cityNameItem = (TextView) view.findViewById(R.id.city_name_item);
            TextView countryNameItem = (TextView) view.findViewById(R.id.country_name_item);

            if (cityNameItem != null) {
                cityNameItem.setText(city.getName());
                countryNameItem.setText(city.getCountry());
            }
        }

        return view;
    }

}