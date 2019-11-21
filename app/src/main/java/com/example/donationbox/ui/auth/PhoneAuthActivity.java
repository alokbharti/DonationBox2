package com.example.donationbox.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donationbox.MainActivity;
import com.example.donationbox.R;
import com.example.donationbox.ui.ngo.NgoActivity;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText phoneNumberEt, ngoIdEt, ngoPasswordEt, verificationEt;
    private LinearLayout ngoLoginLayout, phoneLoginLayout, verificationLayout;
    private Button ngoLoginButton, phoneLoginButton, verificationButton;
    private TextView ngoLoginTv, donorLoginTv;
    private PhoneAuthViewModel phoneAuthViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        initViews();
        phoneAuthViewModel = ViewModelProviders.of(this).get(PhoneAuthViewModel.class);
        phoneAuthViewModel.getDonorLoginStatus().observe(this, isDonor ->{
            if (isDonor){
                Log.e("User", "Donor");
                startActivity(new Intent(this, MainActivity.class));
            } else{
                phoneLoginLayout.setVisibility(View.VISIBLE);
            }
        });
        phoneAuthViewModel.getNGOLoginStatus().observe(this, isNGO ->{
            if (isNGO){
                startActivity(new Intent(this, NgoActivity.class));
            } else{
                phoneLoginLayout.setVisibility(View.VISIBLE);
            }
        });

        phoneAuthViewModel.getPhoneAuthSuccessStatus().observe(this, status ->{
            if(status){
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(this, MainActivity.class));
            } else{

            }
        });
    }

    private void initViews() {

        phoneNumberEt = findViewById(R.id.phone_number_et);
        ngoIdEt = findViewById(R.id.ngo_id_et);
        ngoPasswordEt = findViewById(R.id.ngo_password_et);
        verificationEt = findViewById(R.id.verification_code_et);

        ngoLoginLayout = findViewById(R.id.ngo_login_layout);
        phoneLoginLayout = findViewById(R.id.normal_login_layout);
        verificationLayout = findViewById(R.id.verification_layout);

        ngoLoginButton = findViewById(R.id.ngo_login_button);
        phoneLoginButton = findViewById(R.id.login_button);
        verificationButton = findViewById(R.id.verification_button);

        ngoLoginTv = findViewById(R.id.ngo_login_tv);
        donorLoginTv = findViewById(R.id.donor_login_tv);

        progressBar = findViewById(R.id.progressbar);

        ngoLoginTv.setOnClickListener(v->{
            phoneLoginLayout.setVisibility(View.GONE);
            ngoLoginLayout.setVisibility(View.VISIBLE);
        });
        donorLoginTv.setOnClickListener(v->{
            ngoLoginLayout.setVisibility(View.GONE);
            phoneLoginLayout.setVisibility(View.VISIBLE);
        });

        ngoLoginButton.setOnClickListener(v->{
            String ngoId = ngoIdEt.getText().toString();
            String ngoPassword = ngoPasswordEt.getText().toString();

            if(TextUtils.isEmpty(ngoId) || TextUtils.isEmpty(ngoPassword)){
                Toast.makeText(this, "All fields are necessary!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!phoneAuthViewModel.checkNgoCredential(ngoId, ngoPassword)){
                Toast.makeText(this, "wrong credentials!!", Toast.LENGTH_SHORT).show();
                return;
            } else{
                startActivity(new Intent(this, NgoActivity.class));
            }

        });

        phoneLoginButton.setOnClickListener(v->{
            String phoneNumber = phoneNumberEt.getText().toString();

            if(phoneNumber.length() < 10){
                phoneNumberEt.setError("Phone number is wrong!!");
                return;
            }

            phoneLoginLayout.setVisibility(View.GONE);
            ngoLoginLayout.setVisibility(View.GONE);
            verificationLayout.setVisibility(View.VISIBLE);

            sendVerificationCode("+91"+phoneNumber);
        });

        verificationButton.setOnClickListener( v-> {
            String code = verificationEt.getText().toString();
            if (code.isEmpty() || code.length()<6){
                verificationEt.setError("wrong code!!");
                verificationEt.requestFocus();
                return;
            }

            phoneAuthViewModel.verifyCode(code);
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        phoneAuthViewModel.sendCode(phoneNumber);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
