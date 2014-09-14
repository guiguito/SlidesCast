package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "content" })
public class Message extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 2928893269754663045L;

	@XStreamAsAttribute
	private int ID;
	private String content;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
