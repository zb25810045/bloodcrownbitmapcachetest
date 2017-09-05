package com.bloodcrown.bloodcrownbitmapcachetest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.DeadObjectException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private List<Integer> images;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getImages();
        image = (ImageView) findViewById(R.id.view_image);
        nextImage(image);
    }

    private List<Integer> getImages() {

        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(R.drawable.a);
        images.add(R.drawable.aa);
        images.add(R.drawable.aaa);
        images.add(R.drawable.aaaa);
        images.add(R.drawable.aaaaa);
        images.add(R.drawable.aaaaaa);

        return images;
    }

    public void nextImage(View view) {
        List<Integer> images = getImages();
        if (index == images.size() - 1) {
            index = 0;
        }
//        recycleBitmap(image);
        image.setImageResource(images.get(index));
        index++;
    }

    private void recycleBitmap(ImageView imageView) {

        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

}
