package com.example.alexander.test2.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.view.adapter.CityListViewAdapter;

import java.util.ArrayList;

public class CityListActivity extends AppCompatActivity {

    ListView cityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<City> a = new ArrayList<>();
        a.add(new City("1", "212", 12, 12));
        a.add(new City("12", "2122", 12, 12));
        a.add(new City("123", "21d2", 12, 12));
        a.add(new City("1234", "2we12", 12, 12));



        cityListView = (ListView) findViewById(R.id.cityListView);

        CityListViewAdapter cityListViewAdapter = new CityListViewAdapter(getApplicationContext(),  R.layout.city_list_item, a);
        cityListView.setAdapter(cityListViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_city_list, menu);
        return true;
    }
}
