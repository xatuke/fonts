package com.crowdedgeek.fontboard.utils;

import android.graphics.Bitmap;

import net.glxn.qrgen.android.QRCode;

public class QRGenerator {
    public Bitmap makeQR(String input){
        return QRCode.from(input).bitmap();
    }
}
