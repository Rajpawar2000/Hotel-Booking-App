package com.example.hotelbooking.ui_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;
import com.example.hotelbooking.adapters.ExploreCitiesAdapter;
import com.example.hotelbooking.functions.FDB;
import com.example.hotelbooking.functions.LocalDatabase;
import com.example.hotelbooking.objects.ExploreCitiesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    RecyclerView exploreCityRec;
    ProgressBar progressBar;

    // Explore cities
    List<ExploreCitiesModel> exploreCitiesModelList;
    ExploreCitiesAdapter exploreCitiesAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.linearProgressIndicator);
        progressBar.setVisibility(View.VISIBLE);

        //explore cities
        exploreCityRec = root.findViewById(R.id.all_main_category);
        TextView textHome = root.findViewById(R.id.text_home);
        exploreCityRec.setLayoutManager(new GridLayoutManager(getContext(),2));
        exploreCitiesModelList = new ArrayList<>();
        exploreCitiesAdapter = new ExploreCitiesAdapter(getActivity(), exploreCitiesModelList);
        exploreCityRec.setAdapter(exploreCitiesAdapter);

        FDB.getFDB().collection("city")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ExploreCitiesModel exploreCitiesModel = document.toObject(ExploreCitiesModel.class);
                                exploreCitiesModelList.add(exploreCitiesModel);
                                exploreCitiesAdapter.notifyDataSetChanged();
                                textHome.setText(getString(R.string._choose_text)
                                        .replaceAll("X", String.valueOf(exploreCitiesModelList.size()))
                                        .replaceAll("#", Objects.requireNonNull(LocalDatabase.getInstance(getContext()).getUsername()
                                                == null ? LocalDatabase.getInstance(getContext()).getPhoneNumber() :
                                                LocalDatabase.getInstance(getContext()).getUsername())));

                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    }
                }).addOnFailureListener(e -> Snackbar.make(exploreCityRec, "Error connecting to network!", Snackbar.LENGTH_LONG).show());

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}