package com.example.hotelbooking.objects;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

public class HotelBookingApp extends Application {
	@Override
	public void onCreate() {
		FirebaseApp.initializeApp(this);
		FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
		firebaseAppCheck.installAppCheckProviderFactory(
				SafetyNetAppCheckProviderFactory.getInstance());
		super.onCreate();
	}
}
