package com.alokbharti.donationbox.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alokbharti.donationbox.ProductDataRepository;
import com.alokbharti.donationbox.ui.donate.Donor;

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