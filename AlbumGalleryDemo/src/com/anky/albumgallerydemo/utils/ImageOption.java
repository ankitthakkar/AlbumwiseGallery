package com.anky.albumgallerydemo.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public enum ImageOption {
    
  
    GALLERY_OPTIONS(new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisc(false)
            .cacheInMemory(true)
            .build()
    );


    public DisplayImageOptions getDisplayImageOptions() {
        return displayImageOptions;
    }

    private DisplayImageOptions displayImageOptions;

    ImageOption(DisplayImageOptions displayImageOptions) {

        this.displayImageOptions = displayImageOptions;
    }

}