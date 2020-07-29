package com.daytodayhealth.rating.response;

public class DataResponse extends Response {

	Object data;
	
	public DataResponse(int code, String message, Object data) {
		super(code, message);
		this.data = data;
	}

	public DataResponse() {

	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
