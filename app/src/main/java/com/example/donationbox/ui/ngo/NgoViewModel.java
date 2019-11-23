package com.example.donationbox.ui.ngo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donationbox.ProductDataRepository;
import com.example.donationbox.ui.donate.Donor;

import java.util.ArrayList;

public class NgoViewModel extends ViewModel {

    private ProductDataRepository productDataRepository;

    public NgoViewModel() {
        productDataRepository = new ProductDataRepository();
    }

    public MutableLiveData<ArrayList<Donor>> getAllDonorList(){
        return productDataRepository.getAllProductList();
    }

    public LiveData<Boolean> isProductClaimed(Donor donor, String ngoId){
        return productDataRepository.claimDonatedProduct(donor, ngoId);
    }

    public LiveData<Boolean> isUndoClaimDone(Donor donor, String ngoId){
        return productDataRepository.undoClaimDonatedProduct(donor, ngoId);
    }
}
