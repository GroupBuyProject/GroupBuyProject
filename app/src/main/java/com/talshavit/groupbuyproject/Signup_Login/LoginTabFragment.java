package com.talshavit.groupbuyproject.Signup_Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.talshavit.groupbuyproject.models.User;


public class LoginTabFragment extends Fragment {

    private EditText emailLogin, passwordLogin;
    private androidx.appcompat.widget.AppCompatButton loginButton;

    private FirebaseAuth firebaseAuth;

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

                if(email.isEmpty()){
                    emailLogin.setError("You must fill email!");
                } if(password.isEmpty()){
                    passwordLogin.setError("You must fill password!");
                }else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    loadUserData();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    emailLogin.setError("Incorrect email or password");
                                    passwordLogin.setError("Incorrect email or password");
                                }
                            });
                    }
                else{
                    emailLogin.setError("Please enter valid email");
                }
            }
        });
    }
    private void loadUserData() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Log.d("LoginTabFragment", "User data loaded: " + user.getName());
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



    private void findViews(View view) {
        emailLogin = view.findViewById(R.id.emailLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        loginButton = view.findViewById(R.id.loginButton);
    }

    private void openMainActivity(User user) {
        GlobalResources.setUser(user);
        Log.d("lala", GlobalResources.user.getName());
        Intent myIntent = new Intent(getContext(), MainActivity.class);
        startActivity(myIntent);
    }
}