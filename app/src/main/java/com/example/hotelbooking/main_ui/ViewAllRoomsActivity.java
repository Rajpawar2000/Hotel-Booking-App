package com.example.hotelbooking.main_ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;
import com.example.hotelbooking.adapters.ViewAllRoomsAdapter;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.objects.ViewAllRoomsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAllRoomsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ViewAllRoomsAdapter viewAllRoomsAdapter;
    List<ViewAllRoomsModel> viewAllRoomsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewAllRoomsModelList = new ArrayList<>();
        viewAllRoomsAdapter = new ViewAllRoomsAdapter(this, viewAllRoomsModelList);
        recyclerView.setAdapter(viewAllRoomsAdapter);

        String city = getIntent().getStringExtra("city");

        String[] cityList = {"mumbai","bangalore","goa","chennai","kolkata","lucknow","delhi","jaipur"};

        for (int i = 0; i<=cityList.length-1; i++){
            if (city!= null && city.equalsIgnoreCase(cityList[i])){
                FDB.getFDB().collection("city").document(cityList[i]).collection("roomCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult()).getDocuments()){
                            ViewAllRoomsModel viewAllRoomsModel = documentSnapshot.toObject(ViewAllRoomsModel.class);
                            viewAllRoomsModelList.add(viewAllRoomsModel);
                            viewAllRoomsAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }
}