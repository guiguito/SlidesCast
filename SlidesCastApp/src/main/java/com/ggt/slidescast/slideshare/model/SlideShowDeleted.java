package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class SlideShowDeleted extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = -4002964819198887366L;

	private String SlideshowID;

	public String getSlideshowID() {
		return SlideshowID;
	}

	public void setSlideshowID(String slideshowID) {
		SlideshowID = slideshowID;
	}

}
