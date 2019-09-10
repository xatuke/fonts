package com.crowdedgeek.fontboard.image.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Writes bitmaps and HTML to directories on the external storage directory.
 */
public class AsciiImageWriter {

    private static final DateFormat filenameDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);

    static {
        File sdCard = Environment.getExternalStorageDirectory();
    }

    public static String saveImage(Context context, Bitmap image)
            throws IOException {
        String datestr = filenameDateFormat.format(new Date());
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File imageFile;
        if (isSDPresent) {
            imageFile = new File(FileUtil.getImageDirectory(context), datestr + ".png");
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
                imageFile.createNewFile();
            }
        } else {
            imageFile = new File(context.getFilesDir(), datestr + ".png");
        }
        saveBitmap(image, imageFile);
        return imageFile.getPath();
    }

    public static boolean saveBitmap(@NonNull Bitmap bitmap, @NonNull File fileToWrite) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (Exception e) {
            return false;
        } finally {
            if (output != null) output.close();
        }
        return true;
    }
}
