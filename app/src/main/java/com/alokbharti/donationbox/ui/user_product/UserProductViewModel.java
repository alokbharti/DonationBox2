package com.alokbharti.donationbox.ui.user_product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alokbharti.donationbox.ui.donate.Donor;

public class UserProductViewModel extends ViewModel {

    private UserProductRepository userProductRepository;

    public UserProductViewModel() {
        userProductRepository = new UserProductRepository();
    }

    public LiveData<Boolean> deleteProduct(Donor donor) {
        return userProductRepository.deleteProductFromDb(donor);
    }
}