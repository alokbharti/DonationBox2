package com.example.donationbox.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donationbox.ProductDataRepository;
import com.example.donationbox.ui.donate.Donor;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private ProductDataRepository productDataRepository;

    public HomeViewModel() {
        productDataRepository = new ProductDataRepository();
    }

    public LiveData<ArrayList<Donor>> getAllDonatedProductList(){
        return productDataRepository.getAllProductList();
    }

}