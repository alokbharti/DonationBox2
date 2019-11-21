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

    public DonateViewModel() {
        imageUrl = new MutableLiveData<>();
        imageUrl.setValue("");
        isImageUploaded = new MutableLiveData<>();
        isImageUploaded.setValue(false);
        isDonorDataSaved = new MutableLiveData<>();
        isDonorDataSaved.setValue(false);
    }

    public LiveData<String> getImageDownLoadUrl() {
        return imageUrl;
    }
    public LiveData<Boolean> getImageUploadProgress() {
        return isImageUploaded;
    }
    public LiveData<Boolean> getDonorDataUploadingStatus(){return isDonorDataSaved;}

    public void uploadAndGetImageUrl(Uri file){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProductImages");
        storageReference.putFile(file)
                .continueWithTask(task -> {
                    // Forward any exceptions
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Request the public download URL
                    return storageReference.getDownloadUrl();
                })
                .addOnSuccessListener(uri1 -> {
                    Log.e("Image upload success", uri1.toString());
                    isImageUploaded.setValue(true);
                    imageUrl.setValue(uri1.toString());
                });
    }

    public void saveDataToDatabase(Donor donor){
        DatabaseReference localDbRef;
        localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor");

        localDbRef.push().setValue(donor).addOnSuccessListener(aVoid -> isDonorDataSaved.setValue(true));
    }

}