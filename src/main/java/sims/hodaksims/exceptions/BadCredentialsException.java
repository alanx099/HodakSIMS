package sims.hodaksims.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadCredentialsException extends Exception{
    private static final Logger log = LoggerFactory.getLogger(BadCredentialsException.class);

    public BadCredentialsException(String message) {
        super(message);
        log.info(message);
    }
}
