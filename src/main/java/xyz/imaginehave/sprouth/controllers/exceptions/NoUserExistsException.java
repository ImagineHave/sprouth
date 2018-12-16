package xyz.imaginehave.sprouth.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoUserExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 3114904724782022001L;

	public NoUserExistsException(String exception) {
		super(exception);
	}

}
