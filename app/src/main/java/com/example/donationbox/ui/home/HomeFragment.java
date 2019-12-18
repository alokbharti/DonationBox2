package com.example.donationbox.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;
import com.example.donationbox.UtilFunctions;
import com.example.donationbox.ui.donate.Donor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView productListRecyclerView;
    private ProgressBar progressBar;
    private FirebaseRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView internetTv = root.findViewById(R.id.internet_text);
        progressBar = root.findViewById(R.id.home_progressbar);
        if (!UtilFunctions.isOnline(getContext())){
            progressBar.setVisibility(View.GONE);
            internetTv.setVisibility(View.VISIBLE);
        }

        productListRecyclerView = root.findViewById(R.id.product_list_recyclerview);
        productListRecyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        productListRecyclerView.setLayoutManager(gridLayoutManager);

        Query query = FirebaseDatabase.getInstance().getReference().child("Donor").orderByChild("donorPhoneNumber");

        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();

        adapter = new FirebaseRecyclerAdapter<Donor, ProductHolder>(options) {

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
                internetTv.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.donate_product_list, parent, false);

                return new ProductHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProductHolder holder, int position, Donor donor) {
                // Bind the Chat object to the ChatHolder
                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userName.setText(donor.getDonorName());
                Picasso.with(getContext()).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                if(donor.getDonorProductIsClaimed()){
                    holder.productClaimStatus.setText(String.format("Claimed by: %s", donor.getDonorProductClaimedBy()));
                } else {
                    holder.productClaimStatus.setText("Not Claimed");
                }
            }
        };

        productListRecyclerView.setAdapter(adapter);

        MobileAds.initialize(getContext(), "ca-app-pub-9542285454381078~2501556947");
        AdView adView = root.findViewById(R.id.adView);
        adView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}