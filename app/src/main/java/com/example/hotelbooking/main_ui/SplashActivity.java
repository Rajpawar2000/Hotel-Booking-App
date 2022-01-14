package com.example.hotelbooking.main_ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbooking.R;
import com.example.hotelbooking.functions.LocalDatabase;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_BookHotel_NoActionBar);
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, LocalDatabase.getInstance(this).getPhoneNumber() != null ?
				MainActivity.class : LoginActivity.class));
		finish();
	}
}