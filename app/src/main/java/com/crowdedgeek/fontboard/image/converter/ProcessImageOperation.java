package com.crowdedgeek.fontboard.image.converter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.io.IOException;

public class ProcessImageOperation {
    private static final String TAG = "ProcessImageOperation";

    /**
     * Reads the image from the given URI, creates ASCII PNG and HTML files, and writes them to
     * a new directory under the AsciiCam directory in /sdcard. Returns the path to the PNG file.
     */
    @Nullable
    public static String processImage(Context context, Uri uri,
                                      @Nullable AsciiConverter.ColorType type) throws IOException {
        Log.d(TAG, "processImage() called with: context = [" + context + "], uri = [" + uri + "]");

        AsciiConverter.ColorType colorType;
        if (type == null)
            colorType = AsciiConverter.ColorType.NONE;
        else {
            colorType = type;
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // assume width is always larger
        int displayWidth = Math.max(display.getWidth(), display.getHeight());
        int displayHeight = Math.min(display.getWidth(), display.getHeight());

        final AsciiRenderer renderer = new AsciiRenderer();
        renderer.setMaximumImageSize(displayWidth, displayHeight);

        int minWidth = Math.max(2 * renderer.asciiColumns(), 480);
        int minHeight = Math.max(2 * renderer.asciiRows(), 320);

        Bitmap bitmap = AndroidUtils.scaledBitmapFromURIWithMinimumSize(context, uri, minWidth, minHeight);
        if (bitmap == null) {
            return null;
        }
        renderer.setCameraImageSize(bitmap.getWidth(), bitmap.getHeight());
        renderer.setTextSize(12);

        AsciiConverter converter = new AsciiConverter();
        final AsciiConverter.Result result = converter.computeResultForBitmap(bitmap,
                renderer.asciiRows(), renderer.asciiColumns(), colorType);

        String path = AsciiImageWriter.saveImage(context, renderer.createBitmap(result));
        if (!bitmap.isRecycled()) bitmap.recycle();
        return path;
    }
}
