package com.talshavit.groupbuyproject.Fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talshavit.groupbuyproject.General.Constants;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Order;
import com.talshavit.groupbuyproject.models.Payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckoutFragment extends Fragment {

    //private TextInputEditText etCardNumber, etExpiryDate, etCVV;
    private AppCompatButton btnPay;

    private EditText etCardNumber, etID, etCVV;
    private AutoCompleteTextView month, year;
    private TextInputLayout textInputLayoutMonth, textInputLayoutYear;
    private ItemsAdapterView itemsAdapterView;

    private String[] monthsArray, yearsArray;
    private String chosenMonth, chosenYear, cardNumberText;

    private double price;
    private boolean isSaveInfoPayment = true;


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
        onCreditCard();
        initMonths();
        initYear();
        onMonthClick();
        onYearClick();
        onPayBtn();
    }

    private void onYearClick() {
        year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenYear = yearsArray[i];
            }
        });
    }

    private void onMonthClick() {
        month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenMonth = monthsArray[i];
            }
        });
    }

    private void onCreditCard() {
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            private int deleteIndex;
            private boolean isDeleting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count > after) {
                    isDeleting = true;
                    deleteIndex = start;
                } else {
                    isDeleting = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) {
                    return;
                }

                isFormatting = true;

                String cleanString = s.toString().replaceAll(" ", "");

                if (cleanString.length() > Constants.CREDIT_CARD_NUMBER) {
                    cleanString = cleanString.substring(0, Constants.CREDIT_CARD_NUMBER);
                }

                if (isDeleting && deleteIndex < s.length() && s.charAt(deleteIndex) == ' ') {
                    s.delete(deleteIndex, deleteIndex + 1);
                }

                StringBuilder formatted = new StringBuilder(s.toString().replaceAll(" ", ""));
                for (int i = Constants.CREDIT_CARD_PART; i < formatted.length(); i += Constants.CREDIT_CARD_SPACE) {
                    formatted.insert(i, " ");
                }

                etCardNumber.removeTextChangedListener(this);
                etCardNumber.setText(formatted.toString());
                etCardNumber.setSelection(formatted.length());
                etCardNumber.addTextChangedListener(this);

                isFormatting = false;
            }
        });
    }

    private void initYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> yearsList = new ArrayList<>();
        for (int i = currentYear; i <= currentYear + 20; i++) {
            yearsList.add(String.valueOf(i));
        }
        yearsArray = yearsList.toArray(new String[0]);
        initSpinnerAdapter(year, yearsArray, textInputLayoutMonth);
    }

    private void initMonths() {
        monthsArray = new String[12];
        for (int i = 1; i <= 12; i++) {
            monthsArray[i - 1] = String.valueOf(i);
        }

        initSpinnerAdapter(month, monthsArray, textInputLayoutMonth);
    }

    private void initSpinnerAdapter(AutoCompleteTextView autoCompleteTextView, String[] items, TextInputLayout textInputLayout) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void onPayBtn() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                cardNumberText = etCardNumber.getText().toString().replaceAll(" ", "");
                if (etCardNumber.getText().length() < Constants.CREDIT_CARD_NUMBER) {
                    etCardNumber.setError("מספר כרטיס חייב להכיל 16 ספרות");
                    isValid = false;
                }
                if (etID.getText().length() < Constants.ID_NUMBERS) {
                    etID.setError("מספר תעודת זהות חייב להיות 9 ספרות");
                    isValid = false;
                }
                if (etCVV.getText().length() < Constants.CVV_NUMBERS) {
                    etCVV.setError("חייב להיות 3 ספרות");
                    isValid = false;
                }
                if (isValid) {
                    addHistoryToFirebase(view);
                    if (isSaveInfoPayment) {
                        addPaymentToFirebase(view);
                    }
                    GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new AcceptedPayment());
                }
            }
        });

    }

    private void addHistoryToFirebase(View view) {
        Order order = new Order(GlobalResources.cart, price);
        GlobalResources.user.addHistory(order);
        MainActivity.isPaid = true;
        GlobalResources.cart = new Cart();
        itemsAdapterView.changeCount();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        userReference.child("HistoriesList").setValue(GlobalResources.user.getHistories()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(view.getContext(), "Failed to save order: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addPaymentToFirebase(View view) {
        long cardNumber = Long.parseLong(cardNumberText);
        int idNumber = Integer.parseInt(etID.getText().toString());
        int year = Integer.parseInt(chosenYear);
        int month = Integer.parseInt(chosenMonth);
        int cvv = Integer.parseInt(etCVV.getText().toString());
        Payment payment = new Payment(cardNumber, idNumber, year, month, cvv);
        GlobalResources.user.addPayment(payment);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        userReference.child("PaymentsInfo").setValue(GlobalResources.user.getPayments()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(view.getContext(), "Failed to save order: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void findViews(View view) {
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etID = view.findViewById(R.id.etID);
        etCVV = view.findViewById(R.id.cvv);
        month = view.findViewById(R.id.month);
        year = view.findViewById(R.id.year);
        btnPay = view.findViewById(R.id.btnPay);
        textInputLayoutMonth = view.findViewById(R.id.textInputLayoutMonth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
}