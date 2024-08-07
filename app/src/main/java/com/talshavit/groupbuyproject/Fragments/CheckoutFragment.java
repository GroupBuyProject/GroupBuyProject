package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talshavit.groupbuyproject.General.Constants;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.models.Order;
import com.talshavit.groupbuyproject.models.Payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CheckoutFragment extends Fragment {

    private AppCompatButton btnPay, btnPoints, btnCancelPoints;
    private EditText etCardNumber, etID, etCVV;
    private ImageButton checkoutBackButton;
    private AutoCompleteTextView month, year;
    private TextView cvv_explanation, points_question, totalPriceCheckout;
    private ItemsAdapterView itemsAdapterView;
    private String[] monthsArray, yearsArray;
    private String chosenMonth, chosenYear, cardNumberText, userID;
    private ShapeableImageView cvv_explain_button;
    private View.OnFocusChangeListener focusChangeListener;
    private double price, virtualCurrencies, originalPrice;
    private boolean isSaveInfoPayment = true, isUsedPoint = false;
    private DatabaseReference userReference;
    private MainActivity mainActivity;
    private Long newOrderId;


    public CheckoutFragment(double price) {
        this.price = price;
        this.originalPrice = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        findViews(view);
        changeCvvEplainVisibility(view);
        initViews();
    }

    private void initViews() {
        initUserDataBase();
        itemsAdapterView = new ItemsAdapterView(getContext(), GlobalResources.items, "", -1);
        onCreditCard();
        initMonths();
        initYear();
        onMonthClick();
        onYearClick();
        onCvvExplain();
        onPayBtn();
        setPointsQuestion();
        setTotalPrice();
        onPointsBtn();
        onCancelPoints();
        onCheckoutBackButton();
    }

    private void initUserDataBase() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
    }

    private void onCvvExplain() {
        cvv_explain_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvv_explanation.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setPointsQuestion() {
        double virtualCurrencies = GlobalResources.user.getVirtualCurrencies();
        String formattedValue = String.format("%.2f", virtualCurrencies);
        if (GlobalResources.user.getVirtualCurrencies() > 0.0) {
            points_question.setText("יש ברשותך " + formattedValue + " נקודות. " + "האם תרצה לממש אותן?");
        } else {
            points_question.setText("יש ברשותך 0 נקודות");
            btnPoints.setVisibility(View.INVISIBLE);
        }
    }

    private void setTotalPrice() {
        String currentPrice = String.format("%.2f", price);
        totalPriceCheckout.setText("₪ " + currentPrice);
    }

    private void onPointsBtn() {
        btnPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsedPoint = true;
                btnCancelPoints.setVisibility(View.VISIBLE);
                if (price < GlobalResources.user.getVirtualCurrencies()) {
                    virtualCurrencies = GlobalResources.user.getVirtualCurrencies() - price;
                    price = 0.0;
                    String formattedValue = String.format("%.2f", virtualCurrencies);
                    totalPriceCheckout.setText("₪ " + 0.0);
                    points_question.setText("יש ברשותך " + formattedValue + " נקודות. " + "האם תרצה לממש אותן?");
                    btnPoints.setVisibility(View.VISIBLE);
                } else {
                    virtualCurrencies = 0.0;
                    price = price - GlobalResources.user.getVirtualCurrencies();
                    String currentPrice = String.format("%.2f", price);
                    totalPriceCheckout.setText("₪ " + currentPrice);
                    points_question.setText("יש ברשותך 0 נקודות");
                }
            }
        });
    }

    private void onCancelPoints() {
        btnCancelPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsedPoint = false;
                price = originalPrice;
                String currentPrice = String.format("%.2f", price);
                totalPriceCheckout.setText("₪ " + currentPrice);
                double virtualCurrencies = GlobalResources.user.getVirtualCurrencies();
                String formattedValue = String.format("%.2f", virtualCurrencies);
                points_question.setText("יש ברשותך " + formattedValue + " נקודות. " + "האם תרצה לממש אותן?");
            }
        });


    }

    private void changeCvvEplainVisibility(View view) {
        onScreen(view);
        onElement(view);
        setChanges();
    }

    private void onCheckoutBackButton() {
        checkoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void setChanges() {
        etCardNumber.setOnFocusChangeListener(focusChangeListener);
        etID.setOnFocusChangeListener(focusChangeListener);
        month.setOnFocusChangeListener(focusChangeListener);
        year.setOnFocusChangeListener(focusChangeListener);
    }

    private void onElement(View view) {
        focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && cvv_explanation.getVisibility() == View.VISIBLE) {
                    cvv_explanation.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    private void onScreen(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cvv_explanation.getVisibility() == View.VISIBLE) {
                    cvv_explanation.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    private void onYearClick() {
        year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenYear = yearsArray[i];
                cvv_explain_button.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void onMonthClick() {
        month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenMonth = monthsArray[i];
                cvv_explain_button.setVisibility(View.INVISIBLE);
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
                cvv_explain_button.setVisibility(View.INVISIBLE);
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
        initSpinnerAdapter(year, yearsArray);
    }

    private void initMonths() {
        monthsArray = new String[12];
        for (int i = 1; i <= 12; i++) {
            monthsArray[i - 1] = String.valueOf(i);
        }

        initSpinnerAdapter(month, monthsArray);
    }

    private void initSpinnerAdapter(AutoCompleteTextView autoCompleteTextView, String[] items) {
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
                    if (isUsedPoint) {
                        if (price < GlobalResources.user.getVirtualCurrencies()) {
                            GlobalResources.user.setVirtualCurrencies(virtualCurrencies);
                        } else
                            GlobalResources.user.setVirtualCurrencies(0.0);
                    }
                    addHistoryToFirebase(view);
                    saveVirtualCurrencies();
                    if (isSaveInfoPayment) {
                        addPaymentToFirebase(view);
                    }
                    addPointsToFirebase();
                    GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new AcceptedPayment());
                }
            }
        });
    }

    private void sendOrderEmail(String orderDetails) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", Constants.GMAIL_HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.SENDER_EMAIL, Constants.SENDER_PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Constants.RECIVER_EMAIL));
            message.setSubject("הזמנה מספר - " + newOrderId);
            message.setText(orderDetails);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveVirtualCurrencies() {
        double virtualCurrencies = price * 0.1;
        GlobalResources.user.addVirtualCurrencies(virtualCurrencies);
    }

    private void addHistoryToFirebase(View view) {
        Cart currentCart = duplicateCartItems();
        Order order = new Order(GlobalResources.cart, currentCart, price);
        setLastOrderId(order);
        GlobalResources.user.addHistory(order);
        MainActivity.isPaid = true;
        GlobalResources.cart = new Cart();
        setHistoryOnFirebase(view);
        itemsAdapterView.changeCount();
    }

    private void setHistoryOnFirebase(View view) {
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

    private void setLastOrderId(Order order) {
        DatabaseReference orderLastIdRef = FirebaseDatabase.getInstance().getReference().child("LastOrderId");
        orderLastIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long lastOrderId = snapshot.getValue(Long.class);
                if (lastOrderId == null)
                    lastOrderId = 0L;
                newOrderId = lastOrderId + 1;
                orderLastIdRef.setValue(newOrderId);
                order.setOrderID(newOrderId);
                sendOrderEmail(order.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPointsToFirebase() {
        userReference.child("VirtualPoints").setValue(GlobalResources.user.getVirtualCurrencies()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mainActivity != null) {
                        mainActivity.onCoinsUpdated(GlobalResources.user.getVirtualCurrencies()); // Notify the MainActivity
                    }
                } else {
                    //Log.d("lala", "not added");
                }
            }
        });

    }

    private Cart duplicateCartItems() {
        Cart cart = new Cart();
        for (int i = 0; i < GlobalResources.cart.getItems().size(); i++) {
            Item copiedItem = new Item(GlobalResources.cart.items.get(i));
            cart.items.add(copiedItem);
        }
        return cart;
    }

    public void addPaymentToFirebase(View view) {
        long cardNumber = Long.parseLong(cardNumberText);
        int idNumber = Integer.parseInt(etID.getText().toString());
        int year = Integer.parseInt(chosenYear);
        int month = Integer.parseInt(chosenMonth);
        int cvv = Integer.parseInt(etCVV.getText().toString());
        Payment payment = new Payment(cardNumber, idNumber, year, month, cvv);
        GlobalResources.user.addPayment(payment);

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
        cvv_explanation = view.findViewById(R.id.cvv_explanation);
        cvv_explain_button = view.findViewById(R.id.cvv_explain_button);
        points_question = view.findViewById(R.id.points_question);
        btnPoints = view.findViewById(R.id.btnPoints);
        btnCancelPoints = view.findViewById(R.id.btnCancelPoints);
        totalPriceCheckout = view.findViewById(R.id.totalPriceCheckout);
        checkoutBackButton = view.findViewById(R.id.checkoutBackButton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
}