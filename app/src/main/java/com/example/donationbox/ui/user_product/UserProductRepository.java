package com.example.donationbox.ui.user_product;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.donationbox.ui.donate.Donor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProductRepository {

    public MutableLiveData<Boolean> deleteProductFromDb(Donor donor){
        MutableLiveData<Boolean> isProductDeleted = new MutableLiveData<>();

        FirebaseDatabase.getInstance().getReference().child("Donor")
                .child(String.valueOf(donor.getDonatedTimestamp()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot donorSnapshot: dataSnapshot.getChildren()) {
                            donorSnapshot.getRef().removeValue();
                            isProductDeleted.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        isProductDeleted.setValue(false);
                    }
        });

        return isProductDeleted;
    }
}
