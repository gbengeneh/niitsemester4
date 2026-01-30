package com.db.bankingapi.exceptions;

public class DisabledUserException extends RuntimeException {



	public DisabledUserException(String msg) {
		super(msg);
	}

}
