package com.example.donationbox.ui.auth;

import android.content.Context;

import com.example.donationbox.Constants;
import com.example.donationbox.GlobalSettingsRepository;

import java.util.HashMap;

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

    public String getNgoId(String ngoId) {
        if (Constants.ngoCred.containsKey(ngoId)) {
            return ngoId;
        }
        return null;
    }

    public void setNgoId(String ngoId){
        GlobalSettingsRepository.setNgoId(context, ngoId);
    }

    public String getNgoPassword() {
        return GlobalSettingsRepository.getNgoPassword(context);
    }
}
