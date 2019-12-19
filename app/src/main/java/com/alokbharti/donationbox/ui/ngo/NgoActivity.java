package com.alokbharti.donationbox.ui.ngo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alokbharti.donationbox.FirebaseFilterableRecyclerAdapter;
import com.alokbharti.donationbox.GlobalSettingsRepository;
import com.alokbharti.donationbox.InternetConnectionLiveData;
import com.alokbharti.donationbox.R;
import com.alokbharti.donationbox.UtilFunctions;
import com.alokbharti.donationbox.ui.auth.PhoneAuthActivity;
import com.alokbharti.donationbox.ui.donate.Donor;
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

public class NgoActivity extends AppCompatActivity{

    private NgoViewModel ngoViewModel;
    private RecyclerView donorListRecyclerView;
    private FirebaseRecyclerAdapter adapter;
    private String ngoId = "";
    private ProgressBar progressBar;
    //get Firebase Reference
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    boolean isDataFiltered=false;
    EditText editText;
    private boolean isFirsTimeInternetCheck;
    private FirebaseFilterableRecyclerAdapter filterableRecyclerAdapter;
    private TextView emptyDonationTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        //set custom toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.ngo_toolbar);
            
            editText = getSupportActionBar().getCustomView().findViewById(R.id.pincode_edit_text);
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (v.getText().length()<6){
                        Toast.makeText(this, "Enter valid pincode!!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    isDataFiltered=true;
                    progressBar.setVisibility(View.VISIBLE);
                    setFilteredAdapter(v.getText().toString());
                    return true;
                }
                return false;
            });
        }

        ngoViewModel = ViewModelProviders.of(this).get(NgoViewModel.class);

        progressBar = findViewById(R.id.ngo_progressbar);
        TextView internetTv = findViewById(R.id.ngo_internet_text);
        emptyDonationTv = findViewById(R.id.empty_donation_text);
        progressBar = findViewById(R.id.ngo_progressbar);
        if (!UtilFunctions.isOnline(this)){
            progressBar.setVisibility(View.GONE);
            internetTv.setVisibility(View.VISIBLE);
        }

        observeInternetStatusChanges();

        donorListRecyclerView = findViewById(R.id.donor_list_recyclerview);
        donorListRecyclerView.setHasFixedSize(false);
        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = dbRef.child("Donor").orderByChild("donorProductIsClaimed").equalTo(false);
        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();

        adapter = new FirebaseRecyclerAdapter<Donor, NgoViewHolder>(options){

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
                internetTv.setVisibility(View.GONE);

                if(options.getSnapshots().isEmpty()) emptyDonationTv.setVisibility(View.VISIBLE);
            }

            @NonNull
            @Override
            public NgoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(NgoActivity.this).inflate(R.layout.ngo_product_list_item, parent, false);
                return new NgoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NgoViewHolder holder, int position, @NonNull Donor donor) {
                //Log.e("pincode", String.valueOf(donor.getDonorPincode()));
                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userAddress.setText(donor.getDonorAddress());
                holder.userName.setText(donor.getDonorName());
                holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
                holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
                holder.userDate.setText(UtilFunctions.getDateFromTimeStamp(donor.getDonatedTimestamp()));
                Picasso.with(NgoActivity.this).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                holder.claimButton.setOnClickListener(v -> {
                    ngoViewModel.isProductClaimed(donor, ngoId);
                });
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        donorListRecyclerView.setAdapter(adapter);

        //ignore this line, as it will be modified when number of NGOs will increase
        ngoId = GlobalSettingsRepository.getNgoId(this);

        MobileAds.initialize(this, "ca-app-pub-9542285454381078~2501556947");
        AdView adView = findViewById(R.id.ngo_adView);
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
    protected void onResume() {
        super.onResume();
        isFirsTimeInternetCheck = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ngo_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ngo_sign_out){
            GlobalSettingsRepository.setUserType(this, "");
            startActivity(new Intent(this, PhoneAuthActivity.class));
        } else if(item.getItemId() == R.id.ngo_claimed_product){
            startActivity(new Intent(this, NgoClaimedProductActivity.class));
        }
        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        if(isDataFiltered){
            filterableRecyclerAdapter.stopListening();
            filterableRecyclerAdapter.onDetachedFromRecyclerView(donorListRecyclerView);
            donorListRecyclerView.setAdapter(adapter);
            Query query = dbRef.child("Donor").orderByChild("donorProductIsClaimed").equalTo(false);
            FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();
            adapter.updateOptions(options);
            editText.setText("");
            isDataFiltered=false;
        } else {
            super.onBackPressed();
        }
    }

    private void observeInternetStatusChanges() {
        new InternetConnectionLiveData(this).observe(this, isInternetConnected ->{
            if (!isInternetConnected){
                UtilFunctions.displaySnackBar(this, findViewById(R.id.ngo_ll), "You're offline, check your internet!!", 0);
            } else {
                if (!isFirsTimeInternetCheck) UtilFunctions.displaySnackBar(this, findViewById(R.id.ngo_ll), "We're back again", 0);
                else isFirsTimeInternetCheck = false;
            }
        });
    }

    private void setFilteredAdapter(String pincode){
        Query filterQuery = dbRef.child("Donor").orderByChild("donorPincode").equalTo(Integer.parseInt(pincode));
        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>()
                .setQuery(filterQuery, Donor.class)
                .build();

        filterableRecyclerAdapter = new FirebaseFilterableRecyclerAdapter<Donor, NgoViewHolder>(options, true){

            @Override
            public void onDataChanged() {
                //constraint doesn't matter 'cause filter is working isClaimed property
                filterableRecyclerAdapter.getFilter().filter("Doesn't matter");
                progressBar.setVisibility(View.GONE);

                if(options.getSnapshots().isEmpty()) emptyDonationTv.setVisibility(View.VISIBLE);
            }

            @NonNull
            @Override
            public NgoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(NgoActivity.this).inflate(R.layout.ngo_product_list_item, parent, false);
                return new NgoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NgoViewHolder holder, int position, @NonNull Donor donor) {
                //Log.e("pincode", String.valueOf(donor.getDonorPincode()));
                if(donor.getDonorProductIsClaimed()) emptyDonationTv.setVisibility(View.VISIBLE);

                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userAddress.setText(donor.getDonorAddress());
                holder.userName.setText(donor.getDonorName());
                holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
                holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
                holder.userDate.setText(UtilFunctions.getDateFromTimeStamp(donor.getDonatedTimestamp()));
                Picasso.with(NgoActivity.this).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                holder.claimButton.setOnClickListener(v -> {
                    ngoViewModel.isProductClaimed(donor, ngoId);
                });
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected boolean filterCondition(Donor donor, String filterPattern) {
                return !donor.getDonorProductIsClaimed();
            }
        };

        adapter.onDetachedFromRecyclerView(donorListRecyclerView);
        filterableRecyclerAdapter.startListening();
        donorListRecyclerView.setAdapter(filterableRecyclerAdapter);
    }
}
