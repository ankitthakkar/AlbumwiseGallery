package com.anky.albumgallerydemo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.anky.albumgallerydemo.model.MediaObject;

public class Utils {

	public static List<MediaObject> extractMediaList(Cursor cursor,
			MediaType mediaType) {
		List<MediaObject> list = new ArrayList<MediaObject>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String filePath = cursor.getString(1);
				long creationDate = getCreationDate(filePath);

				MediaObject mediaObject = new MediaObject(cursor.getInt(0),
						filePath, mediaType, creationDate);
				list.add(mediaObject);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return list;
	}

	private static long getCreationDate(String filePath) {
		File file = new File(filePath);
		return file.lastModified();
	}
	
	
	 public static String getDurationMark(String filePath, MediaMetadataRetriever retriever) {
	        try {
	            retriever.setDataSource(filePath);
	        } catch (Exception e) {
	            Log.e("getDurationMark", e.toString());
	            return "?:??";
	        }
	        String time = null;

	        //fix for the gallery picker crash
	        // if it couldn't detect the media file
	        try {
	            Log.e("file", filePath);

	            time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
	        } catch (Exception ex) {
	            Log.e("getDurationMark", ex.toString());
	        }

	        //fix for the gallery picker crash
	        // if it couldn't extractMetadata() of a media file
	        //time was null
	        time = time == null ? "0" : time.isEmpty() ? "0" : time;
	        //bam crash - no more :)
	        int timeInMillis = Integer.parseInt(time);
	        int duration = timeInMillis / 1000;
	        int hours = duration / 3600;
	        int minutes = (duration % 3600) / 60;
	        int seconds = duration % 60;
	        StringBuilder sb = new StringBuilder();
	        if (hours > 0) {
	            sb.append(hours).append(":");
	        }
	        if (minutes < 10) {
	            sb.append("0").append(minutes);
	        } else {
	            sb.append(minutes);
	        }
	        sb.append(":");
	        if (seconds < 10) {
	            sb.append("0").append(seconds);
	        } else {
	            sb.append(seconds);
	        }
	        return sb.toString();
	    }
}
