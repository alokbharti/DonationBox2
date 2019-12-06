package com.example.donationbox.ui.ngo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;

public class NgoViewHolder extends RecyclerView.ViewHolder {

    public TextView productDetails, userName, userAddress, userPincode, productQuality, userPhoneNumber, userDate;
    public AppCompatImageView productImage;
    public AppCompatButton claimButton, undoButton;
    public CardView productCard;

    public NgoViewHolder(@NonNull View itemView) {
        super(itemView);

        productDetails = itemView.findViewById(R.id.product_details);
        productQuality = itemView.findViewById(R.id.product_condition);
        userName = itemView.findViewById(R.id.product_donor_name);
        userAddress = itemView.findViewById(R.id.product_donor_address);
        userPincode = itemView.findViewById(R.id.product_donor_pincode);
        userPhoneNumber = itemView.findViewById(R.id.product_donor_phone_number);
        userDate = itemView.findViewById(R.id.product_donor_date);
        productImage = itemView.findViewById(R.id.product_image);
        claimButton = itemView.findViewById(R.id.mark_claim_button);
        undoButton = itemView.findViewById(R.id.undo_claim_button);
        productCard = itemView.findViewById(R.id.product_card);
    }
}
