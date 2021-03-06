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
import com.example.alexander.test2.view.FavoriteCityOnClickListener;
import com.example.alexander.test2.view.RemoveCityOnClickListener;

import java.util.List;

/**
 * Created by Alexander on 16.03.2016.
 */
public class SavedCityListViewAdapter extends ArrayAdapter<City> {

    private static City favoriteCity;

    public RemoveCityOnClickListener removeButtonDelegate;
    public FavoriteCityOnClickListener favoriteButtonDelegate;

    public SavedCityListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SavedCityListViewAdapter(Context context, int resource, List<City> items) {
        super(context, resource, items);
    }

    public static void setFavoriteCity(City city){
        favoriteCity = city;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.saved_city_list_item, null);
        }

        final City city = getItem(position);

        if (city != null) {
            TextView cityNameItem = (TextView) view.findViewById(R.id.city_name_item);
            TextView countryNameItem = (TextView) view.findViewById(R.id.country_name_item);
            ImageView removeItem = (ImageView) view.findViewById(R.id.remove_city_item);
            final ImageView favoriteItem = (ImageView) view.findViewById(R.id.favorite_city_item);

            favoriteItem.setSelected(false);

            cityNameItem.setText(city.getName());
            countryNameItem.setText(city.getCountry());

            if (city.equals(favoriteCity)){
                favoriteItem.setSelected(true);
            } else {
                favoriteItem.setSelected(false);
            }

            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeButtonDelegate.removeCityOnClickListener(city);
                }
            });

            favoriteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (city.equals(favoriteCity)) {
                        favoriteCity = null;
                    } else {
                        favoriteCity = city;
                    }

                    favoriteButtonDelegate.favoriteCityOnClickListener(favoriteCity);
                }

            });

        }

        return view;
    }

}