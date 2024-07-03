package com.talshavit.groupbuyproject.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsUserForOrderFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap myMap;
    private TextView pickUp, pickUpCitySearch, addressSearch;
    private Dialog dialog;
    private EditText searchBar_TXT_serch;
    private ListView searchBar_LISTV_listView;
    private ArrayAdapter<String> arrayAdapter;
    private AppCompatTextView addNewPoint;
    private String selectedPickupName;
    private ArrayList<String> allCitiesName, allPointsName;
    private ArrayList<City> allCities;
    private Map<String, LatLng> citiesMap = new HashMap<>();
    private static final String PHONE_NUMBER = "+9720544885004";
    private static final String MESSAGE = "שלום, אני מעוניין להוסיף מיקום חדש לאיסוף.";

    public DetailsUserForOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCitiesMap();
    }

    private void initCitiesMap() {
        GlobalResources.initCities();
        allCities = GlobalResources.allCities;
        allCitiesName = new ArrayList<>();
        allPointsName = new ArrayList<>();

        for (City city : allCities) {
            allCitiesName.add(city.getName());
            citiesMap.put(city.getName(), city.getLatLng());
            citiesMap.putAll(city.getPoints());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initViews();
    }

    private void initViews() {
        initMapFragment();
        addNewPoint.setPaintFlags(addNewPoint.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        onAddNewPickUp();
        onSpinnerCity();
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onSpinnerCity() {
        pickUpCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog(allCitiesName);
                clickOnListView();
            }
        });
    }

    private void onSpinnerPoint() {
        addressSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog(allPointsName);
                clickOnListViewPoint();
            }
        });
    }

    private void clickOnListView() {
        searchBar_LISTV_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickUpCitySearch.setText(arrayAdapter.getItem(position));
                String name = arrayAdapter.getItem(position);
                LatLng latLan = citiesMap.get(name);
                allPointsName.clear();
                for (String pointName : allCities.get(position).getPoints().keySet()) {
                    allPointsName.add(pointName);
                }

                if (latLan != null && myMap != null) {
                    myMap.clear();
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLan, 12));
                    for (Map.Entry<String, LatLng> point : allCities.get(position).getPoints().entrySet()) {
                        myMap.addMarker(new MarkerOptions().position(point.getValue()).title(point.getKey()));
                    }
                }
                onSpinnerPoint();
                dialog.dismiss();
            }
        });
    }

    private void clickOnListViewPoint() {
        searchBar_LISTV_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addressSearch.setText(arrayAdapter.getItem(position));
                dialog.dismiss();
                pickUp.setText(arrayAdapter.getItem(position));
            }
        });
    }

    private void searchListener() {
        searchBar_TXT_serch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initDialog(ArrayList<String> arrayList) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_searchig_bar);
        dialog.getWindow().setLayout(650, 700);
        dialog.show();

        searchBar_TXT_serch = dialog.findViewById(R.id.searchBar_TXT_serch);
        searchBar_LISTV_listView = dialog.findViewById(R.id.searchBar_LISTV_listView);
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        searchBar_LISTV_listView.setAdapter(arrayAdapter);

        searchListener();
    }

    private void onAddNewPickUp() {
        addNewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsAppMessage(PHONE_NUMBER, MESSAGE);
            }
        });

    }

    private void findViews(View view) {
        pickUp = view.findViewById(R.id.pickUp);
        addNewPoint = view.findViewById(R.id.addNewPoint);
        pickUpCitySearch = view.findViewById(R.id.pickUpCitySearch);
        addressSearch = view.findViewById(R.id.addressSearch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragnent_details_user_order, container, false);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng defaultLocation = new LatLng(31.5, 34.75); // Center of Israel
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 8));

        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedPickupName = marker.getTitle().toString();
                pickUp.setText(selectedPickupName);
                return false;
            }
        });

    }

    private void addPickupPoint(LatLng location, String title) {
        myMap.addMarker(new MarkerOptions().position(location).title(title));
    }

    private void sendWhatsAppMessage(String phoneNumber, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Log.d("lala", "whatsapp not install");
        }
    }
}