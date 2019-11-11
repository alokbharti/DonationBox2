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
    
    private MutableLiveData<String> isUserLogged;
    private MutableLiveData<Boolean> isPhoneAuthSuccessful;

    private String verificationCode;
    
    public PhoneAuthViewModel(Application context){
        super(context);
        isUserLogged = new MutableLiveData<>();
        isPhoneAuthSuccessful = new MutableLiveData<>();
        setUserLoginStatus();
    }

    private void setUserLoginStatus() {
        String userType = GlobalSettingsRepository.getUserType(getApplication());
        if (userType.length() == 0){
            isUserLogged.setValue("");
        }else{
            isUserLogged.setValue(userType);
        }
    }

    public LiveData<String> getUserLoginStatus(){
        return isUserLogged;
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
                        GlobalSettingsRepository.setUserType(getApplication(), "DONOR");
                        isPhoneAuthSuccessful.setValue(true);
                    } else {
                        isPhoneAuthSuccessful.setValue(false);
                    }
                });
    }
}
