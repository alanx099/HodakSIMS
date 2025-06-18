package sims.hodaksims.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.repository.WarehouseRepository;

public class RepositoryAccessException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(RepositoryAccessException.class);

    public RepositoryAccessException(String message) {
        super(message);
        log.error(message);
    }

}
