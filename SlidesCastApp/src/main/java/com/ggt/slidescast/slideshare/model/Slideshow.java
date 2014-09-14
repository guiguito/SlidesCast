package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class Slideshow extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 6881857829169860826L;

	private String ID;
	private String Title;
	private String Description;
	private int Status;
	private String Username;
	private String URL;
	private String ThumbnailURL;
	private String ThumbnailSize;
	private String ThumbnailSmallURL;
	private String Embed;
	private String Created;
	private String Updated;
	private String Language;
	private String Format;
	private int Download;
	private String DownloadUrl;
	private int SlideshowType;
	private int InContest;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getThumbnailURL() {
		return ThumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		ThumbnailURL = thumbnailURL;
	}

	public String getThumbnailSize() {
		return ThumbnailSize;
	}

	public void setThumbnailSize(String thumbnailSize) {
		ThumbnailSize = thumbnailSize;
	}

	public String getThumbnailSmallURL() {
		return ThumbnailSmallURL;
	}

	public void setThumbnailSmallURL(String thumbnailSmallURL) {
		ThumbnailSmallURL = thumbnailSmallURL;
	}

	public String getEmbed() {
		return Embed;
	}

	public void setEmbed(String embed) {
		Embed = embed;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	public String getUpdated() {
		return Updated;
	}

	public void setUpdated(String updated) {
		Updated = updated;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public int getDownload() {
		return Download;
	}

	public void setDownload(int download) {
		Download = download;
	}

	public String getDownloadUrl() {
		return DownloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		DownloadUrl = downloadUrl;
	}

	public int getSlideshowType() {
		return SlideshowType;
	}

	public void setSlideshowType(int slideshowType) {
		SlideshowType = slideshowType;
	}

	public int getInContest() {
		return InContest;
	}

	public void setInContest(int inContest) {
		InContest = inContest;
	}

}
