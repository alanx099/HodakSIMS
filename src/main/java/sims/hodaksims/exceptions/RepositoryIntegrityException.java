package sims.hodaksims.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RepositoryIntegrityException iznimka za povrjeđeni referencijalni integritert
 */
public class RepositoryIntegrityException extends Exception {
    private static final Logger log = LoggerFactory.getLogger(RepositoryIntegrityException.class);

    public RepositoryIntegrityException(String message) {
        super(message);
        log.error(message);
    }
}
