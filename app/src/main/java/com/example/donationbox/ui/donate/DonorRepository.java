package com.example.donationbox.ui.donate;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DonorRepository {

    public MutableLiveData<String> uploadAndGetImageUrl(Uri file){
        MutableLiveData<String> imageUrl = new MutableLiveData<>();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProductImages").child(String.valueOf(System.currentTimeMillis()));
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
                    //Log.e("Image upload success", uri1.toString());
                    imageUrl.setValue(uri1.toString());
                });

        return imageUrl;
    }

    public MutableLiveData<Boolean> saveDataToDatabase(Donor donor){
        MutableLiveData<Boolean> isDonorDataSaved = new MutableLiveData<>();

        DatabaseReference localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor");

        localDbRef.child(String.valueOf(donor.getDonatedTimestamp())).setValue(donor).addOnSuccessListener(aVoid -> isDonorDataSaved.setValue(true));

        return isDonorDataSaved;
    }
}
