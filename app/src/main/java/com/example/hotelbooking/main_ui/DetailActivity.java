package com.example.hotelbooking.main_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.R;
import com.example.hotelbooking.databinding.LayoutBookHotelBinding;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.objects.ViewAllRoomsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.SetOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {
    ImageView hotelImage;
    TextView hotelName, hotelCity, hotelAddress, hotelRoomDetails, hotelDiningFacilities;
    TextView hotelHotelFacilities, hotelRoomFacilities, hotelRoomAdult, hotelRoomChild;
    TextView hotelPhone, hotelRoomPrice, hotelRoomRating;
    TextView hotelParking, hotelWifi;
    Button showDesc,bookHotel;
    LinearLayoutCompat hotelShowDesc;

    ViewAllRoomsModel viewAllRoomsModel;
    AlertDialog bookingDialogue;
    ArrayList<ViewAllRoomsModel> localList;
    List<ViewAllRoomsModel> hawaii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        hotelImage = findViewById(R.id.hotel_room_image);
        bookHotel = findViewById(R.id.book_hotel);
        hotelName = findViewById(R.id.hotel_name);
        hotelCity = findViewById(R.id.hotel_city);
        hotelAddress = findViewById(R.id.hotel_address);
        hotelRoomDetails = findViewById(R.id.hotel_room_details);
        hotelDiningFacilities = findViewById(R.id.hotel_dining_facilities);
        hotelHotelFacilities = findViewById(R.id.hotel_hotel_facilities);
        hotelRoomFacilities = findViewById(R.id.hotel_room_facilities);
        hotelRoomAdult = findViewById(R.id.hotel_room_adult);
        hotelRoomChild = findViewById(R.id.hotel_room_child);
        hotelPhone = findViewById(R.id.hotel_phone);
        hotelRoomPrice = findViewById(R.id.hotel_room_price);
        hotelRoomRating = findViewById(R.id.hotel_room_rating);
        hotelParking = findViewById(R.id.hotel_parking);
        hotelWifi = findViewById(R.id.hotel_wifi);
        showDesc = findViewById(R.id.show_desc);
        hotelShowDesc = findViewById(R.id.hotel_show_desc);

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllRoomsModel){
            viewAllRoomsModel = (ViewAllRoomsModel)object;
        }

        if (viewAllRoomsModel != null){

            Glide.with(getApplicationContext()).load(viewAllRoomsModel.getImages_url()).into(hotelImage);
            hotelName.setText(viewAllRoomsModel.getName());
            hotelAddress.setText(viewAllRoomsModel.getAddress());
            hotelRoomDetails.setText((int) viewAllRoomsModel.getTotal_available() + " out of " + (int) viewAllRoomsModel.getTotal_room() + " available.");
            hotelDiningFacilities.setText(viewAllRoomsModel.getD_facilities());
            hotelHotelFacilities.setText(viewAllRoomsModel.getH_facilities());
            hotelRoomFacilities.setText(viewAllRoomsModel.getR_facilities());
            hotelRoomAdult.setText((int) viewAllRoomsModel.getPeople_adult() + " Adult Beds");
            hotelRoomChild.setText((int) viewAllRoomsModel.getPeople_child() + " Child Beds");
            hotelPhone.setText(viewAllRoomsModel.getPhone());
            hotelRoomPrice.setText("INR " + viewAllRoomsModel.getPrice());
            hotelRoomRating.setText(String.valueOf(viewAllRoomsModel.getRating()));

            if (viewAllRoomsModel.getTotal_available() == 0) {
                hotelRoomDetails.setText("No rooms are currently available for booking!");
                hotelRoomDetails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_room
                        , 0, R.drawable.ic_no, 0);
                bookHotel.setEnabled(false);
            }

            hotelParking.setText(viewAllRoomsModel.isParking() ?
                    "Parking Available 24X7" : "Parking Not Available");
            hotelParking.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_parking, 0,
                    viewAllRoomsModel.isWifi() ? R.drawable.ic_yes : R.drawable.ic_no, 0);

            hotelWifi.setText(viewAllRoomsModel.isParking() ?
                    "WiFi Available 24X7" : "WiFi Not Available");
            hotelWifi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wifi, 0,
                    viewAllRoomsModel.isWifi() ? R.drawable.ic_yes : R.drawable.ic_no, 0);

            showDesc.setOnClickListener(v -> hotelShowDesc.setVisibility(hotelShowDesc.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE));

            bookHotel.setOnClickListener(v -> showBookDialogueExternal());
        }

    }

    private void showBookDialogueExternal() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog check_in = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar now1 = Calendar.getInstance();
                    TimePickerDialog check_out = TimePickerDialog.newInstance(
                            (view1, hourOfDay, minute, second) ->
                                    showBookDialogueInternal(monthOfYear, dayOfMonth, hourOfDay, minute),
                            now1.get(Calendar.HOUR_OF_DAY),
                            now1.get(Calendar.MINUTE),
                            true
                    );

                    check_out.setTitle("Select Check-in Time");
                    check_out.show(getSupportFragmentManager(), "sco");
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        check_in.setTitle("Select Check-in Date");
        check_in.show(getSupportFragmentManager(), "scn");
    }

    private void showBookDialogueInternal(int old_month, int old_day, int old_hour, int old_minute) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog check_in = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar now1 = Calendar.getInstance();
                    TimePickerDialog check_out = TimePickerDialog.newInstance(
                            (view1, hourOfDay, minute, second) ->
                            {
                                try {
                                    showBookDialogue(year, old_month, old_day, old_hour,
                                                old_minute, monthOfYear, dayOfMonth, hourOfDay, minute);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            },
                            now1.get(Calendar.HOUR_OF_DAY),
                            now1.get(Calendar.MINUTE),
                            true
                    );

                    check_out.setTitle("Select Check-out Time");
                    check_out.show(getSupportFragmentManager(), "sco");
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        check_in.setTitle("Select Check-out Date");
        check_in.show(getSupportFragmentManager(), "scn");
    }

    private void showBookDialogue(int year, int old_month, int old_day, int old_hour, int old_minute,
                                  int monthOfYear, int dayOfMonth, int hourOfDay, int minute) throws ParseException {


        View view = LayoutInflater.from(this).inflate(R.layout.layout_book_hotel, null);
        LayoutBookHotelBinding bookHotelBinding = LayoutBookHotelBinding.bind(view);

        bookingDialogue = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        bookHotelBinding.checkInDate.setText(old_day + " " + monthName(old_month) + "\n" + old_hour + ":" + old_minute);
        bookHotelBinding.checkOutDate.setText(dayOfMonth + " " + monthName(monthOfYear) + "\n" + hourOfDay + ":" + minute);

        bookHotelBinding.adultBedsNeed.setRange(1, (int) viewAllRoomsModel.getPeople_adult());
        bookHotelBinding.childBedsNeed.setRange(0, (int) viewAllRoomsModel.getPeople_child());
        bookHotelBinding.adultBedsNeed.setNumber(String.valueOf((int) viewAllRoomsModel.getPeople_adult()));
        bookHotelBinding.childBedsNeed.setNumber(String.valueOf((int) viewAllRoomsModel.getPeople_child()));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date firstDate = sdf.parse(old_month + "/" + old_day + "/" + year + " " + old_hour + ":" + old_minute + ":00");
        Date secondDate = sdf.parse(monthOfYear + "/" + dayOfMonth + "/" + year + " " + hourOfDay + ":" + minute + ":00");

        long diff = TimeUnit.DAYS.convert(Math.abs(secondDate.getTime() - firstDate.getTime()), TimeUnit.MILLISECONDS);

        bookHotelBinding.hotelRoomPriceTotal.setText(
                "INR " + (diff * viewAllRoomsModel.getPrice()) + " total for " + diff + " days."
        );

        bookHotelBinding.bookHotelConfirm.setOnSlideCompleteListener(slideToActView -> {

            long order_id = System.currentTimeMillis();

            Map<String, Object> all = new HashMap<>();

            all.put("HOTEL_NAME", viewAllRoomsModel.getName());
            all.put("CHECK_IN", firstDate.getTime());
            all.put("CHECK_OUT", secondDate.getTime());
            all.put("ADULT_BED", Integer.parseInt(bookHotelBinding.adultBedsNeed.getNumber()));
            all.put("CHILD_BED", Integer.parseInt(bookHotelBinding.childBedsNeed.getNumber()));
            all.put("PRICE_TOTAL", diff * viewAllRoomsModel.getPrice());
            all.put("ORDER_ID", order_id);
            all.put("IMAGES_ID", viewAllRoomsModel.getImages_url());
            all.put("IMAGES_LOC", viewAllRoomsModel.getParent_location());
            all.put("STATUS", 0);

            FDB.getFDB()
                    .collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("orders")
                    .document(String.valueOf(order_id))
                    .set(all, SetOptions.merge())
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Hotel is booked!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Hotel booking failed! Please retry!", Toast.LENGTH_SHORT).show());

            if (bookingDialogue.isShowing())
                bookingDialogue.dismiss();
        });

        if (!this.isFinishing())
            bookingDialogue.show();
    }

    private String monthName(int index) {
        String[] all = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return all[index];
    }
}