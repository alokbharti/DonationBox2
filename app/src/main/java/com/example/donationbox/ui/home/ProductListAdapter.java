package com.example.donationbox.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;
import com.example.donationbox.ui.donate.Donor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ArrayList<Donor> productArrayList;
    private Context context;

    public ProductListAdapter(ArrayList<Donor> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donate_product_list, parent, false);
        return new ProductListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donor donor = productArrayList.get(position);
        holder.productDetails.setText(donor.getDonorProductDetails());
        holder.productCategory.setText(donor.getDonorProductCategory());
        holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
        holder.userName.setText(donor.getDonorName());
        Picasso.with(context).load(donor.getDonorProductImageUrl()).into(holder.productImage);

        if(donor.getDonorProductIsClaimed()){
            holder.productClaimStatus.setText(String.format("Claimed by: %s", donor.getDonorProductClaimedBy()));
        } else {
            holder.productClaimStatus.setText("Not Claimed");
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productDetails, productClaimStatus, userName, productCategory, productQuality;
        private AppCompatImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productDetails = itemView.findViewById(R.id.donated_product_details);
            productCategory = itemView.findViewById(R.id.donated_product_category);
            productQuality = itemView.findViewById(R.id.donated_product_condition);
            userName = itemView.findViewById(R.id.donated_product_donor_name);
            productImage = itemView.findViewById(R.id.donated_product_image);
            productClaimStatus = itemView.findViewById(R.id.donated_product_claim_status);
        }
    }
}
