package com.example.donationbox.ui.donate;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonateViewModel extends ViewModel {

    private MutableLiveData<String> imageUrl;
    private MutableLiveData<Integer> progress;

    public DonateViewModel() {
        imageUrl = new MutableLiveData<>();
        imageUrl.setValue("");
        progress = new MutableLiveData<>();
        progress.setValue(0);
    }

    public LiveData<String> getImageDownLoadUrl() {
        return imageUrl;
    }
    public LiveData<Integer> getImageUploadProgress() {return progress;}

    public void uploadAndGetImageUrl(Uri file){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProductImages");
        storageReference.putFile(file)
                .addOnProgressListener(taskSnapshot ->
                        progress.setValue((int) (100.0 * taskSnapshot.getBytesTransferred()))
                )
                .continueWithTask(task -> {
                    // Forward any exceptions
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Request the public download URL
                    return storageReference.getDownloadUrl();
                })
                .addOnSuccessListener(uri1 -> {
                    imageUrl.setValue(uri1.toString());
                });
    }

    public void saveDataToDatabase(Donor donor){
        DatabaseReference localDbRef;
        switch (donor.getDonorProductCategory()){
            case "Food":
                localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor").child("Food");
                break;
            case "Cloth":
                localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor").child("Cloth");
                break;
            case "Book":
                localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor").child("Book");
                break;
            case "Other":
                localDbRef = FirebaseDatabase.getInstance().getReference().child("Donor").child("Other");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + donor.getDonorProductCategory());
        }

        localDbRef.push().setValue(donor);
    }
}