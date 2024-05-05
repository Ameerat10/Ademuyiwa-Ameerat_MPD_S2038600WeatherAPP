package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherapp.Activities.WeatherActivity;
import com.example.weatherapp.Models.City;
import com.example.weatherapp.R;

import java.util.ArrayList;


// Ameerat Ademuyiwa - S2038600

public class CitiesAdapter extends ArrayAdapter<City> {

    private Context mContext;
    private ArrayList<City> mCities;

    public CitiesAdapter(Context context, ArrayList<City> cities) {
        super(context, 0, cities);
        mContext = context;
        mCities = cities;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        City currentCity = mCities.get(position);

        TextView cityName = listItem.findViewById(R.id.city_name);
        cityName.setText(currentCity.getName());

        ImageView cityImage = listItem.findViewById(R.id.city_icon); // Assuming the ImageView ID is 'city_image'
        cityImage.setImageResource(currentCity.getCityImage()); // Make sure this method exists in your City model

        return listItem;
    }
}