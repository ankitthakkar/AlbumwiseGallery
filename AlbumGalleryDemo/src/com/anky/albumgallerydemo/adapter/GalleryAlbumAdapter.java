package com.anky.albumgallerydemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anky.albumgallerydemo.R;
import com.anky.albumgallerydemo.model.GalleryPhotoAlbum;

public class GalleryAlbumAdapter extends CommonAdapter<GalleryPhotoAlbum> {
	private Context mContext;

	public GalleryAlbumAdapter(Context c) {
		mContext = c;
	}


	// Get max available VM memory, exceeding this amount will throw an
	// OutOfMemory exception. Stored in kilobytes as LruCache takes an
	// int in its constructor.

	private class ViewHolder {
		// ImageView imgThumb;
		private TextView albumName,albumCount;
		// MediaObject object;
		//private int position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			//holder.position = position;
			convertView = vi.inflate(R.layout.list_item_gallery_album, null);
			holder.albumName = (TextView) convertView
					.findViewById(R.id.list_gallery_album_tv_albumname);
			holder.albumCount = (TextView) convertView
					.findViewById(R.id.list_gallery_album_tv_count);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GalleryPhotoAlbum galleryPhotoAlbum =  getItem(position);
		holder.albumName.setText(galleryPhotoAlbum.getBucketName());
		holder.albumCount.setText(String.valueOf(galleryPhotoAlbum.getTotalCount()));
		convertView.setTag(holder);

		return convertView;
	}
}
