package sims.hodaksims.exceptions;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.repository.WarehouseRepository;

public class RepositoryAccessException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(RepositoryAccessException.class);

    public RepositoryAccessException(String message) {
        super(message);
        log.error(message);
        if(message.contains("Referential integrity constraint violation")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Željenu stavku nije moguće obrisati\n");
            alert.setContentText("Stavku nije moguće obrisati jer postoji stavka koja nju koristi");
            alert.showAndWait();
        }
    }

}
