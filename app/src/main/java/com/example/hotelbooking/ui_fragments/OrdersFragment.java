package com.example.hotelbooking.ui_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hotelbooking.adapters.BookedOrderAdapter;
import com.example.hotelbooking.databinding.FragmentOrdersBinding;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.objects.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

	private FragmentOrdersBinding binding;
	private ArrayList<Order> orders;
	BookedOrderAdapter adapter;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		binding = FragmentOrdersBinding.inflate(inflater, container, false);
		orders = new ArrayList<>();

		load();
		return binding.getRoot();
	}

	private void load() {
		FDB.getFDB()
				.collection("users")
				.document(FirebaseAuth.getInstance().getUid())
				.collection("orders")
				.get()
				.addOnSuccessListener(queryDocumentSnapshots -> {
					for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
						Order order = new Order();
						order.setHotel_name(snapshot.getString("HOTEL_NAME"))
								.setCheck_in(snapshot.getLong("CHECK_IN"))
								.setCheck_out(snapshot.getLong("CHECK_OUT"))
								.setAdult_bed(snapshot.getLong("ADULT_BED"))
								.setChild_bed(snapshot.getLong("CHILD_BED"))
								.setPrice_total(snapshot.getLong("PRICE_TOTAL"))
								.setOrder_id(snapshot.getLong("ORDER_ID"))
								.setImages_id(snapshot.getString("IMAGES_ID"))
								.setOrder_loc(snapshot.getString("IMAGES_LOC"))
								.setStatus(toIntExact(snapshot.getLong("STATUS")));
						orders.add(order);
					}
					binding.loadingStuff.setVisibility(View.GONE);
					if (orders.size() == 0) {
						binding.zeroOrders.setVisibility(View.VISIBLE);
						binding.allBookedHotels.setVisibility(View.GONE);
					} else {
						adapter = new BookedOrderAdapter(getActivity(), orders);
						binding.allBookedHotels.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error loading orders", Toast.LENGTH_SHORT).show());
	}

	public static int toIntExact(long value) {
		if ((int) value != value) {
			throw new ArithmeticException("integer overflow");
		}
		return (int) value;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}