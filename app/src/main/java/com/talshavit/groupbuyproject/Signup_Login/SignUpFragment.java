package com.talshavit.groupbuyproject.Signup_Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.User;

import java.io.IOException;
import java.io.InputStream;

public class SignUpFragment extends Fragment {

    private EditText nameEditText, emailSignUp, passwordSignup, confirmSignup;
    private androidx.appcompat.widget.AppCompatButton signupButton;

    private TextView privacypolicytXT, termTxt;

    private String name, email, password;
    private FirebaseAuth firebaseAuth;

    // private FirebaseAuth firebaseAuth;


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
        // Inflate the layout for this fragment
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
                String htmlContent = loadHtmlFromAsset("terms_of_conditions.html");
                dialogFunc(htmlContent);
            }
        });
    }

    private void onPrivacyPolicy() {
        privacypolicytXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String htmlContent = loadHtmlFromAsset("privacy_policy.html");
                dialogFunc(htmlContent);

            }
        });
    }

    private void dialogFunc(String htmlContent) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent));
        }
        materialAlertDialogBuilder.setPositiveButton("סגור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //Close the dialog
            }
        });

        materialAlertDialogBuilder.setCancelable(false);
        materialAlertDialogBuilder.show();
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
                    nameEditText.setError("חובה למלא שם!");
                    isValid = false;
                }
                if (email.isEmpty()) {
                    emailSignUp.setError("חובה למלא מייל!");
                    isValid = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailSignUp.setError("כתובת המייל לא בפורמט תקין!");
                    isValid = false;
                }
                if (password.isEmpty()) {
                    passwordSignup.setError("חובה למלא סיסמא!");
                    isValid = false;
                } else if (password.length() < 6) {
                    passwordSignup.setError("סיסמא חייבת להכיל לפחות 6 תווים!");
                    isValid = false;
                }
                if (confirmPassword.isEmpty()) {
                    confirmSignup.setError("יש למלא אימות סיסמא!");
                    isValid = false;
                } else if (confirmPassword.length() < 6) {
                    confirmSignup.setError("סיסמא חייבת להכיל לפחות 6 תווים!");
                    isValid = false;
                } else if (!password.equals(confirmPassword)) {
                    confirmSignup.setError("סיסמאות לא זהות!");
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
        Intent myIntent = new Intent(getContext(), MainActivity.class);
        startActivity(myIntent);
    }

    private String loadHtmlFromAsset(String filename) {
        String textFile = "";
        try {
            InputStream inputStream = requireContext().getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            textFile = new String(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFile;
    }
}
