package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class User extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 4214734734801817654L;

	private String Name;
	private Long Count;

	@XStreamImplicit(itemFieldName = "Slideshow")
	private List<Slideshow> slideshows;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Long getCount() {
		return Count;
	}

	public void setCount(Long count) {
		Count = count;
	}

	public List<Slideshow> getSlideshows() {
		return slideshows;
	}

	public void setSlideshows(List<Slideshow> slideshows) {
		this.slideshows = slideshows;
	}

}
