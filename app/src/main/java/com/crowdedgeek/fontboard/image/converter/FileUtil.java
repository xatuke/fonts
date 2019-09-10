package com.crowdedgeek.fontboard.image.converter;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class FileUtil {
    public static final Pattern PATTERN_SPLIT = Pattern.compile("(\")(.*?)(\")", Pattern.DOTALL);
    private static final String TAG = "FileUtil";
    private static final String IMAGE_FOLDER_NAME = "Image";


    public static File getImageDirectory(Context context) {
        if (hasSdCard(context)) {
            File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FOLDER_NAME);
            if (!file.exists()) file.mkdir();
            return file;
        }
        File file = new File(context.getFilesDir(), IMAGE_FOLDER_NAME);
        if (!file.exists()) file.mkdir();
        return file;
    }

    private static boolean hasSdCard(Context context) {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String streamToString(@NonNull InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    private static byte[] streamToByteArray(@NonNull InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

}