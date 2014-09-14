package com.ggt.slidescast.slideshare.model;

import java.io.Serializable;

public class Meta extends SlideShareObject implements Serializable {

	private static final long serialVersionUID = 6167580637951331315L;

	private String Query;
	private String ResultOffset;
	private long NumResults;
	private long TotalResults;

	public String getQuery() {
		return Query;
	}

	public void setQuery(String query) {
		Query = query;
	}

	public String getResultOffset() {
		return ResultOffset;
	}

	public void setResultOffset(String resultOffset) {
		ResultOffset = resultOffset;
	}

	public long getNumResults() {
		return NumResults;
	}

	public void setNumResults(long numResults) {
		NumResults = numResults;
	}

	public long getTotalResults() {
		return TotalResults;
	}

	public void setTotalResults(long totalResults) {
		TotalResults = totalResults;
	}

}
