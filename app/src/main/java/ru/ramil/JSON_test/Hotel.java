/*
 * Copyright (c) 2016.
 * Ramil Zainullin
 * zainullinramil@gmail.com
 * +7 963 306-16-01
 *
 */

package ru.ramil.JSON_test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 21.03.2016.
 */
    public class Hotel{
    private String name;
    private long idhotel;
    private long id;
    private String image;
    private String address;
    private double stars;
    private double distance;
    private double lat;
    private double lon;
    private String suites_availability;
    private int availab_count;

    public Hotel(){

    }

    public Hotel(long _id, String _name, long _idhotel, String _image, String _address, double _stars, double _distance, String _suites_availability, int _availab_count, double _lat, double _lon) {
        id = _id;
        name = _name;
        idhotel = _idhotel;
        image = _image;
        address = _address;
        stars = _stars;
        distance = _distance;
        lat = _lat;
        lon = _lon;
        suites_availability = _suites_availability;
        availab_count = _availab_count;
    }

    public String getName() {
        return name;
    }

    public long getIDhotel() {
        return idhotel;
    }

    public long getID() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public double getStars() {
        return stars;
    }

    public double getDistance() {
        return distance;
    }
    public double getLat() { return lat; }
    public double getLon() {
        return lon;
    }

    public String getSuitesAv() {
        return suites_availability;
    }
    public int getCountAv() { return availab_count; }
}
