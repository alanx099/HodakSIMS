package sims.hodaksims.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EmptyRepositoryResultException iznimka za problem s repositoriem
 *
 */
public class EmptyRepositoryResultException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(EmptyRepositoryResultException.class);

    public EmptyRepositoryResultException(String message) {
        super(message);
        log.error(message);
    }

    public EmptyRepositoryResultException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

    public EmptyRepositoryResultException(Throwable cause) {
        super(cause);
        log.error(cause.getMessage());
    }

    public EmptyRepositoryResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyRepositoryResultException() {
    }
}
