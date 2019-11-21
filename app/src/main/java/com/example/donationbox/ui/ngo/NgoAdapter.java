package com.example.donationbox.ui.ngo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;
import com.example.donationbox.ui.donate.Donor;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NgoAdapter extends RecyclerView.Adapter<NgoAdapter.ViewHolder> {

    private ArrayList<Donor> donorArrayList;
    private ProductClickListener productClickListener;
    private Context context;

    public NgoAdapter(ArrayList<Donor> donorArrayList, ProductClickListener productClickListener) {
        this.donorArrayList = donorArrayList;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ngo_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donor donor = donorArrayList.get(position);
        holder.productDetails.setText(donor.getDonorProductDetails());
        holder.productCategory.setText(donor.getDonorProductCategory());
        holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
        holder.userAddress.setText(donor.getDonorAddress());
        holder.userName.setText(donor.getDonorName());
        holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
        holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
        Picasso.with(context).load(donor.getDonorProductImageUrl()).into(holder.productImage);

        holder.claimButton.setOnClickListener(v -> {
            productClickListener.onClaimListener(donor);
            holder.claimButton.setEnabled(false);
            holder.claimButton.setClickable(false);
            holder.undoButton.setClickable(true);
            holder.undoButton.setEnabled(true);
        });

        holder.claimButton.setOnClickListener(v -> {
            productClickListener.onUndoClaimListener(donor);
            holder.undoButton.setClickable(false);
            holder.undoButton.setEnabled(false);
            holder.claimButton.setEnabled(true);
            holder.claimButton.setClickable(true);
        });

        if (donor.getDonorProductIsClaimed()){
            holder.claimButton.setEnabled(false);
            holder.claimButton.setClickable(false);
        } else{
            holder.undoButton.setClickable(false);
            holder.undoButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return donorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productDetails, userName, userAddress, userPincode, productCategory, productQuality, userPhoneNumber;
        private AppCompatImageView productImage;
        private AppCompatButton undoButton, claimButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productDetails = itemView.findViewById(R.id.product_details);
            productCategory = itemView.findViewById(R.id.product_category);
            productQuality = itemView.findViewById(R.id.product_condition);
            userName = itemView.findViewById(R.id.product_donor_name);
            userAddress = itemView.findViewById(R.id.product_donor_address);
            userPincode = itemView.findViewById(R.id.product_donor_pincode);
            userPhoneNumber = itemView.findViewById(R.id.product_donor_phone_number);
            productImage = itemView.findViewById(R.id.product_image);
            undoButton = itemView.findViewById(R.id.mark_claim_undo_button);
            claimButton = itemView.findViewById(R.id.mark_claim_button);
        }
    }
}
