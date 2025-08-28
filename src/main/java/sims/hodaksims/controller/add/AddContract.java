package sims.hodaksims.controller.add;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.ContractRepository;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.repository.WarehouseRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddContract {
    static final Logger log = LoggerFactory.getLogger(AddContract.class);
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
    private final List<Supplier> SuppList = sRep.findAll();
    private final List<Warehouse> WarehouseList = wRep.findAll();
    @FXML
    public void initialize() {
        supplierCombo.getItems().addAll(SuppList);

        warehouseCombo.getItems().addAll(WarehouseList);

    }

    @FXML
    protected void switchToSceneListContract() {
        ScreenManagerController.switchTo(View.LISTCONTRACT);
    }
    @FXML
    public void insertToDb(){
        Contract cont = new Contract(supplierCombo.getValue(), warehouseCombo.getValue(), contractStart.getValue(), contractEnd.getValue());
        cRep.save(cont);
        this.switchToSceneListContract();
    }
    public void beforeInsert() {
       // Map<String, String> required = Map.of("Dobavljač",  Objects.toString(this.warehouseCombo.getSelectionModel().getSelectedItem(), ""), "Skladište", this.warehouseCombo.getSelectionModel().getSelectedItem().toString(), "Datum od", this.contractStart.getPromptText(), "Datum do", this.contractEnd.getValue().toString());
        Map<String, String> required = Map.of("Dobavljač",  Objects.toString(this.supplierCombo.getSelectionModel().getSelectedItem(), ""), "Skladište",  Objects.toString(this.warehouseCombo.getSelectionModel().getSelectedItem(), ""), "Datum početak", Objects.toString(this.contractStart.getValue(), ""),"Datum kraj", Objects.toString(this.contractEnd.getValue(), ""));
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)) {
            insertToDb();
        }
    }
}
