package com.alokbharti.donationbox.ui.auth;

import android.content.Context;

import com.alokbharti.donationbox.Constants;
import com.alokbharti.donationbox.GlobalSettingsRepository;

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
