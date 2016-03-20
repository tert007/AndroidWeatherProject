package com.example.alexander.test2.view.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alexander.test2.R;
import com.example.alexander.test2.bean.City;
import com.example.alexander.test2.service.SearchAsyncTaskResponce;
import com.example.alexander.test2.service.SearchCitiesAsyncTask;
import com.example.alexander.test2.view.adapter.SearchCityListViewAdapter;

import java.util.List;

public class CityListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchAsyncTaskResponce {

    ListView savedCityListView;
    ListView searchCityListView;
    TextView savedCityStatusTextView;
    TextView searchCityStatusTextView;
    SearchView searchView;

    List<City> savedCity;

    SearchCityListViewAdapter searchCityAdapter;

    SearchCitiesAsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savedCityListView = (ListView) findViewById(R.id.saved_city_list_view);
        searchCityListView = (ListView) findViewById(R.id.search_city_list_view);
        savedCityStatusTextView = (TextView) findViewById(R.id.saved_city_status);
        searchCityStatusTextView = (TextView) findViewById(R.id.search_city_status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_city_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        asyncTask = new SearchCitiesAsyncTask();
        asyncTask.delegate = this;
        asyncTask.execute(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchCityListView.setAdapter(null);
        return false;
    }

    @Override
    public void asyncTaskFinish(List<City> cities) {
        if (cities == null) {
            savedCityStatusTextView.setText("Ничего не найдено");
        } else {
            searchCityAdapter = new SearchCityListViewAdapter(getApplicationContext(), R.id.search_city_list_view, cities);
            searchCityListView.setAdapter(searchCityAdapter);
        }
    }
}
