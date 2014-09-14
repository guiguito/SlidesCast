package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Slideshows extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = -743604670141346782L;

	private Meta Meta;

	@XStreamImplicit(itemFieldName = "Slideshow")
	private List<Slideshow> slideShows;

	public Meta getMeta() {
		return Meta;
	}

	public void setMeta(Meta meta) {
		this.Meta = meta;
	}

	public List<Slideshow> getSlideShows() {
		return slideShows;
	}

	public void setSlideShows(List<Slideshow> slideShows) {
		this.slideShows = slideShows;
	}

}
