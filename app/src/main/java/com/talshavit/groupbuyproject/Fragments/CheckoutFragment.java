package com.talshavit.groupbuyproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckoutFragment extends Fragment {

    //private TextInputEditText etCardNumber, etExpiryDate, etCVV;
    private MaterialButton btnPay;

    private EditText etCardNumber, etID, etCVV;
    private Spinner monthSpinner, yearSpinner;
    private ItemsAdapterView itemsAdapterView;

    private List<String> months, years;

    private double price;

    public CheckoutFragment(double price) {
        this.price = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initViews();
    }

    private void initViews() {
        itemsAdapterView = new ItemsAdapterView(getContext(), GlobalResources.items, "", -1);
        initMonths();
        initYear();
        initSpinnerMonth();
        initSpinnerYear();
        onPayBtn();
    }

    private void initYear() {
        years = new ArrayList<>();
        years.add("שנה");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 20; i++) {
            years.add(String.valueOf(i));
        }
    }

    private void initMonths() {
        months = new ArrayList<>();
        months.add("חודש");
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i));
        }
    }

    private void initSpinnerMonth() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, months) {


            @Override
            public boolean isEnabled(int position) {
                return position != 0; //enable all, except the first variable
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

    }

    private void initSpinnerYear() {
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, years) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0; //enable all, except the first variable
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
    }

    private void onPayBtn() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order(GlobalResources.cart, price);
                GlobalResources.user.addHistory(order);
                MainActivity.isPaid = true;
                GlobalResources.cart = new Cart();
                itemsAdapterView.changeCount();

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);


                userReference.child("histories").setValue(GlobalResources.user.getHistories()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Order saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "Failed to save order: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void findViews(View view) {
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etID = view.findViewById(R.id.etID);
        etCVV = view.findViewById(R.id.etCVV);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.year);
        btnPay = view.findViewById(R.id.btnPay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
}