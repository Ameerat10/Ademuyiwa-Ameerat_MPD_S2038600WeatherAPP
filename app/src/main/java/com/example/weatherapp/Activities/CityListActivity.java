package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapp.Adapters.CitiesAdapter;
import com.example.weatherapp.Models.City;
import com.example.weatherapp.R;

import java.util.ArrayList;

// Ameerat Ademuyiwa - S2038600



public class CityListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        ArrayList<City> cities = new ArrayList<>();
        addCities(cities);

        ListView listView = findViewById(R.id.listview_cities);

        CitiesAdapter cityAdapter = new CitiesAdapter(this, cities);
        listView.setAdapter(cityAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            City city = (City) parent.getItemAtPosition(position);
            Intent intent = new Intent(CityListActivity.this, WeatherActivity.class);
            intent.putExtra("name", city.getName());
            intent.putExtra("id", city.getId());
            startActivity(intent);
        });
    }

    private void addCities(ArrayList<City> cities) {
        cities.add(new City("Tunis", 2464470, R.drawable.tunis));
        cities.add(new City("Glasgow", 2648579, R.drawable.glasgow));
        cities.add(new City("London", 2646743, R.drawable.london));
        cities.add(new City("New York", 5128581, R.drawable.newyork));
        cities.add(new City("Oman", 287286, R.drawable.muscat));
        cities.add(new City("Mauritius", 934154, R.drawable.portlouis));
        cities.add(new City("Bangladesh", 1185241, R.drawable.dhaka));
    }
}
