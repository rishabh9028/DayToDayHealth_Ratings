package com.daytodayhealth.rating.exception;


public class ErrorCode {

	private ErrorCode() {
	}

	public static class Common {
		private Common() {
		}

	}
	public static class ReviewError {
		private ReviewError() {
		}
		public static final String RECORD_NOT_FOUND = "msg.review.error";
		public static final String EMPTY_LIST = "msg.list.empty";
		public static final String WRONG_ID_ERROR = "id.error";
	}
	
}
 