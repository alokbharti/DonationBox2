package com.example.donationbox.ui.ngo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.donationbox.GlobalSettingsRepository;
import com.example.donationbox.R;
import com.example.donationbox.ui.donate.Donor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class NgoClaimedProductActivity extends AppCompatActivity {

    private NgoViewModel ngoViewModel;
    private RecyclerView claimedListRecyclerView;
    private FirebaseRecyclerAdapter adapter;
    private String ngoId = "";
    private ProgressBar progressBar;
    //get Firebase Reference
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_claimed_product);
        setTitle("My Claimed Product");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        ngoViewModel = ViewModelProviders.of(this).get(NgoViewModel.class);

        progressBar = findViewById(R.id.ngo_claimed_progressbar);
        claimedListRecyclerView = findViewById(R.id.claimed_list_recyclerview);
        claimedListRecyclerView.setHasFixedSize(false);
        claimedListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = dbRef.child("Donor").orderByChild("donorProductIsClaimed").equalTo(true);
        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>()
                .setQuery(query, snapshot->{
                    Donor donor = snapshot.getValue(Donor.class);
                    return donor.getDonorProductClaimedBy().equals(GlobalSettingsRepository.getNgoId(this)) ? donor : null;
                })
                .build();

        adapter = new FirebaseRecyclerAdapter<Donor, NgoViewHolder>(options){

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public NgoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(NgoClaimedProductActivity.this).inflate(R.layout.ngo_product_list_item, parent, false);
                return new NgoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NgoViewHolder holder, int position, @NonNull Donor donor) {
                Log.e("pincode", String.valueOf(donor.getDonorPincode()));
                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userAddress.setText(donor.getDonorAddress());
                holder.userName.setText(donor.getDonorName());
                holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
                holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
                Picasso.with(NgoClaimedProductActivity.this).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                holder.claimButton.setVisibility(View.GONE);
                holder.undoButton.setVisibility(View.VISIBLE);

                holder.undoButton.setOnClickListener(v -> {
                    ngoViewModel.isUndoClaimDone(donor, ngoId);
                });
            }

        };

        claimedListRecyclerView.setAdapter(adapter);


        MobileAds.initialize(this, "ca-app-pub-9542285454381078~2501556947");
        AdView adView = findViewById(R.id.ngo_claimed_adView);
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

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
