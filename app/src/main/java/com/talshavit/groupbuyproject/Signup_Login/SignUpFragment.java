package com.talshavit.groupbuyproject.Signup_Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.LoadFromMongoDB;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.User;

public class SignUpFragment extends Fragment {

    private EditText nameEditText, emailSignUp, passwordSignup, confirmSignup;
    private androidx.appcompat.widget.AppCompatButton signupButton;
    private TextView privacypolicytXT, termTxt;
    private String name, email, password;
    private FirebaseAuth firebaseAuth;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        findViews(view);
        initViews();
    }

    private void initViews() {
        onSingUp();
        onPrivacyPolicy();
        onTerms();
    }

    private void onTerms() {
        termTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String htmlContent = GlobalResources.loadHtmlFromAsset(getActivity(), "terms_of_conditions.html");
                GlobalResources.dialogFunc(getContext(), htmlContent);
            }
        });
    }

    private void onPrivacyPolicy() {
        privacypolicytXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String htmlContent = GlobalResources.loadHtmlFromAsset(getActivity(), "privacy_policy.html");
                GlobalResources.dialogFunc(getContext(), htmlContent);
            }
        });
    }

    private void onSingUp() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString().trim();
                email = emailSignUp.getText().toString().trim();
                password = passwordSignup.getText().toString().trim();
                String confirmPassword = confirmSignup.getText().toString().trim();
                boolean isValid = true;

                if (name.isEmpty()) {
                    nameEditText.setError(getString(R.string.mustName));
                    isValid = false;
                }
                if (email.isEmpty()) {
                    emailSignUp.setError(getString(R.string.mustEmail));
                    isValid = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailSignUp.setError(getString(R.string.invalidEmail));
                    isValid = false;
                }
                if (password.isEmpty()) {
                    passwordSignup.setError(getString(R.string.mustPassword));
                    isValid = false;
                } else if (password.length() < 6) {
                    passwordSignup.setError(getString(R.string.limitPassword));
                    isValid = false;
                }
                if (confirmPassword.isEmpty()) {
                    confirmSignup.setError(getString(R.string.mustPassword));
                    isValid = false;
                } else if (confirmPassword.length() < 6) {
                    confirmSignup.setError(getString(R.string.limitPassword));
                    isValid = false;
                } else if (!password.equals(confirmPassword)) {
                    confirmSignup.setError(getString(R.string.notMatchPasswords));
                    isValid = false;
                }
                if (isValid) {
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                            User user = new User(name);

                            databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        openMainActivity();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void findViews(View view) {
        nameEditText = view.findViewById(R.id.nameEditText);
        signupButton = view.findViewById(R.id.signupButton);
        emailSignUp = view.findViewById(R.id.emailSignUp);
        passwordSignup = view.findViewById(R.id.passwordSignup);
        confirmSignup = view.findViewById(R.id.confirmSignup);
        privacypolicytXT = view.findViewById(R.id.privacypolicytXT);
        termTxt = view.findViewById(R.id.termTxt);
    }

    private void openMainActivity() {
        Intent myIntent = new Intent(getContext(), LoadFromMongoDB.class);
        startActivity(myIntent);
    }
}
