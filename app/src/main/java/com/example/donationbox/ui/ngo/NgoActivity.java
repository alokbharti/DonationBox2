package com.example.donationbox.ui.ngo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.donationbox.R;

public class NgoActivity extends AppCompatActivity {

    private NgoViewModel ngoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);
    }
}
