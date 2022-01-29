package com.example.driveallnight.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driveallnight.MyPreferences;

public class SplashScreenActivity extends AppCompatActivity
{
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences(MyPreferences.MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(MyPreferences.LOGIN, false))
        {
            startActivity(new Intent(SplashScreenActivity.this, SigninActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }

    }
}
