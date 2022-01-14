package com.example.hotelbooking.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelbooking.R;
import com.example.hotelbooking.main_ui.DetailActivity;
import com.example.hotelbooking.objects.ViewAllRoomsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewAllRoomsAdapter extends RecyclerView.Adapter<ViewAllRoomsAdapter.ViewHolder> {

    Context context;
    List<ViewAllRoomsModel> viewAllRoomsModelList;

    public ViewAllRoomsAdapter(Context context, List<ViewAllRoomsModel> viewAllRoomsModelList) {
        this.context = context;
        this.viewAllRoomsModelList = viewAllRoomsModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_items,parent,false));

    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(viewAllRoomsModelList.get(position).getImages_url()).into(holder.hotelImage);
        holder.name.setText(viewAllRoomsModelList.get(position).getName());
        holder.address.setText(viewAllRoomsModelList.get(position).getAddress());
        holder.price.setText("Rs. " + viewAllRoomsModelList.get(position).getPrice());
        holder.city.setText(viewAllRoomsModelList.get(position).getCity());
        holder.roomType.setText(viewAllRoomsModelList.get(position).getRoom_type());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", viewAllRoomsModelList.get(position));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return viewAllRoomsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView hotelImage;
        TextView name,address,price,roomType,city;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.view_all_image_view);
            name = itemView.findViewById(R.id.view_all_hotel_name);
            address = itemView.findViewById(R.id.view_all_hotel_address);
            city = itemView.findViewById(R.id.view_all_hotel_city);
            price = itemView.findViewById(R.id.view_all_price);
            roomType = itemView.findViewById(R.id.view_all_room_type);

        }
    }
}
