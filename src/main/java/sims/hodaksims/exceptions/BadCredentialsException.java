package sims.hodaksims.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BadCredentialsException iznimka za krivi unos korisničkih podataka
 */
public class BadCredentialsException extends Exception{
    private static final Logger log = LoggerFactory.getLogger(BadCredentialsException.class);

    /**
     * BadCredentialsException konstruktor
     * @param message poruka
     */
    public BadCredentialsException(String message) {
        super(message);
        log.info(message);
    }
}
