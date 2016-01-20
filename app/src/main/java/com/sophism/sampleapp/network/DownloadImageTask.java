package com.sophism.sampleapp.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    LruCache cache;
    boolean isRounded;
    public DownloadImageTask(ImageView bmImage, LruCache cache) {
        this.bmImage = bmImage;
        this.cache = cache;
        isRounded = false;
    }
    public DownloadImageTask(ImageView bmImage, LruCache cache, boolean isRounded) {
        this.bmImage = bmImage;
        this.cache = cache;
        this.isRounded = isRounded;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon1 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon1 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (urldisplay != null & mIcon1 != null)
            cache.put(urldisplay,mIcon1);
        return mIcon1;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);

    }
}
