package com.example.donationbox.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.donationbox.GlobalSettingsRepository;
import com.example.donationbox.R;
import com.example.donationbox.ui.auth.PhoneAuthActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        root.findViewById(R.id.sign_out_btn).setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            GlobalSettingsRepository.setUserType(getContext(),"");
            startActivity(new Intent(getActivity(), PhoneAuthActivity.class));
        });

        return root;
    }
}