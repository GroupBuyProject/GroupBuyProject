package com.talshavit.groupbuyproject.Signup_Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.talshavit.groupbuyproject.models.Order;
import com.talshavit.groupbuyproject.models.User;

import java.util.ArrayList;


public class LoginTabFragment extends Fragment {

    private EditText emailLogin, passwordLogin;
    private androidx.appcompat.widget.AppCompatButton loginButton;

    private FirebaseAuth firebaseAuth;
    private String userID;
    private DatabaseReference databaseReference;

    public LoginTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        findViews(view);
        initViews();

    }

    private void initViews() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if (email.isEmpty()) {
                    emailLogin.setError("חובה למלא מייל!");
                }
                if (password.isEmpty()) {
                    passwordLogin.setError("חובה למלא סיסמא!");
                } else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    loadUserData();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    emailLogin.setError("מייל או סיסמא שגויים!");
                                    passwordLogin.setError("מייל או סיסמא שגויים!");
                                }
                            });
                } else {
                    emailLogin.setError("Please enter valid email");
                }
            }
        });
    }

    private void loadUserData() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Log.d("LoginTabFragment", "User data loaded: " + user.getName());
                    loadHistoriesFromDatabase();
                    openMainActivity(user);
                } else {
                    Log.d("LoginTabFragment", "User data is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LoginTabFragment", "Failed to load user data: " + databaseError.getMessage());
            }
        });
    }

    private void loadHistoriesFromDatabase() {
        databaseReference.child("HistoriesList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Order order = itemSnapshot.getValue(Order.class);
                    if (order != null) {
                        GlobalResources.user.addHistory(order);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private void findViews(View view) {
        emailLogin = view.findViewById(R.id.emailLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        loginButton = view.findViewById(R.id.loginButton);
    }

    private void openMainActivity(User user) {
        GlobalResources.setUser(user);
        Intent myIntent = new Intent(getContext(), MainActivity.class);
        startActivity(myIntent);
    }
}