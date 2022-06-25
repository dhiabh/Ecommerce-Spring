package com.ecom.ensaf.exceptions;

public class AuthenticationFailException extends IllegalArgumentException {
	public AuthenticationFailException(String msg) {
		super(msg);
	}
}
