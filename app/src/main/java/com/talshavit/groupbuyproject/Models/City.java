package com.talshavit.groupbuyproject.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class City {

    private String name;
    private LatLng latLng;
    public Map<String, LatLng> points = new HashMap<>();

    public City() {
    }

    public City(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public City setLatLng(LatLng latLng) {
        this.latLng = latLng;
        return this;
    }

    public Map<String, LatLng> getPoints() {
        return points;
    }

    public City setPoints(Map<String, LatLng> points) {
        this.points = points;
        return this;
    }
}
