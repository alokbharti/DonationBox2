package com.example.donationbox.ui.user_product;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;

public class UserProductViewHolder extends RecyclerView.ViewHolder {

    public TextView productDetails, userName, userAddress, userPincode, productTimestamp, productQuality, userPhoneNumber, userProductStatus;
    public AppCompatImageView productImage;
    public AppCompatButton deleteButton;

    public UserProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productDetails = itemView.findViewById(R.id.user_product_details);
        productTimestamp = itemView.findViewById(R.id.user_product_timestamp);
        productQuality = itemView.findViewById(R.id.user_product_condition);
        userName = itemView.findViewById(R.id.user_product_donor_name);
        userAddress = itemView.findViewById(R.id.user_product_donor_address);
        userPincode = itemView.findViewById(R.id.user_product_donor_pincode);
        userProductStatus = itemView.findViewById(R.id.user_product_status);
        userPhoneNumber = itemView.findViewById(R.id.user_product_donor_phone_number);
        productImage = itemView.findViewById(R.id.user_product_image);
        deleteButton = itemView.findViewById(R.id.delete_button);

    }
}
