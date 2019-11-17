package com.example.donationbox.ui.auth;

import android.content.Context;

import com.example.donationbox.GlobalSettingsRepository;

public class PhoneRepository {

    private Context context;

    public PhoneRepository(Context context) {
        this.context = context;
    }

    public String getUserType(){
        return GlobalSettingsRepository.getUserType(context);
    }

    public void setUserType(String userType){
        GlobalSettingsRepository.setUserType(context, userType);
    }

    public String getNgoId() {
        return GlobalSettingsRepository.getNgoId(context);
    }

    public String getNgoPassword() {
        return GlobalSettingsRepository.getNgoPassword(context);
    }
}
