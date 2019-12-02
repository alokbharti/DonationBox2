package com.example.donationbox.ui.user_product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donationbox.R;
import com.example.donationbox.UtilFunctions;
import com.example.donationbox.ui.donate.Donor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProductFragment extends Fragment {

    private UserProductViewModel userProductViewModel;
    private RecyclerView recyclerView;
    private TextView userNote;
    private ProgressBar progressBar;
    private FirebaseRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userProductViewModel =
                ViewModelProviders.of(this).get(UserProductViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_product, container, false);

        String phoneNumber = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();

        userNote = root.findViewById(R.id.user_note_text);
        progressBar = root.findViewById(R.id.user_product_progressbar);
        recyclerView = root.findViewById(R.id.user_product_recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("Donor").orderByChild("donorPhoneNumber").equalTo(phoneNumber);

        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();

        adapter = new FirebaseRecyclerAdapter<Donor, UserProductViewHolder>(options){

            @Override
            public void onDataChanged() {
                Log.e("in", "onDataChanged");
                progressBar.setVisibility(View.GONE);
                userNote.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error", error.getDetails());
                Toast.makeText(getContext(), "Error loading data!!", Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public UserProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.user_product_list_item, parent, false);
                return new UserProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserProductViewHolder holder, int position, @NonNull Donor donor) {
                Log.e("in", "onBindView");
                userNote.setVisibility(View.GONE);
                holder.productDetails.setText(donor.getDonorProductDetails());
                holder.productTimestamp.setText(UtilFunctions.getDateFromTimeStamp(donor.getDonatedTimestamp()));
                holder.productQuality.setText(String.format("Product Quality: %s", donor.getDonorProductQuality()));
                holder.userAddress.setText(donor.getDonorAddress());
                holder.userName.setText(donor.getDonorName());
                holder.userPincode.setText(String.valueOf(donor.getDonorPincode()));
                holder.userPhoneNumber.setText(String.valueOf(donor.getDonorPhoneNumber()));
                Picasso.with(getContext()).load(donor.getDonorProductImageUrl()).into(holder.productImage);

                if(donor.getDonorProductIsClaimed()){
                    holder.userProductStatus.setText(String.format("Claimed by: %s", donor.getDonorProductClaimedBy()));
                } else{
                    holder.userProductStatus.setText("Not Claimed");
                }

                holder.deleteButton.setOnClickListener(v -> {
                    userProductViewModel.deleteProduct(donor).observe(getActivity(), isProductDeleted->{
                        if(!isProductDeleted){
                            Toast.makeText(getContext(), "Failed to delete this product!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        };

        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}