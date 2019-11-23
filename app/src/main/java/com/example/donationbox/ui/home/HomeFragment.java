package com.example.donationbox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;
import com.example.donationbox.ui.donate.Donor;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView productListRecyclerView;
    private RecyclerView.Adapter productListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        productListRecyclerView = root.findViewById(R.id.product_list_recyclerview);
        productListRecyclerView.setHasFixedSize(false);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Donor> allDonatedProductList = new ArrayList<>();
        productListAdapter = new ProductListAdapter(allDonatedProductList);
        productListRecyclerView.setAdapter(productListAdapter);

        homeViewModel.getAllDonatedProductList().observe(this, productList -> {
            allDonatedProductList.addAll(productList);
            productListAdapter.notifyDataSetChanged();
        });

        return root;
    }

}