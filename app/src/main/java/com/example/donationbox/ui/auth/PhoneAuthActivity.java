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

import com.example.donationbox.InternetConnectionLiveData;
import com.example.donationbox.MainActivity;
import com.example.donationbox.R;
import com.example.donationbox.UtilFunctions;
import com.example.donationbox.ui.ngo.NgoActivity;

import java.util.Timer;
import java.util.TimerTask;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText phoneNumberEt, ngoIdEt, ngoPasswordEt;
    private LinearLayout ngoLoginLayout, phoneLoginLayout, verificationLayout;
    private Button ngoLoginButton, phoneLoginButton, resendCodeButton;
    private TextView ngoLoginTv, donorLoginTv, timerTv;
    private PhoneAuthViewModel phoneAuthViewModel;
    private ProgressBar progressBar;
    private String phoneNumber;

    private Timer timer;
    private int currentLoginState =0; //0 is for phone number layout and 1 for code verification layout
    private boolean isFirsTimeInternetCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        initViews();
        phoneAuthViewModel = ViewModelProviders.of(this).get(PhoneAuthViewModel.class);
        phoneAuthViewModel.getDonorLoginStatus().observe(this, isDonor ->{
            if (isDonor){
                Log.e("User", "Donor");
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else{
                phoneLoginLayout.setVisibility(View.VISIBLE);
            }
        });
        phoneAuthViewModel.getNGOLoginStatus().observe(this, isNGO ->{
            if (isNGO){
                Intent intent = new Intent(this, NgoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else{
                phoneLoginLayout.setVisibility(View.VISIBLE);
            }
        });

        phoneAuthViewModel.getPhoneAuthSuccessStatus().observe(this, status ->{
            if(status){
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else{

            }
        });

        observeInternetStatusChanges();
    }

    private void initViews() {

        phoneNumberEt = findViewById(R.id.phone_number_et);
        ngoIdEt = findViewById(R.id.ngo_id_et);
        ngoPasswordEt = findViewById(R.id.ngo_password_et);

        ngoLoginLayout = findViewById(R.id.ngo_login_layout);
        phoneLoginLayout = findViewById(R.id.normal_login_layout);
        verificationLayout = findViewById(R.id.verification_layout);

        ngoLoginButton = findViewById(R.id.ngo_login_button);
        phoneLoginButton = findViewById(R.id.login_button);
        resendCodeButton = findViewById(R.id.resend_code_button);

        ngoLoginTv = findViewById(R.id.ngo_login_tv);
        donorLoginTv = findViewById(R.id.donor_login_tv);
        timerTv = findViewById(R.id.timer_text);

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
                Intent intent = new Intent(this, NgoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });

        phoneLoginButton.setOnClickListener(v->{
            currentLoginState = 1;
            phoneNumber = phoneNumberEt.getText().toString();

            if(phoneNumber.length() < 10){
                phoneNumberEt.setError("Phone number is wrong!!");
                return;
            } else if (!UtilFunctions.isOnline(this)){
                Toast.makeText(this, getResources().getText(R.string.offline_message), Toast.LENGTH_SHORT).show();
                return;
            }

            phoneLoginLayout.setVisibility(View.GONE);
            ngoLoginLayout.setVisibility(View.GONE);
            verificationLayout.setVisibility(View.VISIBLE);
            timerTv.setVisibility(View.VISIBLE);
            resendCodeButton.setVisibility(View.GONE);

            sendVerificationCode("+91"+phoneNumber);
            setTimer();

        });

        resendCodeButton.setOnClickListener( v-> {
            sendVerificationCode("+91"+phoneNumber);
            resendCodeButton.setVisibility(View.GONE);
            setTimer();
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        phoneAuthViewModel.sendCode(phoneNumber);
    }

    private void setTimer(){
        final int[] counter = {0};
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                counter[0]++;
                runOnUiThread(() -> {
                    int seconds = counter[0] % 60;
                    int minutes = counter[0] / 60;
                    String sec= String.valueOf(seconds);
                    String min = "0"+ minutes;
                    if(seconds<10){
                        sec = "0"+sec;
                    }
                    timerTv.setText(String.format("Auto verifying your code %s:%s", min, sec));
                    if(counter[0]==120){
                        this.cancel();
                        progressBar.setVisibility(View.GONE);
                        resendCodeButton.setVisibility(View.VISIBLE);
                        timerTv.setText("Unable to get verification code");
                        Toast.makeText(PhoneAuthActivity.this, "Please make sure you've an active internet connection or check your mobile number!!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirsTimeInternetCheck = true;
    }

    @Override
    public void onBackPressed() {
        if (currentLoginState == 1){
            if (timer!=null) timer.cancel();
            verificationLayout.setVisibility(View.GONE);
            phoneLoginLayout.setVisibility(View.VISIBLE);
            currentLoginState = 0;
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    private void observeInternetStatusChanges() {
        new InternetConnectionLiveData(this).observe(this, isInternetConnected ->{
            if (!isInternetConnected){
                UtilFunctions.displaySnackBar(this, findViewById(R.id.auth_ll), "You're offline, check your internet!!",0);
            } else {
                if (!isFirsTimeInternetCheck) UtilFunctions.displaySnackBar(this, findViewById(R.id.auth_ll), "We're back again",0);
                else isFirsTimeInternetCheck = false;
            }
        });
    }
}
