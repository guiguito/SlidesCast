package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class SlideShowUploaded extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 5627734033083597966L;

	private long SlideShowID;

	public long getSlideShowID() {
		return SlideShowID;
	}

	public void setSlideShowID(long slideShowID) {
		SlideShowID = slideShowID;
	}

}
