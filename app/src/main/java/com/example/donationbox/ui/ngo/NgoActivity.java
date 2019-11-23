package com.example.donationbox.ui.ngo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.donationbox.GlobalSettingsRepository;
import com.example.donationbox.R;
import com.example.donationbox.ui.auth.PhoneAuthActivity;
import com.example.donationbox.ui.donate.Donor;

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

        //ignore this line, as it will be modified when number of NGOs will increase
        ngoId = GlobalSettingsRepository.getNgoId(this);

    }

    @Override
    public void onClaimListener(Donor donor, int position) {
        ngoViewModel.isProductClaimed(donor, ngoId);
        productListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onUndoClaimListener(Donor donor, int position) {
        ngoViewModel.isUndoClaimDone(donor, ngoId);
        productListAdapter.notifyItemChanged(position);
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
