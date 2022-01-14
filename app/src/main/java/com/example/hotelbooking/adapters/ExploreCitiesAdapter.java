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
import com.example.hotelbooking.main_ui.ViewAllRoomsActivity;
import com.example.hotelbooking.objects.ExploreCitiesModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExploreCitiesAdapter extends RecyclerView.Adapter<ExploreCitiesAdapter.ViewHolder> {

    private Context context;
    List<ExploreCitiesModel> exploreCitiesModelList;


    public ExploreCitiesAdapter(Context context, List<ExploreCitiesModel> exploreCitiesModelList) {
        this.context = context;
        this.exploreCitiesModelList = exploreCitiesModelList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_cities_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(context).load(exploreCitiesModelList.get(position).getCityImg_url()).into(holder.cityImg);
        holder.city.setText(exploreCitiesModelList.get(position).getCity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAllRoomsActivity.class);
                intent.putExtra("city",exploreCitiesModelList.get(position).getCity());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exploreCitiesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cityImg;
        TextView city;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cityImg = itemView.findViewById(R.id.single_image);
            city = itemView.findViewById(R.id.location_name);

        }
    }
}
