package com.example.hotelbooking.functions;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

public class FDB {

	public static final int STATUS_JUST_ORDERED = 0;
	public static final int STATUS_EXPIRED = 1;
	public static final int STATUS_REQ_CANCELLATION = 2;
	public static final int STATUS_STAYING_IN = 3;

	public static FirebaseFirestore getFDB() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
				.setPersistenceEnabled(true)
				.setCacheSizeBytes(250 * 1024 * 1024) // 250 MB
				.build();
		db.setFirestoreSettings(settings);
		return db;
	}

	public static FirebaseStorage getFDS() {
		return FirebaseStorage.getInstance();
	}

	public static String status(int status) {
		switch (status) {
			case STATUS_JUST_ORDERED:
				return "Booked";
			case STATUS_REQ_CANCELLATION:
				return "Cancellation Requested";
			case STATUS_EXPIRED:
				return "Expired";
			case STATUS_STAYING_IN:
				return "Staying in. Thanks for checking in!";
			default:
				return "Unknown";
		}
	}
}
