package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Favorites extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 6100154461662388021L;

	@XStreamImplicit(itemFieldName = "favorite")
	List<Favorite> favorites;

	public List<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}

}
