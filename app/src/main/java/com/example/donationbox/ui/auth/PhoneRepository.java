package com.example.donationbox.ui.auth;

import android.content.Context;

import com.example.donationbox.GlobalSettingsRepository;

import java.util.HashMap;

public class PhoneRepository {

    private Context context;
    private HashMap<String, String> ngoCred;

    public PhoneRepository(Context context) {
        this.context = context;
        ngoCred = new HashMap<>();
        ngoCred.put("jagriti", "1234");
        ngoCred.put("rdfindia@12", "1234");
        ngoCred.put("katha@12", "1234");
        ngoCred.put("goonj@12", "1234");
        ngoCred.put("kiss@12", "1234");
        ngoCred.put("cini@12", "1234");
        ngoCred.put("resfo@12", "1234");

    }

    public String getUserType(){
        return GlobalSettingsRepository.getUserType(context);
    }

    public void setUserType(String userType){
        GlobalSettingsRepository.setUserType(context, userType);
    }

    public String getNgoId(String ngoId) {
        if (ngoCred.containsKey(ngoId)) {
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
