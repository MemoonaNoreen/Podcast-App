package com.example.driveallnight.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driveallnight.R;
import com.example.driveallnight.databinding.ActivitySigninBinding;
import com.example.driveallnight.databinding.ActivitySignupBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SigninActivity extends AppCompatActivity
{
    private static final String TAG = "SigninActivity";
    private Context context = SigninActivity.this;

    private ActivitySigninBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(context);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.txtSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    binding.emailLay.setError("Please enter your email");
                }
                else if (TextUtils.isEmpty(password))
                {
                    binding.passLay.setError("Please enter your password");
                }
                else
                    {
                        SignInUser(email, password);
                }
            }
        });

        binding.txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = binding.etEmail.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    binding.emailLay.setError("Please enter your email to reset password");
                }
                else
                    {
                        sendPasswordResetEmail(email);
                }
            }
        });
    }

    private void SignInUser(String email, String password)
    {
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> { startMainActivity(); })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    private void sendPasswordResetEmail(String email)
    {
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), "Please Check your Inbox", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
