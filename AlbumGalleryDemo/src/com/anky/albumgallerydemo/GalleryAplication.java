package com.anky.albumgallerydemo;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class GalleryAplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader();
	}

	public void initImageLoader() {
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// .imageDownloader(DownloadModule.getCustomImageDownaloder(this))
				// .discCacheFileNameGenerator(new FileNameGenerator() {
				// @Override
				// public String generate(String imageUri) {
				// return Utils.getNostraImageFileName(imageUri);
				// }
				// })
				.memoryCache(new WeakMemoryCache());

		ImageLoader.getInstance().init(builder.build());
	}

}
