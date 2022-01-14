package com.example.hotelbooking.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class LocalDatabase {
	private final SharedPreferences preferences;
	private static LocalDatabase instance;

	private static final String USER_NAME = "username";
	private static final String PHONE_NUMBER = "phone";
	private static final String UPLOAD_USER = "upload";
	private static final String PROFILE_PIC = "pfp";

	private LocalDatabase(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static LocalDatabase getInstance(Context mContext) {
		return instance != null && instance.preferences != null ? instance : (instance = new LocalDatabase(mContext));
	}

	@Nullable
	public String getUsername() {
		return preferences.getString(USER_NAME, null);
	}

	public LocalDatabase setUsername(String username) {
		preferences.edit().putString(USER_NAME, username).apply();
		return this;
	}

	@Nullable
	public String getPhoneNumber() {
		return preferences.getString(PHONE_NUMBER, null);
	}

	public void setPhone(String phone) {
		preferences.edit().putString(PHONE_NUMBER, phone).apply();
	}

	public boolean isUploaded() {
		return preferences.getBoolean(UPLOAD_USER, false);
	}

	public void uploaded() {
		preferences.edit().putBoolean(UPLOAD_USER, true).apply();
	}

	public String getProfilePic() {
		return preferences.getString(PROFILE_PIC, null);
	}

	public void setProfilePic(String baseString) {
		preferences.edit().putString(PROFILE_PIC, baseString).apply();
	}

	public void clear() {
		preferences.edit().clear().apply();
	}

}
