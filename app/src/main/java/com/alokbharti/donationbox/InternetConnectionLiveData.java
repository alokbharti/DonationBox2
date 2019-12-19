package com.alokbharti.donationbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;

public class InternetConnectionLiveData extends LiveData<Boolean> {

    private Context context;
    public InternetConnectionLiveData(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(internetStatus, filter);
    }


    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(internetStatus);
    }


    private BroadcastReceiver internetStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                postValue(!noConnectivity);
            }
        }
    };
}
