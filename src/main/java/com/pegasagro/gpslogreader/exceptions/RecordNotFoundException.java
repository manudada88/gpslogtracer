package com.pegasagro.gpslogreader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3823260163768004593L;

	public RecordNotFoundException(String message) {
        super(message);
    }

}
