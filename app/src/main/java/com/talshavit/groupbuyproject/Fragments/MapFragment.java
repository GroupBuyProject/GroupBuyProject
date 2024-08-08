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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.talshavit.groupbuyproject.Checkout.CheckoutTabFragment;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap myMap;
    private TextView pickUp, pickUpCitySearch, addressSearch;
    private ImageButton mapBackButton;
    private Dialog dialog;
    private EditText searchBar_TXT_serch;
    private ListView searchBar_LISTV_listView;
    private ArrayAdapter<String> arrayAdapter;
    private AppCompatTextView addNewPoint;
    private String selectedPickupName;
    private ArrayList<String> allCitiesName, allPointsName;
    private ArrayList<City> allCities;
    private Map<String, LatLng> citiesMap = new HashMap<>();
    private androidx.appcompat.widget.AppCompatButton confirmLocation;
    private double price;
    private static final String PHONE_NUMBER = "+9720544885004";
    private static final String MESSAGE = "שלום, אני מעוניין להוסיף מיקום חדש לאיסוף.";

    public MapFragment(double price) {
        this.price = price;
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
        onConfirmButton();

        if (myMap != null) {
            myMap.clear();
        }
    }

    private void onConfirmButton() {
        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pickUp.getText().toString().isEmpty())
                    GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new CheckoutTabFragment(price));
                else
                    Toast.makeText(getContext(), "בחר/י מיקום לאיסוף המשלוח", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        initMapFragment();
        addNewPoint.setPaintFlags(addNewPoint.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        onAddNewPickUp();
        onSpinnerCity();
        onMapBackButton();
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onSpinnerCity() {
        pickUpCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUp.setText("");
                addressSearch.setText("בחר כתובת");
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

    private void onMapBackButton() {
        mapBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalResources.backToPrevFragment(requireActivity().getSupportFragmentManager());
            }
        });
    }

    private void clickOnListView() {
        searchBar_LISTV_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = arrayAdapter.getItem(position);
                pickUpCitySearch.setText(cityName);
                pickUp.setText("");
                addressSearch.setText("בחר כתובת");

                City selectedCity = null;
                for (City city : allCities) {
                    if (city.getName().equals(cityName)) {
                        selectedCity = city;
                        break;
                    }
                }

                if (selectedCity != null) {
                    LatLng latLan = selectedCity.getLatLng();
                    allPointsName.clear();
                    for (String pointName : selectedCity.getPoints().keySet()) {
                        allPointsName.add(pointName);
                    }

                    if (latLan != null && myMap != null) {
                        myMap.clear();
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLan, 12));
                        for (Map.Entry<String, LatLng> point : selectedCity.getPoints().entrySet()) {
                            myMap.addMarker(new MarkerOptions().position(point.getValue()).title(point.getKey()));
                        }
                    }
                    onSpinnerPoint();
                }

                dialog.dismiss();

            }
        });
    }

    private void clickOnListViewPoint() {
        searchBar_LISTV_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pointName = arrayAdapter.getItem(position);
                addressSearch.setText(pointName);
                pickUp.setText(pickUpCitySearch.getText().toString() + " - " + pointName);

                LatLng pointLatLng = null;
                for (City city : allCities) {
                    if (city.getName().equals(pickUpCitySearch.getText().toString())) {
                        pointLatLng = city.getPoints().get(pointName);
                        break;
                    }
                }

                if (pointLatLng != null && myMap != null) {
                    myMap.clear();
                    myMap.addMarker(new MarkerOptions().position(pointLatLng).title(pointName));
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointLatLng, 15));
                }

                dialog.dismiss();
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
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

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
        confirmLocation = view.findViewById(R.id.confirmLocation);
        mapBackButton = view.findViewById(R.id.mapBackButton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
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
                pickUp.setText(pickUpCitySearch.getText().toString() + " - " + selectedPickupName);
                addressSearch.setText(selectedPickupName);
                return false;
            }
        });
    }

    private void sendWhatsAppMessage(String phoneNumber, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    //    private void addPickupPoint(LatLng location, String title) {
//        myMap.addMarker(new MarkerOptions().position(location).title(title));
//    }

}