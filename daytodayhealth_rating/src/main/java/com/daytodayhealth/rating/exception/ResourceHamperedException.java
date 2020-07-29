package com.daytodayhealth.rating.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceHamperedException extends CoreException {

	private static final long serialVersionUID = 1L;

	public ResourceHamperedException(String errorCode) {
		super(errorCode);
	}

	public ResourceHamperedException(String errorCode, String message) {
		super(errorCode, message);
	}

}
