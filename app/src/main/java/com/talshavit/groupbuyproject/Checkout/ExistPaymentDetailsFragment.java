package com.talshavit.groupbuyproject.Checkout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talshavit.groupbuyproject.Fragments.AcceptedPayment;
import com.talshavit.groupbuyproject.General.Constants;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.CreditCardAdapter;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnCardDeletedListener;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.Models.Cart;
import com.talshavit.groupbuyproject.Models.Item;
import com.talshavit.groupbuyproject.Models.Order;
import com.talshavit.groupbuyproject.Models.Payment;
import com.talshavit.groupbuyproject.R;

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

public class ExistPaymentDetailsFragment extends Fragment {
    private TextView chooseCardTXT, selectedCardText, points_question, totalPriceCheckout;
    private RelativeLayout selectCardLayout;
    private ImageView selectedCardIcon, selectedCardCheckIcon, selected_card_check_icon;
    private Payment selectedCard;
    private String userID;
    private AppCompatButton btnCancelPoints, btnPoints, btnPay;
    private double price, virtualCurrencies, originalPrice;
    private ItemsAdapterView itemsAdapterView;
    private boolean isUsedPoint = false;
    private DatabaseReference userReference;
    private MainActivity mainActivity;
    private Long newOrderId;

    public ExistPaymentDetailsFragment(double price) {
        this.price = price;
        this.originalPrice = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exist_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        findViews(view);
        initViews();
    }

    private void initViews() {
        initUserDataBase();
        itemsAdapterView = new ItemsAdapterView(getContext(), GlobalResources.items, "", -1);
        setPointsQuestion();
        setTotalPrice();
        onPointsBtn();
        onCancelPoints();
        onSelectCardLayout();
        initSelectCardPosition();
        initSelectCardLayout();
        if (GlobalResources.user.getPayments().size() > 0) {
            updateSelectedCardUI(GlobalResources.user.getPayments().get(0));
            onPayBtn();
        }
    }

    private void openCardSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_card, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewCards);

        List<Payment> cards = GlobalResources.user.getPayments();
        CreditCardAdapter adapter = new CreditCardAdapter(cards, new CreditCardAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(Payment card) {
                selectedCard = card;
                GlobalResources.selectedCardPosition = cards.indexOf(card);
            }
        }, GlobalResources.selectedCardPosition, userReference, new OnCardDeletedListener() {
            @Override
            public void onCardDeleted() {
                updateSelectedCardAfterDeletion();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        builder.setView(dialogView)
                .setTitle(R.string.chooseCard)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    if (selectedCard != null) {
                        updateSelectedCardUI(selectedCard);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void updateSelectedCardAfterDeletion() {
        List<Payment> cards = GlobalResources.user.getPayments();
        if (cards.isEmpty()) {
            //chooseCardTXT.setVisibility(View.INVISIBLE);
            selectCardLayout.setVisibility(View.INVISIBLE);
            //selectedCardText.setText("");
            //selectedCardIcon.setImageResource(0);
            //selectedCardCheckIcon.setVisibility(View.GONE);
        } else {
            updateSelectedCardUI(cards.get(0));
        }
        initSelectCardLayout();
    }

    private void updateSelectedCardUI(Payment selectedCard) {
        selectedCardText.setText("כרטיס **** " + String.valueOf(selectedCard.getCardNumber()).substring(12));
        selectedCardIcon.setImageResource(R.drawable.credit);
        if (GlobalResources.selectedCardPosition != -1)
            selectedCardCheckIcon.setVisibility(View.VISIBLE);
    }

    private void initSelectCardLayout() {
        if (GlobalResources.user.getPayments().size() > 0) {
            chooseCardTXT.setVisibility(View.VISIBLE);
            selectCardLayout.setVisibility(View.VISIBLE);
        } else {
            chooseCardTXT.setVisibility(View.INVISIBLE);
            selectCardLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void initSelectCardPosition() {
        if (GlobalResources.selectedCardPosition != -1) {
            selectedCard = GlobalResources.user.getPayments().get(GlobalResources.selectedCardPosition);
            updateSelectedCardUI(selectedCard);
        }
    }

    private void onSelectCardLayout() {
        selectCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCardSelectionDialog();
            }
        });
    }

    private void initUserDataBase() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
    }

    private void setPointsQuestion() {
        double virtualCurrencies = GlobalResources.user.getVirtualCurrencies();
        if (GlobalResources.user.getVirtualCurrencies() > 0.0) {
            GlobalResources.setPointsQuestionTxt(points_question, virtualCurrencies);
        } else {
            points_question.setText(R.string.zeroPoints);
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
                    totalPriceCheckout.setText("₪ " + 0.0);
                    GlobalResources.setPointsQuestionTxt(points_question, virtualCurrencies);
                    btnPoints.setVisibility(View.VISIBLE);
                } else {
                    virtualCurrencies = 0.0;
                    price = price - GlobalResources.user.getVirtualCurrencies();
                    String currentPrice = String.format("%.2f", price);
                    totalPriceCheckout.setText("₪ " + currentPrice);
                    points_question.setText(R.string.zeroPoints);
                }
                btnCancelPoints.setVisibility(View.VISIBLE);
                btnPoints.setVisibility(View.GONE);
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
                GlobalResources.setPointsQuestionTxt(points_question, virtualCurrencies);
                btnPoints.setVisibility(View.VISIBLE);
                btnCancelPoints.setVisibility(View.GONE);
            }
        });
    }

    private void onPayBtn() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsedPoint) {
                    updateVirtualCurrencies();
                }
                addHistoryToFirebase(view);
                saveVirtualCurrencies();
                addPointsToFirebase();
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new AcceptedPayment());
            }
        });
    }

    private void updateVirtualCurrencies() {
        if (price < GlobalResources.user.getVirtualCurrencies()) {
            GlobalResources.user.setVirtualCurrencies(virtualCurrencies);
        } else {
            GlobalResources.user.setVirtualCurrencies(0.0);
        }
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
        double virtualCurrencies = price * 0.05;
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
                        mainActivity.onCoinsUpdated(GlobalResources.user.getVirtualCurrencies());
                    }
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

    private void findViews(View view) {
        btnPay = view.findViewById(R.id.btnPay);
        points_question = view.findViewById(R.id.points_question);
        btnPoints = view.findViewById(R.id.btnPoints);
        btnCancelPoints = view.findViewById(R.id.btnCancelPoints);
        totalPriceCheckout = view.findViewById(R.id.totalPriceCheckout);
        selectCardLayout = view.findViewById(R.id.select_card_layout);
        selectCardLayout = view.findViewById(R.id.select_card_layout);
        selectedCardText = view.findViewById(R.id.selected_card_text);
        selectedCardIcon = view.findViewById(R.id.selected_card_icon);
        chooseCardTXT = view.findViewById(R.id.chooseCardTXT);
        selectedCardCheckIcon = view.findViewById(R.id.selected_card_check_icon);
    }
}