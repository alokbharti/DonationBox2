package com.example.donationbox;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFunctions {

    public static String getDateFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static void displaySnackBar(View parentView, String message){
        Snackbar snack = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
}
