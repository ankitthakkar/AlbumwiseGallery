package com.anky.albumgallerydemo.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.anky.albumgallerydemo.R;
import com.anky.albumgallerydemo.adapter.GalleryAlbumAdapter;
import com.anky.albumgallerydemo.adapter.ImageAdapter;
import com.anky.albumgallerydemo.model.GalleryPhotoAlbum;
import com.anky.albumgallerydemo.model.MediaObject;
import com.anky.albumgallerydemo.utils.MediaType;
import com.anky.albumgallerydemo.utils.ProcessGalleryFile;
import com.anky.albumgallerydemo.utils.Utils;

public class MainAlbumActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private Button btnGalleryImage, btnGalleryVideo;
	private ViewFlipper viewFlipperGallery;
	private ArrayList<GalleryPhotoAlbum> arrayListAlbums;
	private GalleryAlbumAdapter galleryAlbumAdapter;
	private ListView lvPhotoAlbum;
	private boolean isImage = true;
	private Cursor mPhotoCursor = null;
	private Cursor mVideoCursor = null;
	private List<MediaObject> cursorData;
	private GridView mGridView = null;
	private ImageAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		btnGalleryImage = (Button) findViewById(R.id.fragment_create_gallery_btn_image);
		mGridView = (GridView) findViewById(R.id.gridview);
		btnGalleryVideo = (Button) findViewById(R.id.fragment_create_gallery_btn_video);

		viewFlipperGallery = (ViewFlipper) findViewById(R.id.fragment_create_gallery_flipper);
		lvPhotoAlbum = (ListView) findViewById(R.id.fragment_create_gallery_listview);

		lvPhotoAlbum.setOnItemClickListener(this);
		btnGalleryVideo.setOnClickListener(this);
		btnGalleryImage.setOnClickListener(this);
		mGridView.setOnItemClickListener(null);
		arrayListAlbums = new ArrayList<GalleryPhotoAlbum>();
		getPhotoList();
		
		btnGalleryImage.setBackgroundColor(getResources().getColor(R.color.orange));
		btnGalleryVideo.setBackgroundColor(getResources().getColor(R.color.blue));

	}

	/**
	 * retrieve image album and set
	 */
	private void getPhotoList() {

		// which image properties are we querying
		String[] PROJECTION_BUCKET = { ImageColumns.BUCKET_ID,
				ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATE_TAKEN,
				ImageColumns.DATA };
		// We want to order the albums by reverse chronological order. We abuse
		// the
		// "WHERE" parameter to insert a "GROUP BY" clause into the SQL
		// statement.
		// The template for "WHERE" parameter is like:
		// SELECT ... FROM ... WHERE (%s)
		// and we make it look like:
		// SELECT ... FROM ... WHERE (1) GROUP BY 1,(2)
		// The "(1)" means true. The "1,(2)" means the first two columns
		// specified
		// after SELECT. Note that because there is a ")" in the template, we
		// use
		// "(2" to match it.
		String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
		String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

		// Get the base URI for the People table in the Contacts content
		// provider.
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		Cursor cur = getContentResolver().query(images, PROJECTION_BUCKET,
				BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

		Log.v("ListingImages", " query count=" + cur.getCount());

		GalleryPhotoAlbum album;

		if (cur.moveToFirst()) {
			String bucket;
			String date;
			String data;
			long bucketId;

			int bucketColumn = cur
					.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

			int dateColumn = cur
					.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
			int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);

			int bucketIdColumn = cur
					.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

			do {
				// Get the field values
				bucket = cur.getString(bucketColumn);
				date = cur.getString(dateColumn);
				data = cur.getString(dataColumn);
				bucketId = cur.getInt(bucketIdColumn);

				if (bucket != null && bucket.length() > 0) {
					album = new GalleryPhotoAlbum();
					album.setBucketId(bucketId);
					album.setBucketName(bucket);
					album.setDateTaken(date);
					album.setData(data);
					album.setTotalCount(photoCountByAlbum(bucket));
					arrayListAlbums.add(album);
					// Do something with the values.
					Log.v("ListingImages", " bucket=" + bucket
							+ "  date_taken=" + date + "  _data=" + data
							+ " bucket_id=" + bucketId);
				}

			} while (cur.moveToNext());
		}
		cur.close();
		setData();

	}

	/**
	 * photo count find based on bucket name(album name)
	 * 
	 * @param bucketName
	 * @return
	 */
	private int photoCountByAlbum(String bucketName) {
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			String searchParams = null;
			String bucket = bucketName;
			searchParams = "bucket_display_name = \"" + bucket + "\"";

			// final String[] columns = { MediaStore.Images.Media.DATA,
			// MediaStore.Images.Media._ID };
			Cursor mPhotoCursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
					searchParams, null, orderBy + " DESC");

			if (mPhotoCursor.getCount() > 0) {
				return mPhotoCursor.getCount();
			}
			mPhotoCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	/**
	 * video count find based on bucket name(Album name)
	 * 
	 * @param bucketName
	 * @return
	 */
	private int videoCountByAlbum(String bucketName) {

		try {
			final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
			String searchParams = null;
			String bucket = bucketName;
			searchParams = "bucket_display_name = \"" + bucket + "\"";

			// final String[] columns = { MediaStore.Video.Media.DATA,
			// MediaStore.Video.Media._ID };
			Cursor mVideoCursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
					searchParams, null, orderBy + " DESC");

			if (mVideoCursor.getCount() > 0) {

				return mVideoCursor.getCount();
			}
			mVideoCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	/**
	 * retrieve video album list and set
	 */
	private void getVideoList() {

		// which image properties are we querying
		String[] PROJECTION_BUCKET = { VideoColumns.BUCKET_ID,
				VideoColumns.BUCKET_DISPLAY_NAME, VideoColumns.DATE_TAKEN,
				VideoColumns.DATA };
		// We want to order the albums by reverse chronological order. We abuse
		// the
		// "WHERE" parameter to insert a "GROUP BY" clause into the SQL
		// statement.
		// The template for "WHERE" parameter is like:
		// SELECT ... FROM ... WHERE (%s)
		// and we make it look like:
		// SELECT ... FROM ... WHERE (1) GROUP BY 1,(2)
		// The "(1)" means true. The "1,(2)" means the first two columns
		// specified
		// after SELECT. Note that because there is a ")" in the template, we
		// use
		// "(2" to match it.
		String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
		String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

		// Get the base URI for the People table in the Contacts content
		// provider.
		Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

		Cursor cur = getContentResolver().query(images, PROJECTION_BUCKET,
				BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

		Log.v("ListingImages", " query count=" + cur.getCount());

		GalleryPhotoAlbum album;

		if (cur.moveToFirst()) {
			String bucket;
			String date;
			String data;
			long bucketId;

			int bucketColumn = cur
					.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

			int dateColumn = cur
					.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
			int dataColumn = cur.getColumnIndex(MediaStore.Video.Media.DATA);

			int bucketIdColumn = cur
					.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);

			do {
				// Get the field values
				bucket = cur.getString(bucketColumn);
				date = cur.getString(dateColumn);
				data = cur.getString(dataColumn);
				bucketId = cur.getInt(bucketIdColumn);

				if (bucket != null && bucket.length() > 0) {
					album = new GalleryPhotoAlbum();
					album.setBucketId(bucketId);
					album.setBucketName(bucket);
					album.setDateTaken(date);
					album.setData(data);
					album.setTotalCount(videoCountByAlbum(bucket));
					arrayListAlbums.add(album);
					// Do something with the values.
					Log.v("ListingImages", " bucket=" + bucket
							+ "  date_taken=" + date + "  _data=" + data
							+ " bucket_id=" + bucketId);
				}

			} while (cur.moveToNext());
		}
		cur.close();
		

		setData();

	}

	/**
	 * album data set on list view
	 */
	private void setData() {
		if (arrayListAlbums.size() > 0) {
			if (galleryAlbumAdapter == null) {
				galleryAlbumAdapter = new GalleryAlbumAdapter(
						MainAlbumActivity.this);
			} else {
				galleryAlbumAdapter.notifyDataSetChanged();
			}
			// tvNoItem.setVisibility(View.GONE);
			galleryAlbumAdapter.setData(arrayListAlbums);
			lvPhotoAlbum.setAdapter(galleryAlbumAdapter);
		} 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		arrayListAlbums.clear();

		switch (v.getId()) {
		case R.id.fragment_create_gallery_btn_video:

			getVideoList();
			viewFlipperGallery.setDisplayedChild(0);
			isImage = false;
			btnGalleryVideo.setBackgroundColor(getResources().getColor(R.color.orange));
			btnGalleryImage.setBackgroundColor(getResources().getColor(R.color.blue));

			break;

		case R.id.fragment_create_gallery_btn_image:

			getPhotoList();
			viewFlipperGallery.setDisplayedChild(0);
			isImage = true;
			btnGalleryImage.setBackgroundColor(getResources().getColor(R.color.orange));
			btnGalleryVideo.setBackgroundColor(getResources().getColor(R.color.blue));
			
			
			break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		

		if (viewFlipperGallery.getDisplayedChild() == 0 && isImage) {
			initPhotoImages(arrayListAlbums.get(arg2).getBucketName());
		} else {
			initVideoImages(arrayListAlbums.get(arg2).getBucketName());
		}
		
		viewFlipperGallery.showNext();
	}

	Set<ProcessGalleryFile> tasks = new HashSet<ProcessGalleryFile>();
	Runnable runnable1 = new Runnable() {
		@Override
		public void run() {

			if (!cursorData.isEmpty()) {
				Log.v("cursorData.size", String.valueOf(cursorData.size()));
				mAdapter = new ImageAdapter(MainAlbumActivity.this);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						mAdapter.setData(cursorData);
						mGridView
								.setOnScrollListener(new AbsListView.OnScrollListener() {

									public void onScroll(AbsListView view,
											int firstVisibleItem,
											int visibleItemCount,
											int totalItemCount) {
									}

									public void onScrollStateChanged(
											AbsListView view, int scrollState) {
										if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
											mAdapter.setFirstTime(false);
											int count = view.getChildCount();

											for (int i = 0; i < count; i++) {
												View convertView = view
														.getChildAt(i);
												ImageAdapter.ViewHolder holder = (ImageAdapter.ViewHolder) convertView
														.getTag();
												if (holder == null)
													return;
												final ProcessGalleryFile processGalleryFile = new ProcessGalleryFile(
														holder.imgThumb,
														holder.videoDuration,
														holder.object.getPath(),
														holder.object
																.getMediaType());
												if (tasks == null) {
													tasks = new HashSet<ProcessGalleryFile>();
												}
												if (!tasks
														.contains(processGalleryFile)) {
													try {
														processGalleryFile
																.execute();
														tasks.add(processGalleryFile);
													} catch (Exception ignored) {
													}
												}
											}
										} else {
											try {
												cancelAll();
											} catch (Exception ignored) {
											}
										}
									}

									public void cancelAll() throws Exception {
										final Iterator<ProcessGalleryFile> iterator = tasks
												.iterator();
										while (iterator.hasNext()) {
											iterator.next().cancel(true);
											iterator.remove();
										}
									}
								});

						mGridView.setAdapter(mAdapter);

						mGridView
								.setOnItemClickListener(new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(
											AdapterView<?> adapterView,
											View view, int i, long l) {

									}
								});
					}
				});
			}
		}

	};

	/**
	 * find image list for given bucket name
	 * 
	 * @param bucketName
	 */
	private void initPhotoImages(String bucketName) {
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			String searchParams = null;
			String bucket = bucketName;
			searchParams = "bucket_display_name = \"" + bucket + "\"";

			// final String[] columns = { MediaStore.Images.Media.DATA,
			// MediaStore.Images.Media._ID };
			mPhotoCursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
					searchParams, null, orderBy + " DESC");

			if (mPhotoCursor.getCount() > 0) {

				cursorData = new ArrayList<MediaObject>();

				cursorData.addAll(Utils.extractMediaList(mPhotoCursor,
						MediaType.PHOTO));

				new Thread(runnable1).start();

			}

			// setAdapter(mImageCursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * find video list for given bucket name
	 * 
	 * @param bucketName
	 */
	private void initVideoImages(String bucketName) {
		try {
			final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
			String searchParams = null;
			String bucket = bucketName;
			searchParams = "bucket_display_name = \"" + bucket + "\"";

			
			mVideoCursor = getContentResolver().query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
					searchParams, null, orderBy + " DESC");

			if (mVideoCursor.getCount() > 0) {

				
				cursorData = new ArrayList<MediaObject>();
			

				cursorData.addAll(Utils.extractMediaList(mVideoCursor,
						MediaType.VIDEO));

				new Thread(runnable1).start();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		if (viewFlipperGallery.getDisplayedChild() == 1) {
			viewFlipperGallery.setDisplayedChild(0);
		} else {
			finish();
		}
	}

}
