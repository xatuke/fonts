package com.crowdedgeek.fontboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.crowdedgeek.fontboard.image.converter.AsciiConverter;
import com.crowdedgeek.fontboard.image.converter.ShareUtil;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;

import static com.crowdedgeek.fontboard.image.converter.ProcessImageOperation.processImage;

public class ASCIIActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1231;
    private static final String TAG = "ImageToAsciiFragment";
    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_PERMISSION = 1002;
    private PhotoView mPreview;
    private ProgressBar mProgressBar;
    private Spinner mSpinnerType;
    private File mResultFile = null;
    private Uri mOriginalUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii);
        getSupportActionBar().hide();
        mPreview = findViewById(R.id.image_preview);
        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCurrentImage();
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOriginalUri != null) {
                    convertImageToAsciiFromIntent(mOriginalUri);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button select = findViewById(R.id.btn_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Button share = findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCurrentImage();
            }
        });
        if(!permissionGrated()) {
            requestPermissions();
        }

    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } catch (Exception ignored) {
        }
    }

    private boolean permissionGrated() {
        int state;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            state = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (state == PackageManager.PERMISSION_GRANTED) {
                state = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (state != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ASCIIActivity.this, "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionGrated()) {
            selectImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (intent != null && intent.getData() != null) {
                        mOriginalUri = intent.getData();
                        mPreview.setImageBitmap(null);
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        mOriginalUri = null;
                        mResultFile = null;
                        mPreview.setImageBitmap(null);
                    }
                }
                break;
            case TAKE_PICTURE:
                this.mOriginalUri = intent.getData();
                if (resultCode == RESULT_OK) {
                    if (intent.getData() != null) {
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        Toast.makeText(ASCIIActivity.this, "Capture failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }


    private void convertImageToAsciiFromIntent(Uri uri) {
        this.mResultFile = null;
        new TaskConvertImageToAscii(ASCIIActivity.this, getCurrentType()).execute(uri);
    }

    private AsciiConverter.ColorType getCurrentType() {
        switch (mSpinnerType.getSelectedItemPosition()) {
            case 0:
                return AsciiConverter.ColorType.ANSI_COLOR;
            case 1:
                return AsciiConverter.ColorType.FULL_COLOR;
            case 2:
                return AsciiConverter.ColorType.NONE;
            default:
                return AsciiConverter.ColorType.ANSI_COLOR;
        }
    }

    private void saveImage() {
        if (mResultFile != null) {
            addImageToGallery(mResultFile.getPath());
        } else {
            Toast.makeText(ASCIIActivity.this, "Null URI", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareCurrentImage() {
        if (mResultFile == null) {
            Toast.makeText(ASCIIActivity.this, "Null Uri", Toast.LENGTH_SHORT).show();
        } else {
            ShareUtil.shareImage(ASCIIActivity.this, mResultFile);
        }
    }

    public void addImageToGallery(final String filePath) {
        try {
            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "images/png");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            ASCIIActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Toast.makeText(ASCIIActivity.this, "Saved in gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ASCIIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private class TaskConvertImageToAscii extends AsyncTask<Uri, Void, File> {
        private Context context;
        private AsciiConverter.ColorType type;

        TaskConvertImageToAscii(Context context, AsciiConverter.ColorType type) {
            this.context = context;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected File doInBackground(Uri... params) {
            try {
                String output = processImage(context, params[0], type);
                if (output != null) {
                    return new File(output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final File uri) {
            super.onPostExecute(uri);
            if (uri == null) {
                Toast.makeText(context, "IO Exception", Toast.LENGTH_SHORT).show();
            } else {
                mPreview.setImageURI(Uri.fromFile(uri));
                mResultFile = uri;
            }
            mProgressBar.setVisibility(View.GONE);
        }

    }
}
