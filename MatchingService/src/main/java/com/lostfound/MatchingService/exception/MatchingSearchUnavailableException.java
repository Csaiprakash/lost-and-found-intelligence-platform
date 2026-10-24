package com.lostfound.MatchingService.exception;

public class MatchingSearchUnavailableException
        extends RuntimeException {

    public MatchingSearchUnavailableException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}
