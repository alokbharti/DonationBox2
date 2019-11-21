package com.example.donationbox.ui.ngo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.donationbox.GlobalSettingsRepository;
import com.example.donationbox.R;
import com.example.donationbox.ui.auth.PhoneAuthActivity;
import com.example.donationbox.ui.donate.Donor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NgoActivity extends AppCompatActivity implements ProductClickListener{

    private NgoViewModel ngoViewModel;
    private RecyclerView donorListRecyclerView;
    private RecyclerView.Adapter productListAdapter;
    private String ngoId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        ngoViewModel = ViewModelProviders.of(this).get(NgoViewModel.class);

        donorListRecyclerView = (RecyclerView) findViewById(R.id.donor_list_recyclerview);
        donorListRecyclerView.setHasFixedSize(false);
        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Donor> allDonatedProductList = new ArrayList<>();
        productListAdapter = new NgoAdapter(allDonatedProductList, this);
        donorListRecyclerView.setAdapter(productListAdapter);

        ngoViewModel.getAllDonorList().observe(this, productList -> {
            allDonatedProductList.addAll(productList);
            productListAdapter.notifyDataSetChanged();
        });
        Log.e("product list", allDonatedProductList.toString());

        ngoId = GlobalSettingsRepository.getNgoId(this);

    }

    @Override
    public void onClaimListener(Donor donor) {
        ngoViewModel.isProductClaimed(donor, ngoId);
    }

    @Override
    public void onUndoClaimListener(Donor donor) {
        ngoViewModel.isUndoClaimDone(donor, ngoId);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
