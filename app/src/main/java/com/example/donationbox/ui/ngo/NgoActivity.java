package com.example.donationbox.ui.ngo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
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

import com.example.donationbox.GlobalSettingsRepository;
import com.example.donationbox.R;
import com.example.donationbox.ui.auth.PhoneAuthActivity;
import com.example.donationbox.ui.donate.Donor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
                    isDataFiltered=true;
                    progressBar.setVisibility(View.VISIBLE);
                    Query filterQuery = dbRef.child("Donor").orderByChild("donorPincode").equalTo(Integer.parseInt(String.valueOf(v.getText())));
                    FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>()
                            .setQuery(filterQuery, snapshot -> {
                                Donor donor = snapshot.getValue(Donor.class);
                                return !donor.getDonorProductIsClaimed() ? donor : null;
                            })
                            .build();
                    adapter.updateOptions(options);
                    adapter.onDataChanged();
                    return true;
                }
                return false;
            });
        }

        ngoViewModel = ViewModelProviders.of(this).get(NgoViewModel.class);

        progressBar = findViewById(R.id.ngo_progressbar);
        donorListRecyclerView = findViewById(R.id.donor_list_recyclerview);
        donorListRecyclerView.setHasFixedSize(false);
        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = dbRef.child("Donor").orderByChild("donorProductIsClaimed").equalTo(false);
        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();

        adapter = new FirebaseRecyclerAdapter<Donor, NgoViewHolder>(options){

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public NgoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(NgoActivity.this).inflate(R.layout.ngo_product_list, parent, false);
                return new NgoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NgoViewHolder holder, int position, @NonNull Donor donor) {
                Log.e("pincode", String.valueOf(donor.getDonorPincode()));
                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productCategory.setText(donor.getDonorProductCategory());
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userAddress.setText(donor.getDonorAddress());
                holder.userName.setText(donor.getDonorName());
                holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
                holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
                Picasso.with(NgoActivity.this).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                holder.claimButton.setOnClickListener(v -> {
                    ngoViewModel.isProductClaimed(donor, ngoId);
                });
            }

        };

        donorListRecyclerView.setAdapter(adapter);

        //ignore this line, as it will be modified when number of NGOs will increase
        ngoId = GlobalSettingsRepository.getNgoId(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out){
            GlobalSettingsRepository.setUserType(this, "");
            startActivity(new Intent(this, PhoneAuthActivity.class));
        } else if(item.getItemId() == R.id.action_claimed_product){
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
            Query query = dbRef.child("Donor");
            FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();
            adapter.updateOptions(options);
            editText.setText("");
            isDataFiltered=false;
        } else {
            super.onBackPressed();
        }
    }
}
