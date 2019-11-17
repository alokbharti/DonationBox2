package com.example.donationbox.ui.ngo;

import androidx.annotation.NonNull;

import com.example.donationbox.ui.donate.Donor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NgoRepository {

    public ArrayList<ProductListForNgo> getAllProductList(){
        ArrayList<ProductListForNgo> allProductList = new ArrayList<>();
        ArrayList<ProductListForNgo> allClaimedProductList = new ArrayList<>();
        ArrayList<ProductListForNgo> allNonClaimedProductList = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference clothDatabaseRef = firebaseDatabase.getReference().child("Clothes");
        DatabaseReference foodDatabaseRef = firebaseDatabase.getReference().child("Food");
        DatabaseReference bookDatabaseRef = firebaseDatabase.getReference().child("Books");
        DatabaseReference otherDatabaseRef = firebaseDatabase.getReference().child("Others");

        clothDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donor donor = snapshot.getValue(Donor.class);
                    if (donor != null) {
                        ProductListForNgo temp = new ProductListForNgo(donor.getDonorProductDetails(), donor.getDonorName(), donor.getDonorAddress(), donor.getDonorProductQuality(),
                                donor.getDonorProductImageUrl(), donor.getDonorProductIsClaimed(), donor.getDonorPincode());

                        if (temp.isClaimed()){
                            allClaimedProductList.add(temp);
                        } else{
                            allNonClaimedProductList.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        foodDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donor donor = snapshot.getValue(Donor.class);
                    if (donor != null) {
                        ProductListForNgo temp = new ProductListForNgo(donor.getDonorProductDetails(), donor.getDonorName(), donor.getDonorAddress(), donor.getDonorProductQuality(),
                                donor.getDonorProductImageUrl(), donor.getDonorProductIsClaimed(), donor.getDonorPincode());

                        if (temp.isClaimed()){
                            allClaimedProductList.add(temp);
                        } else{
                            allNonClaimedProductList.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donor donor = snapshot.getValue(Donor.class);
                    if (donor != null) {
                        ProductListForNgo temp = new ProductListForNgo(donor.getDonorProductDetails(), donor.getDonorName(), donor.getDonorAddress(), donor.getDonorProductQuality(),
                                donor.getDonorProductImageUrl(), donor.getDonorProductIsClaimed(), donor.getDonorPincode());

                        if (temp.isClaimed()){
                            allClaimedProductList.add(temp);
                        } else{
                            allNonClaimedProductList.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        otherDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donor donor = snapshot.getValue(Donor.class);
                    if (donor != null) {
                        ProductListForNgo temp = new ProductListForNgo(donor.getDonorProductDetails(), donor.getDonorName(), donor.getDonorAddress(), donor.getDonorProductQuality(),
                                donor.getDonorProductImageUrl(), donor.getDonorProductIsClaimed(), donor.getDonorPincode());

                        if (temp.isClaimed()){
                            allClaimedProductList.add(temp);
                        } else{
                            allNonClaimedProductList.add(temp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        allProductList.addAll(allNonClaimedProductList);
        allProductList.addAll(allClaimedProductList);

        return allProductList;
    }
}
