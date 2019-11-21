package com.example.donationbox.ui.ngo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.donationbox.ui.donate.Donor;

import java.util.ArrayList;

public class NgoViewModel extends ViewModel {

    private NgoRepository ngoRepository;

    public NgoViewModel() {
        ngoRepository = new NgoRepository();
    }

    public MutableLiveData<ArrayList<Donor>> getAllDonorList(){
        return ngoRepository.getAllProductList();
    }

    public LiveData<Boolean> isProductClaimed(Donor donor, String ngoId){
        return ngoRepository.claimDonatedProduct(donor, ngoId);
    }

    public LiveData<Boolean> isUndoClaimDone(Donor donor, String ngoId){
        return ngoRepository.undoClaimDonatedProduct(donor, ngoId);
    }
}
