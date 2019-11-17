package com.example.donationbox.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.donationbox.GlobalSettingsRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class PhoneAuthViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isDonorLoggedIn;
    private MutableLiveData<Boolean> isNGOLoggedIn;
    private MutableLiveData<Boolean> isPhoneAuthSuccessful;
    private PhoneRepository phoneRepository;

    private String verificationCode;
    
    public PhoneAuthViewModel(Application context){
        super(context);
        isDonorLoggedIn = new MutableLiveData<>();
        isNGOLoggedIn = new MutableLiveData<>();
        isPhoneAuthSuccessful = new MutableLiveData<>();
        setUserLoginStatus();
        phoneRepository = new PhoneRepository(getApplication());
    }

    private void setUserLoginStatus() {
        String userType = phoneRepository.getUserType();
        if (userType.length() == 0){
            isDonorLoggedIn.setValue(false) ;
            isNGOLoggedIn.setValue(false) ;
        }else{
            if (userType.equals("DONOR")){
                isDonorLoggedIn.setValue(true);
            } else {
                isNGOLoggedIn.setValue(false);
            }
        }
    }

    public LiveData<Boolean> getDonorLoginStatus(){
        return isDonorLoggedIn;
    }
    public LiveData<Boolean> getNGOLoginStatus(){
        return isNGOLoggedIn;
    }

    public LiveData<Boolean> getPhoneAuthSuccessStatus(){
        return isPhoneAuthSuccessful;
    }

    //manual code verification
    public void verifyCode(String code){
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInWithCredential(phoneAuthCredential);
    }

    public void sendCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //for auto-code detection
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            signInWithCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
        }
    };

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        phoneRepository.setUserType("DONOR");
                        isPhoneAuthSuccessful.setValue(true);
                    } else {
                        isPhoneAuthSuccessful.setValue(false);
                    }
                });
    }

    public boolean checkNgoCredential(String ngoId, String ngoPassword){
        String savedId = phoneRepository.getNgoId();
        String savedPassword = phoneRepository.getNgoPassword();
        if( !ngoId.equals(savedId) || !ngoPassword.equals(savedPassword)){
            return false;
        } else{
            phoneRepository.setUserType("NGO");
            return true;
        }
    }
}
