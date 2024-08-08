package com.talshavit.groupbuyproject.Checkout;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.talshavit.groupbuyproject.Models.Cart;
import com.talshavit.groupbuyproject.Models.Item;
import com.talshavit.groupbuyproject.Models.Order;
import com.talshavit.groupbuyproject.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BasePaymentFragment {

    public static void initUserDataBase(DatabaseReference userReference) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
    }

    public static void setPointsQuestion(TextView points_question, AppCompatButton btnPoints) {
        double virtualCurrencies = GlobalResources.user.getVirtualCurrencies();
        String formattedValue = String.format("%.2f", virtualCurrencies);
        if (GlobalResources.user.getVirtualCurrencies() > 0.0) {
            points_question.setText("יש ברשותך " + formattedValue + " נקודות. " + "האם תרצה לממש אותן?");
        } else {
            points_question.setText("יש ברשותך 0 נקודות");
            btnPoints.setVisibility(View.INVISIBLE);
        }
    }

//    private void onPointsBtn() {
//        btnPoints.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isUsedPoint = true;
//                btnCancelPoints.setVisibility(View.VISIBLE);
//                if (price < GlobalResources.user.getVirtualCurrencies()) {
//                    virtualCurrencies = GlobalResources.user.getVirtualCurrencies() - price;
//                    price = 0.0;
//                    totalPriceCheckout.setText("₪ " + 0.0);
//                    GlobalResources.setPointsQuestionTxt(points_question, virtualCurrencies);
//                    btnPoints.setVisibility(View.VISIBLE);
//                } else {
//                    virtualCurrencies = 0.0;
//                    price = price - GlobalResources.user.getVirtualCurrencies();
//                    String currentPrice = String.format("%.2f", price);
//                    totalPriceCheckout.setText("₪ " + currentPrice);
//                    points_question.setText(R.string.zeroPoints);
//                }
//                btnCancelPoints.setVisibility(View.VISIBLE);
//                btnPoints.setVisibility(View.GONE);
//            }
//        });
//    }

    public static void setTotalPrice(double price, TextView totalPriceCheckout) {
        String currentPrice = String.format("%.2f", price);
        totalPriceCheckout.setText("₪ " + currentPrice);
    }

    public static void updateVirtualCurrencies(double price, double virtualCurrencies) {
        if (price < GlobalResources.user.getVirtualCurrencies()) {
            GlobalResources.user.setVirtualCurrencies(virtualCurrencies);
        } else {
            GlobalResources.user.setVirtualCurrencies(0.0);
        }
    }

//    public static void addHistoryToFirebase(View view, ItemsAdapterView itemsAdapterView, DatabaseReference userReference, Long newOrderId) {
//        Cart currentCart = duplicateCartItems();
//        Order order = new Order(GlobalResources.cart, currentCart, price);
//        setLastOrderId(order, newOrderId);
//        GlobalResources.user.addHistory(order);
//        MainActivity.isPaid = true;
//        GlobalResources.cart = new Cart();
//        setHistoryOnFirebase(view, userReference);
//        itemsAdapterView.changeCount();
//    }

//    public static void setLastOrderId(Order order, Long newOrderId) {
//        DatabaseReference orderLastIdRef = FirebaseDatabase.getInstance().getReference().child("LastOrderId");
//        orderLastIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Long lastOrderId = snapshot.getValue(Long.class);
//                if (lastOrderId == null)
//                    lastOrderId = 0L;
//                newOrderId = lastOrderId + 1;
//                orderLastIdRef.setValue(newOrderId);
//                order.setOrderID(newOrderId);
//                sendOrderEmail(order.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public static void setHistoryOnFirebase(View view, DatabaseReference userReference) {
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

    public static Cart duplicateCartItems() {
        Cart cart = new Cart();
        for (int i = 0; i < GlobalResources.cart.getItems().size(); i++) {
            Item copiedItem = new Item(GlobalResources.cart.items.get(i));
            cart.items.add(copiedItem);
        }
        return cart;
    }

//    private void sendOrderEmail(String orderDetails) {
//        Properties properties = System.getProperties();
//        properties.put("mail.smtp.host", Constants.GMAIL_HOST);
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(Constants.SENDER_EMAIL, Constants.SENDER_PASSWORD);
//            }
//        });
//
//        MimeMessage message = new MimeMessage(session);
//        try {
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Constants.RECIVER_EMAIL));
//            message.setSubject("הזמנה מספר - " + newOrderId);
//            message.setText(orderDetails);
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(message);
//                    } catch (MessagingException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
//            thread.start();
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public void addPointsToFirebase() {
//        userReference.child("VirtualPoints").setValue(GlobalResources.user.getVirtualCurrencies()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    if (mainActivity != null) {
//                        mainActivity.onCoinsUpdated(GlobalResources.user.getVirtualCurrencies());
//                    }
//                }
//            }
//        });
//
//    }
}
