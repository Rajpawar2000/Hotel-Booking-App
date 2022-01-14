package com.example.hotelbooking.ui_fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.hotelbooking.R;
import com.example.hotelbooking.databinding.FragmentProfileBinding;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.functions.LocalDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.SetOptions;
import com.mukesh.OtpView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {

	private FragmentProfileBinding binding;
	BottomSheetDialog modal;
	int i = 10;
	private Local_Caller caller;
	AlertDialog alertDialog, alertDialog_later;
	private String new_num;

	@SuppressLint("SetTextI18n")
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentProfileBinding.inflate(inflater, container, false);

		binding.profileImg.setOnClickListener(v -> updateProfile());
		loadImg();

		binding.profileName.setText(LocalDatabase.getInstance(getContext()).getUsername());
		binding.phoneDisplay.setText("Phone : +91" + LocalDatabase.getInstance(getContext()).getPhoneNumber());

		binding.profileName.setOnClickListener(v -> {
			TextInputEditText editText = new TextInputEditText(requireActivity());
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			alertDialog = new AlertDialog.Builder(requireActivity())
					.setTitle("Change Your Name")
					.setView(editText)
					.setPositiveButton("Ok", (dialog, which) -> {
						Map<String, String> map = new HashMap<>();
						map.put("USER_NAME", Objects.requireNonNull(editText.getText()).toString());
						FDB.getFDB()
								.collection("users")
								.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
								.set(map, SetOptions.merge()).addOnSuccessListener(unused -> Toast.makeText(requireActivity(),
								"Success!", Toast.LENGTH_SHORT).show());
						LocalDatabase.getInstance(getContext()).setUsername(editText.getText().toString());
						binding.profileName.setText(editText.getText().toString());
					})
					.create();

			if (!requireActivity().isFinishing())
				alertDialog.show();
		});

		binding.phoneDisplay.setOnClickListener(v -> {
			TextInputEditText editText = new TextInputEditText(requireActivity());
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			alertDialog = new AlertDialog.Builder(requireActivity())
					.setTitle("Change Your Phone")
					.setView(editText)
					.setPositiveButton("Ok", (dialog, which) -> {
						new_num = editText.getText().toString();
						startLogin();
					})
					.create();

			if (!requireActivity().isFinishing())
				alertDialog.show();
		});

		return binding.getRoot();
	}

	private void loadImg() {
		if (LocalDatabase.getInstance(getContext()).getProfilePic() == null) {
			FDB.getFDB()
					.collection("users")
					.document(FirebaseAuth.getInstance().getUid())
					.get()
					.addOnSuccessListener(documentSnapshot -> {
						if (documentSnapshot.get("USER_IMG") != null) {
							String data = String.valueOf(documentSnapshot.get("USER_IMG"));
							byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
							Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
							binding.profileImg.setImageBitmap(decodedByte);

							LocalDatabase.getInstance(getContext()).setProfilePic(data);
						} else {
							Toast.makeText(getContext(), "You can upload your own image by clicking the camera button", Toast.LENGTH_SHORT).show();
						}
					});
		} else {
			loadPfpInternal(LocalDatabase.getInstance(getContext()).getProfilePic());
		}
	}

	private void loadPfpInternal(String profilePic) {
		byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		binding.profileImg.setImageBitmap(decodedByte);
	}

	private void updateProfile() {
		modal = new BottomSheetDialog(requireActivity());
		View any = LayoutInflater.from(requireActivity()).inflate(R.layout.btm_sheet, null);

		TextView open_cam = any.findViewById(R.id.open_cam);
		TextView open_gal = any.findViewById(R.id.open_gal);

		open_cam.setOnClickListener(v -> openCam());
		open_gal.setOnClickListener(v -> openGal());

		modal.setContentView(any);
		modal.setTitle("select");
		modal.setCancelable(false);
		modal.show();
	}

	private void openGal() {
		Intent libIntent = new Intent(Intent.ACTION_GET_CONTENT);
		libIntent.setType("*/*"); // type of content to access  folder/files
		Intent pickIntent = Intent.createChooser(libIntent, "Pick an image"); // pick one of it
		startActivityForResult(pickIntent, 1002);  // after picking an image return back to src with a value
		modal.dismiss();
	}

	private void openCam() {
		Intent action = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
		startActivityForResult(action, 456);
		modal.dismiss();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
		if (requestCode == 456 && resultCode == Activity.RESULT_OK) {
			//cam
			Bitmap image = (Bitmap) data.getExtras().get("data");
			binding.profileImg.setImageBitmap(image);

		} else if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
			//gal
			Uri imageUri = data.getData();
			binding.profileImg.setImageURI(imageUri);
		}
		super.onActivityResult(requestCode, resultCode, data);

		startUpload();
	}

	private void startUpload() {
		new Handler().postDelayed(() -> {

			binding.indicator.setVisibility(View.VISIBLE);
			updateIndicator();

			Drawable drawable = binding.profileImg.getDrawable();

			if (drawable == null) {
				Toast.makeText(getContext(), "Can't Upload Profile Image To Sever", Toast.LENGTH_SHORT).show();
				return;
			}

			Bitmap mutableBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mutableBitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);

			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
			byte[] byteArray = byteStream.toByteArray();
			String baseString = Base64.encodeToString(byteArray, Base64.DEFAULT);

			LocalDatabase.getInstance(getContext()).setProfilePic(baseString);

			Map<String, String> all = new HashMap<>();
			all.put("USER_IMG", baseString);
			Toast.makeText(getContext(), "Updating Profile Picture..Please Wait", Toast.LENGTH_SHORT).show();
			FDB.getFDB()
					.collection("users")
					.document(FirebaseAuth.getInstance().getUid())
					.set(all, SetOptions.merge())
					.addOnSuccessListener(unused -> {
						Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
						binding.indicator.setVisibility(View.INVISIBLE);
					})
					.addOnFailureListener(e -> Toast.makeText(getContext(), "Err, Can't Update Profile!", Toast.LENGTH_SHORT).show());
		}, 250);
	}

	private void updateIndicator() {
		Handler handler = new Handler();

		handler.post(new Runnable() {
			@Override
			public void run() {
				if (binding != null && binding.indicator.getVisibility() == View.VISIBLE) {
					binding.indicator.setProgress(i > 90 ? (i = 0) : (i += 10));
					handler.postDelayed(this, 100);
				}
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	public void startLogin() {
		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		String phone_number_text = Objects.requireNonNull(new_num);
		if (!phone_number_text.contains("+91"))
			phone_number_text = "+91" + phone_number_text;

		PhoneAuthOptions options =
				PhoneAuthOptions.newBuilder(mAuth)
						.setPhoneNumber(phone_number_text)
						.setTimeout(60L, TimeUnit.SECONDS)
						.setActivity(requireActivity())
						.setCallbacks(mCallbacks(mAuth))
						.build();
		PhoneAuthProvider.verifyPhoneNumber(options);
		Toast.makeText(getContext(), "Please wait for OTP to be sent!", Toast.LENGTH_LONG).show();
	}

	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks(FirebaseAuth auth) {
		return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

			@Override
			public void onCodeSent(@NonNull String signature, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
				Toast.makeText(requireActivity(), "Verification Code Sent", Toast.LENGTH_SHORT).show();
				showOTPMenu(signature, auth);
				super.onCodeSent(signature, forceResendingToken);
			}

			@Override
			public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
				caller.timeout();
				super.onCodeAutoRetrievalTimeOut(s);
			}

			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				tryToVerify(auth, phoneAuthCredential);
			}

			@Override
			public void onVerificationFailed(@NonNull FirebaseException e) {
				e.printStackTrace();
				Toast.makeText(requireActivity(), "Can't verify, please try again", Toast.LENGTH_SHORT).show();
			}
		};
	}

	private void showOTPMenu(String signature, FirebaseAuth auth) {
		View progressView = getLayoutInflater().inflate(R.layout.otp_dialogue, null);

		MaterialButton enter_otp = progressView.findViewById(R.id.enter_otp);
		MaterialButton otp_cancel = progressView.findViewById(R.id.otp_cancel);

		enter_otp.setOnClickListener(v -> caller.timeout());
		otp_cancel.setOnClickListener(v -> {
			if (alertDialog != null && alertDialog.isShowing())
				alertDialog.dismiss();
		});

		alertDialog = new AlertDialog.Builder(requireActivity())
				.setView(progressView)
				.setCancelable(false)
				.create();

		caller = () -> {
			alertDialog.dismiss();

			View progressView_later = getLayoutInflater().inflate(R.layout.otp_dialogue_2nd, null);
			alertDialog_later = new AlertDialog.Builder(requireActivity())
					.setView(progressView_later)
					.setCancelable(false)
					.create();

			OtpView otp_view = progressView_later.findViewById(R.id.otp_view);

			otp_view.setOtpCompletionListener(otp -> tryToVerify(signature, auth, otp));

			alertDialog_later.show();
		};

		alertDialog.show();
	}

	private void tryToVerify(FirebaseAuth mAuth, PhoneAuthCredential credential) {
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(requireActivity(), task -> {
					if (task.isSuccessful()) {
						storePhone();
						Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
								.updatePhoneNumber(credential)
								.addOnSuccessListener(unused -> {
									Toast.makeText(getActivity(), "Update Phone", Toast.LENGTH_SHORT).show();
									binding.phoneDisplay.setText(new_num);
								})
								.addOnFailureListener(e -> Toast.makeText(getActivity(), "Cant Update Phone", Toast.LENGTH_SHORT).show());
					} else {
						Toast.makeText(requireActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void tryToVerify(String signature, FirebaseAuth auth, String otp) {
		PhoneAuthCredential credential = PhoneAuthProvider.getCredential(signature, otp);
		auth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
			storePhone();
			Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
					.updatePhoneNumber(credential)
					.addOnSuccessListener(unused -> {
						Toast.makeText(getActivity(), "Update Phone", Toast.LENGTH_SHORT).show();
						binding.phoneDisplay.setText(new_num);
					})
					.addOnFailureListener(e -> Toast.makeText(getActivity(), "Cant Update Phone", Toast.LENGTH_SHORT).show());
		});
	}

	private interface Local_Caller {
		void timeout();
	}

	private void storePhone() {
		LocalDatabase.getInstance(getContext()).setPhone(Objects.requireNonNull(new_num));
	}

}