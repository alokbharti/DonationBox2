package com.example.donationbox.ui.donate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.donationbox.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class DonateFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 11;
    private DonateViewModel donateViewModel;
    private ImageView productImageView;
    private TextInputEditText userNameEt, userAddressEt, userPincodeEt, productDetailsEt;
    private AutoCompleteTextView productQualityAt, productCategoryAt, uploadProgressAt;
    private AppCompatButton donateButton;
    private ProgressBar progressBar;

    private String imageDownloadUrl = "";
    private int uploadProgress =0;
    private Uri localImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        donateViewModel =
                ViewModelProviders.of(this).get(DonateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donor, container, false);

        initView(root);

        donateViewModel.getImageDownLoadUrl().observe(this, imageLink ->{
            imageDownloadUrl = imageLink;
        });
        donateViewModel.getImageUploadProgress().observe(this, progress -> {
            uploadProgress = progress;
            uploadProgressAt.setText(String.format("%s %%", String.valueOf(progress)));
            if(progress >=100){
                progressBar.setVisibility(View.INVISIBLE);
                uploadProgressAt.setVisibility(View.GONE);
                final InputStream imageStream;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(localImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    productImageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    private void initView(View root) {
        productImageView = root.findViewById(R.id.donor_product_image);
        userNameEt = root.findViewById(R.id.donor_name_et);
        userAddressEt = root.findViewById(R.id.donor_address_et);
        userPincodeEt = root.findViewById(R.id.donor_pincode_et);
        productDetailsEt = root.findViewById(R.id.donor_product_details_et);
        productCategoryAt = root.findViewById(R.id.donor_product_category);
        productQualityAt = root.findViewById(R.id.donor_product_quality);
        donateButton = root.findViewById(R.id.donate_button);
        progressBar = root.findViewById(R.id.progressbar);
        uploadProgressAt = root.findViewById(R.id.progress_tv);

        String [] productCategories = new String [] {"Book", "Cloth", "Food", "Other"};
        String [] productQualities = new String [] {"Very Good", "Good", "Bad", "Very Bad"};
        ArrayAdapter<String> categoryAdap = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1 , productCategories);
        ArrayAdapter<String> qualityAdap = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1 , productQualities);

        productCategoryAt.setAdapter(categoryAdap);
        productQualityAt.setAdapter(qualityAdap);

        productImageView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        donateButton.setOnClickListener(view -> {
            if(uploadProgress<100){
                Toast.makeText(getActivity(), "Image is not uploaded yet!!", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = userNameEt.getText().toString();
            String address = userAddressEt.getText().toString();
            String pincode = userPincodeEt.getText().toString();
            String productDetails = productDetailsEt.getText().toString();
            String productCategory = productCategoryAt.getText().toString();
            String productQuality = productQualityAt.getText().toString();
            String phnoeNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

            if (name.length()==0 || address.length()==0 || pincode.length()==0 || productDetails.length()==0
                    || productCategory.length()==0 || productQuality.length()==0){
                Toast.makeText(getActivity(), "Fill all the fields!!", Toast.LENGTH_SHORT).show();
                return;
            }

            Donor donor = new Donor(name, address, productDetails, productCategory, productQuality,
                    imageDownloadUrl, "", phnoeNumber,
                    Integer.parseInt(pincode), System.currentTimeMillis(), false);

            Log.e("Donor details", ""+name+" "+address+" "+ productDetails+" "+ productCategory+" "+ productQuality+" "+ imageDownloadUrl+" "+ phnoeNumber);

            donateViewModel.saveDataToDatabase(donor);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            if (data != null) {
                localImageUri = data.getData();
                donateViewModel.uploadAndGetImageUrl(localImageUri);
                progressBar.setVisibility(View.VISIBLE);
                uploadProgressAt.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "File Not Selected!!", Toast.LENGTH_SHORT).show();
                donateButton.setEnabled(false);
                donateButton.setClickable(false);
            }
        }
    }
}