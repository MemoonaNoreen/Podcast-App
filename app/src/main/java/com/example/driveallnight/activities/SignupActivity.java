package com.example.driveallnight.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driveallnight.R;
import com.example.driveallnight.databinding.ActivitySigninBinding;
import com.example.driveallnight.databinding.ActivitySignupBinding;
import com.example.driveallnight.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

public class SignupActivity extends AppCompatActivity
{
    private static final String TAG = "SignupActivity";
    private Context context = SignupActivity.this;
    private ActivitySignupBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference userReference;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userReference = FirebaseDatabase.getInstance().getReference("Users");
        progressDialog = new ProgressDialog(context);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.txtSignnin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name =  binding.etName.getText().toString();
                String email = binding.etEmail.getText().toString();
                String phone = binding.etPhone.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    binding.nameLay.setError("Please enter your full name");
                }
                else if (TextUtils.isEmpty(email))
                {
                    binding.emailLay.setError("Please enter your email");
                }
                else if (TextUtils.isEmpty(phone))
                {
                    binding.phoneLay.setError("Please enter your contact number");
                }
                else if (TextUtils.isEmpty(password))
                {
                    binding.passLay.setError("Please enter your password");
                }
                else {
                    users = new Users(email, name, phone, System.currentTimeMillis());
                    signUpUser(email, password);
                }
            }
        });
    }

    private void signUpUser(String email, String password)
    {
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                FirebaseUser firebaseUser = authResult.getUser();
                storeUserInDatabase(firebaseUser, password);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e)
                    {
                        progressDialog.dismiss();
                        Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeUserInDatabase(FirebaseUser firebaseUser, String password)
    {
        userReference.child(firebaseUser.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused)
            {
                progressDialog.dismiss();
                Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
                }).addOnFailureListener(e -> {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), password);

                    firebaseUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    Toast.makeText(context, "Error on creating User, please try again later", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                });
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
