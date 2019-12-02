package com.example.donationbox;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFunctions {

    public static String getDateFromTimeStamp(long timeStamp){
        Date date = new Date(timeStamp);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
