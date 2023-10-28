package com.jspiders.studentmanagementsystem.exception;

public class StudentNotFoundByPhNoException extends RuntimeException {
	private String message;

	public StudentNotFoundByPhNoException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
