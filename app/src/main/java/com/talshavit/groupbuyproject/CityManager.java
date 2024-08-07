package com.talshavit.groupbuyproject;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.talshavit.groupbuyproject.models.City;

import java.util.ArrayList;
import java.util.HashMap;

public class CityManager {
    private ArrayList<City> cities;

    public CityManager() {
        cities = new ArrayList<>();
        initCities();
    }

    private void initCities() {
        Log.d("lala", "shir");
        addCity("תל אביב", new LatLng(32.0853, 34.7818), createPoints(
                "אוניברסיטת תל אביב", new LatLng(32.1093, 34.8554),
                "דיזינגוף סנטר", new LatLng(32.0753, 34.7747)
        ));

        addCity("ירושלים", new LatLng(31.7683, 35.2137), createPoints(
                "הכותל המערבי", new LatLng(31.7767, 35.2345),
                "גן סאקר", new LatLng(31.7814, 35.2050)
        ));

        addCity("באר שבע", new LatLng(31.2520, 34.7915), createPoints(
                "אוניברסיטת בן-גוריון בנגב", new LatLng(31.2622, 34.8013),
                "העיר העתיקה", new LatLng(31.2439, 34.7931)
        ));

        addCity("חיפה", new LatLng(32.7940, 34.9896), createPoints(
                "הגנים הבהאיים", new LatLng(32.8151, 34.9896),
                "חוף דדו", new LatLng(32.7940, 34.9620)
        ));
    }

    private void addCity(String name, LatLng location, HashMap<String, LatLng> points) {
        City city = new City(name, location);
        city.points.putAll(points);
        cities.add(city);
    }

    private HashMap<String, LatLng> createPoints(Object... points) {
        HashMap<String, LatLng> pointsMap = new HashMap<>();
        for (int i = 0; i < points.length; i += 2) {
            pointsMap.put((String) points[i], (LatLng) points[i + 1]);
        }
        return pointsMap;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
}
