package com.example.donationbox.ui.donate;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DonateViewModel extends ViewModel {

    private MutableLiveData<String> imageUrl;
    private MutableLiveData<Boolean> isImageUploaded;
    private MutableLiveData<Boolean> isDonorDataSaved;
    private DonorRepository donorRepository;

    public DonateViewModel() {
        imageUrl = new MutableLiveData<>();
        imageUrl.setValue("");
        isImageUploaded = new MutableLiveData<>();
        isImageUploaded.setValue(false);
        isDonorDataSaved = new MutableLiveData<>();
        isDonorDataSaved.setValue(false);

        donorRepository = new DonorRepository();
    }

    public LiveData<String> getImageDownLoadUrl(Uri file) {
        return donorRepository.uploadAndGetImageUrl(file);
    }
    public LiveData<Boolean> getDonorDataUploadingStatus(Donor donor){return donorRepository.saveDataToDatabase(donor);}

}