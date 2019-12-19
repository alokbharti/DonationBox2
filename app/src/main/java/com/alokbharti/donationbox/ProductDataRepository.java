package com.alokbharti.donationbox;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alokbharti.donationbox.ui.donate.Donor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDataRepository {

    public MutableLiveData<ArrayList<Donor>> getAllProductList(){
        MutableLiveData<ArrayList<Donor>> allProductList = new MutableLiveData<>();
        /*MutableLiveData<ArrayList<Donor>> allClaimedProductList = new MutableLiveData<>();
        MutableLiveData<ArrayList<Donor>> allNonClaimedProductList = new MutableLiveData<>();*/

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Donor");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Donor> tempList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donor donor = snapshot.getValue(Donor.class);
                    if (donor != null) {
                        /*if (donor.getDonorProductIsClaimed()){
                            allClaimedProductList.s(donor);
                        } else{
                            allNonClaimedProductList.add(donor);
                        }*/
                        tempList.add(donor);
                    }
                }
                allProductList.setValue(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*allProductList.setValue(allNonClaimedProductList);
        allProductList.setValue(allClaimedProductList);*/

        return allProductList;
    }

    //timeStamp works here as primary key
    public LiveData<Boolean> claimDonatedProduct(Donor donor, String ngoId){
        MutableLiveData<Boolean> isProductUpdated = new MutableLiveData<>();
        isProductUpdated.setValue(false);

        donor.setDonorProductIsClaimed(true);
        //remove @12 from id
        donor.setDonorProductClaimedBy(ngoId.substring(0, ngoId.length()-3));

        FirebaseDatabase.getInstance().getReference().child("Donor")
                .child(String.valueOf(donor.getDonatedTimestamp()))
                .setValue(donor)
                .addOnSuccessListener(aVoid -> isProductUpdated.setValue(true));

        return isProductUpdated;
    }

    public LiveData<Boolean> undoClaimDonatedProduct(Donor donor, String ngoId){
        MutableLiveData<Boolean> isProductUpdated = new MutableLiveData<>();
        isProductUpdated.setValue(false);

        donor.setDonorProductIsClaimed(false);
        donor.setDonorProductClaimedBy("");
        FirebaseDatabase.getInstance().getReference().child("Donor")
                .child(String.valueOf(donor.getDonatedTimestamp()))
                .setValue(donor)
                .addOnSuccessListener(aVoid -> isProductUpdated.setValue(true));

        return isProductUpdated;
    }
}
