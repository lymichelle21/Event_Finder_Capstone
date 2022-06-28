package com.example.event_finder_capstone;

import android.graphics.Bitmap;

public class BitmapScaler {
    public static Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }
}
