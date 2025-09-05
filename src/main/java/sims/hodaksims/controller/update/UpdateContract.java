package sims.hodaksims.controller.update;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.Contract;
import sims.hodaksims.model.Supplier;
import sims.hodaksims.model.View;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.ContractRepository;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.repository.WarehouseRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * AddContract klasa je controller za ažuriranje ugovora u bazi podataka
 */
public class UpdateContract<T extends Contract> extends AbstractUpdateController<T>{
    static final Logger log = LoggerFactory.getLogger(UpdateContract.class);
    @FXML
    Label title;
    @FXML
    DatePicker contractStart;
    @FXML
    DatePicker contractEnd;
    @FXML
    ComboBox<Supplier> supplierCombo;
    @FXML
    ComboBox<Warehouse> warehouseCombo;
    @FXML
    TextField name;
    @FXML
    TextField description;
    private final ContractRepository<Contract> cRep = new ContractRepository<>();
    private final SupplierRepository<Supplier> sRep = new SupplierRepository<>();
    private final WarehouseRepository<Warehouse> wRep = new WarehouseRepository<>();
    private final List<Supplier> suppList = sRep.findAll();
    private final List<Warehouse> warehouseList = wRep.findAll();
    private Long itemId;
    @FXML
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize() {
        supplierCombo.getItems().addAll(suppList);
        warehouseCombo.getItems().addAll(warehouseList);
    }

    @Override
    public void setFields(T item) {
        itemId = item.getId();
        title.setText("Izmijeni ugovor id:" + item.getId());
        supplierCombo.setValue(item.getSupplier());
        warehouseCombo.setValue(item.getWarehouse());
        contractStart.setValue(item.getStartDate());
        contractEnd.setValue(item.getEndDate());
    }

    @FXML
    /**
     * Prebacuje korisnika na ekran lista kontakta
     */
    protected void switchToSceneListContract() {
        ScreenManagerController.switchTo(View.LISTCONTRACT);
    }
    @FXML
    /**
     * insertToDb unosi podatke u bazu
     */
    public void insertToDb(){
        Contract cont = new Contract(supplierCombo.getValue(), warehouseCombo.getValue(), contractStart.getValue(), contractEnd.getValue());
        cont.setId(itemId);
        cRep.update(cont);
        this.switchToSceneListContract();
    }
    /**
     * beforeInsert metoda koja se poziva kako bi se provjerili podatci prije unosa
     */
    public void beforeInsert() {
        Map<String, String> required = Map.of("Dobavljač",  Objects.toString(this.supplierCombo.getSelectionModel().getSelectedItem(), ""), "Skladište",  Objects.toString(this.warehouseCombo.getSelectionModel().getSelectedItem(), ""), "Datum početak", Objects.toString(this.contractStart.getValue(), ""),"Datum kraj", Objects.toString(this.contractEnd.getValue(), ""));
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)) {
            insertToDb();
        }
    }
}
