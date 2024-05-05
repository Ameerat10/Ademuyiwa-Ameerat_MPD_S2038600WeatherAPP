package com.example.weatherapp.Models;

import android.os.Parcel;
import android.os.Parcelable;


// Ameerat Ademuyiwa - S2038600


public class City implements Parcelable {
    private String name;
    private int id;
    private int cityImage;

    public City(String name, int id, int cityImage) {
        this.name = name;
        this.id = id;
        this.cityImage = cityImage;
    }

    protected City(Parcel in) {
        name = in.readString();
        id = in.readInt();
        cityImage = in.readInt();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCityImage() {
        return cityImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(cityImage);
    }
}
