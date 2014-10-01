package com.anky.albumgallerydemo.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anky.albumgallerydemo.R;
import com.anky.albumgallerydemo.model.MediaObject;
import com.anky.albumgallerydemo.utils.ProcessGalleryFile;

public class ImageAdapter extends CommonAdapter<MediaObject> {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private Set<ProcessGalleryFile> tasks;
    public ImageAdapter(Context c) {
        mContext = c;
        isFirstTime = true;
        tasks = new HashSet<ProcessGalleryFile>();
    }

    private boolean isFirstTime;

    public void setFirstTime(boolean firstTime) {
        this.isFirstTime = firstTime;
    }


    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.


    public static class ViewHolder {
    	public ImageView imgThumb;
        public TextView videoDuration;
        public MediaObject object;
        public int position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            holder.position = position;
            convertView = vi.inflate(R.layout.list_item_gallery, null);
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumbnail);
            holder.videoDuration = (TextView) convertView.findViewById(R.id.txtDuration);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgThumb.setImageResource(R.drawable.ic_launcher);
        holder.object = getItem(position);
        convertView.setTag(holder);
        //String filePath = object.getPath();
        if (isFirstTime) {
            /*
            Create new async task in getView() method is a very bad practice.
            getView() gets called very frequently and therefore we will eventually exceed the number of
            allowed threads if we're not checking if a thread is currently running on a row.
            Temporary solution for avoiding java.util.concurrent.RejectedExecutionException
            */
            ProcessGalleryFile processGalleryFile = new ProcessGalleryFile(holder.imgThumb, holder.videoDuration, holder.object.getPath(), holder.object.getMediaType());
            if (tasks == null) {
                tasks = new HashSet<ProcessGalleryFile>();
            }
            if (!tasks.contains(processGalleryFile)) {
                try {
                    processGalleryFile.execute();
                    tasks.add(processGalleryFile);
                } catch (Exception ignored) {
                }
            }
        } else {
            try {
                cancelAll();
            } catch (Exception ignored) {
            }
            holder.videoDuration.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void setData(List<MediaObject> values) {
        if (values != null) {
            Collections.sort(values);
        }
        super.setData(values);
    }

    public void cancelAll() throws Exception {
        final Iterator<ProcessGalleryFile> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
            iterator.remove();
        }
    }
}
