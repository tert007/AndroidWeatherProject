package com.example.alexander.test2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.view.AddCityOnClickListener;

import java.util.List;

/**
 * Created by Alexander on 19.03.2016.
 */
public class SearchCityListViewAdapter extends ArrayAdapter<City> {

    public AddCityOnClickListener addButtonDelegate;

    public SearchCityListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SearchCityListViewAdapter(Context context, int resource, List<City> items) {
        super(context, resource, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.search_city_list_item, null);
        }

        final City city = getItem(position);

        if (city != null) {
            TextView cityNameItem = (TextView) view.findViewById(R.id.search_city_name_item);
            TextView countryNameItem = (TextView) view.findViewById(R.id.search_country_name_item);
            ImageView addItem = (ImageView) view.findViewById(R.id.add_new_city_item);

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addButtonDelegate.addCityOnClickListener(city);
                }
            });


            cityNameItem.setText(city.getName());
            countryNameItem.setText(city.getCountry());

        }

        return view;
    }

}