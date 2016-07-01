package com.appmanagerserver.exception;

import org.springframework.http.HttpStatus;

public enum CustomErrorMessage {
	QUEUE_CONNECTION_FAILED(1001, "invalid queue connection", HttpStatus.BAD_REQUEST.value()),
	FILE_NOTFOUND(1015, "file.not.found", HttpStatus.INTERNAL_SERVER_ERROR.value()),
	INVALID_USER_NAME(1002,"invalid username",HttpStatus.BAD_REQUEST.value()),
	ERROR_PARSING_JSON(1003,"invalid parsing json",HttpStatus.BAD_REQUEST.value());

	private int errorCode;

	private final String errorMessage;

	private int statusCode;

	private CustomErrorMessage(int errorCode, String errorMessage, int statusCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
