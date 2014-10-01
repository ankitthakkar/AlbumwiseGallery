package com.anky.albumgallerydemo.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class CommonAdapter<T> extends BaseAdapter {

    protected List<T> mData = new ArrayList<T>();

    public void setmData(List<T> mData) {
        if (mData != null) {
            this.mData = mData;
        } else {
            this.mData.clear();
        }
    }

    public List<T> getmData() {
        return mData;
    }

    public void setData(List<T> values) {
        setData(values, true, false);
    }

    public void setData(T[] values) {
        setData(Arrays.asList(values), true, false);
    }

    public void setData(List<T> values, Boolean clear) {
        setData(values, clear, false);
    }

    public void setData(List<T> values, Boolean clear, Boolean addToStart) {
        if (clear) {
            mData.clear();
        }
        if (values != null) {
            if (addToStart) {
                mData.addAll(0, values);
            } else {
                mData.addAll(values);
            }
        }
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean notify) {
        mData.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void removeItem(T item) {
        mData.remove(item);
        notifyDataSetChanged();
    }

    public void replaceItem(T newItem, T target) {
        if (newItem == null) {
            removeItem(target);
        } else {
            int position = mData.indexOf(target);
            if (position != -1) {
                mData.set(position, newItem);
            } else {
                mData.add(newItem);
            }
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < mData.size()) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void insertItem(int to, T item) {
        mData.add(to, item);
    }

    public T getItem(long position) {
        return getItem((int) position);
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public T getItem(int position) {
        return position >= 0 && position < getCount() ? mData.get(position) : null;
    }

    public T getLastItem() {
        return getItem(getCount() - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItemsToStart(List<T> messages) {
        mData.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addAll(List<T> messages) {
        mData.addAll(messages);
        notifyDataSetChanged();
    }
}

