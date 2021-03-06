package com.alokbharti.donationbox.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.alokbharti.donationbox.R;

public class ProductHolder extends RecyclerView.ViewHolder {

    public TextView productDetails, productClaimStatus, userName, productQuality;
    public AppCompatImageView productImage;

    public ProductHolder(@NonNull View itemView) {
        super(itemView);

        productDetails = itemView.findViewById(R.id.donated_product_details);
        productQuality = itemView.findViewById(R.id.donated_product_condition);
        userName = itemView.findViewById(R.id.donated_product_donor_name);
        productImage = itemView.findViewById(R.id.donated_product_image);
        productClaimStatus = itemView.findViewById(R.id.donated_product_claim_status);
    }

}
