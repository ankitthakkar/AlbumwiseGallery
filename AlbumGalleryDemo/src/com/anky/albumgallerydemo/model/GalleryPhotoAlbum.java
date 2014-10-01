package com.anky.albumgallerydemo.model;

public class GalleryPhotoAlbum {
	
	
	private long bucketId;
	private String bucketName;
	private String dateTaken;
	private String data;
	private int totalCount;
	
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public long getBucketId() {
		return bucketId;
	}
	public void setBucketId(long bucketId) {
		this.bucketId = bucketId;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
