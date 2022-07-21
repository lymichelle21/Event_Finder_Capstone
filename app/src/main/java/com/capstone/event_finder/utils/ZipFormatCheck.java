package com.capstone.event_finder.utils;

import android.content.Context;
import android.widget.Toast;

public class ZipFormatCheck {
    public static Boolean isZipValidFormat(String zip, Context context) {
        String zipPattern = "^\\d{5}$";
        if (!zip.matches(zipPattern)) {
            Toast.makeText(context, "Invalid zip", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
