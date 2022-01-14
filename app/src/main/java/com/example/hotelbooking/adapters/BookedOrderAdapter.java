package com.example.hotelbooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.R;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.objects.Order;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookedOrderAdapter extends RecyclerView.Adapter<BookedOrderAdapter.ViewHolder> {

	Context context;
	List<Order> mOrders;

	public BookedOrderAdapter(Context context, ArrayList<Order> orders) {
		this.context = context;
		this.mOrders = orders;
	}


	@NonNull
	@NotNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_order_list,parent,false));
	}


	@Override
	public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
		Glide.with(context).load(mOrders.get(position).getImages_id()).into(holder.hotelImage);
		holder.hotelName.setText(mOrders.get(position).getHotel_name());
		holder.hotelPrice.setText("Rs. " + mOrders.get(position).getPrice_total());

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(mOrders.get(position).getCheck_in());
		holder.hotelCheckIn.setText("Check-in : " + calendar.get(Calendar.DAY_OF_MONTH) + "th" + monthName(calendar.get(Calendar.MONTH))
				+ " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

		calendar.setTimeInMillis(mOrders.get(position).getCheck_out());
		holder.hotelCheckOut.setText("Check-out : " + calendar.get(Calendar.DAY_OF_MONTH) + "th" + monthName(calendar.get(Calendar.MONTH))
				+ " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

		holder.hotelRoomAdult.setText(mOrders.get(position).getAdult_bed() + " Adult Beds");
		holder.hotelRoomChild.setText(mOrders.get(position).getChild_bed() + " Child Beds");

		holder.hotelPrice.setText("INR " + mOrders.get(position).getPrice_total() + " in total");
		holder.hotelRoomStatus.setText(FDB.status(mOrders.get(position).getStatus()));

		if (mOrders.get(position).getStatus() != FDB.STATUS_JUST_ORDERED) {
			holder.requestCancel.setEnabled(false);
		}



		holder.requestCancel.setOnClickListener(v -> {
			Map<String, Object> update = new HashMap<>();
			update.put("STATUS", FDB.STATUS_REQ_CANCELLATION);

			FDB.getFDB()
					.collection("users")
					.document(FirebaseAuth.getInstance().getUid())
					.collection("orders")
					.document("" + mOrders.get(position).getOrder_id())
					.set(update, SetOptions.merge()).addOnSuccessListener(unused -> {
				Toast.makeText(context, "Booking cancellation is requested! Wait for sometime.", Toast.LENGTH_SHORT).show();
				holder.hotelRoomStatus.setText("Cancellation Requested");
				BookedOrderAdapter.this.notifyDataSetChanged();
			}).addOnFailureListener(e -> Toast.makeText(context, "Failed to request cancellation, retry.", Toast.LENGTH_SHORT).show());
		});

	}

	private String monthName(int index) {
		String[] all = {"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"};
		return all[index];
	}

	@Override
	public int getItemCount() {
		return mOrders.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{

		ImageView hotelImage;
		TextView hotelName,hotelRoomAdult,hotelRoomChild,hotelCheckIn,hotelCheckOut;
		TextView hotelPrice,hotelRoomStatus;
		MaterialButton requestCancel;

		public ViewHolder(@NonNull @NotNull View itemView) {
			super(itemView);

			hotelImage = itemView.findViewById(R.id.order_hotel_room_images);
			hotelName = itemView.findViewById(R.id.order_hotel_name);
			hotelRoomAdult = itemView.findViewById(R.id.order_hotel_room_adult);
			hotelRoomChild = itemView.findViewById(R.id.order_hotel_room_child);
			hotelCheckIn = itemView.findViewById(R.id.order_hotel_check_in);
			hotelCheckOut = itemView.findViewById(R.id.order_hotel_check_out);
			hotelPrice = itemView.findViewById(R.id.order_hotel_room_price);
			hotelRoomStatus = itemView.findViewById(R.id.order_hotel_room_status);
			requestCancel = itemView.findViewById(R.id.order_cancel_hotel);
		}
	}
}
