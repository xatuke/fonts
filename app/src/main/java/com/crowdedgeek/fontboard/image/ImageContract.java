package com.crowdedgeek.fontboard.image;

import android.net.Uri;

public class ImageContract {
    public interface View {
        public void display(Uri uri);

        void onFailed(Exception e);
    }

    public interface Presenter {

    }
}
