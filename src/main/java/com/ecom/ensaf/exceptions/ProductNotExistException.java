package com.ecom.ensaf.exceptions;

public class ProductNotExistException extends IllegalArgumentException {

	public ProductNotExistException(String msg) {
		super(msg);
	}
}
