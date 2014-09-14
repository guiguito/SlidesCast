package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class SlideShareServiceError extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = -696563550019943702L;

	private Message Message;

	public Message getMessage() {
		return Message;
	}

	public void setMessage(Message message) {
		Message = message;
	}

}
