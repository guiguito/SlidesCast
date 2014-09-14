package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class Favorite extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = -6427000261981832503L;

	private String slideshow_id;
	private String tag_text;

	public String getSlideshow_id() {
		return slideshow_id;
	}

	public void setSlideshow_id(String slideshow_id) {
		this.slideshow_id = slideshow_id;
	}

	public String getTag_text() {
		return tag_text;
	}

	public void setTag_text(String tag_text) {
		this.tag_text = tag_text;
	}

}
