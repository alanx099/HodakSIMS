package sims.hodaksims.controller.list;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.ContractRepository;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ListContracts {
    @FXML
    TextField nameFilter;
    @FXML
    TextField descriptionFilter;
    @FXML
    private TableView<Contract> contractTable;
    @FXML
    private TableColumn<Contract, String> supplier;
    @FXML
    private TableColumn<Contract, String> warehouse;
    @FXML
    private TableColumn<Contract, String> start;
    @FXML
    private TableColumn<Contract, String> end;
    @FXML
    private ComboBox<Supplier> supplierFilter;
    @FXML
    private ComboBox<Warehouse> warehouseFilter;
    @FXML
    private DatePicker startFilter;
    @FXML
    private DatePicker endFilter;

    private final ContractRepository<Contract> contractRepository = new ContractRepository<>();
    private final List<Contract> contracts = contractRepository.findAll();
    private final ObservableList<Contract> contractObservableList = FXCollections.observableArrayList(contracts);

    private final SupplierRepository<Supplier> sRep = new SupplierRepository<>();
    private final WarehouseRepository<Warehouse> wRep = new WarehouseRepository<>();
    private final List<Supplier> suppList = sRep.findAll();
    private final List<Warehouse> warehouseList = wRep.findAll();
    public void initialize(){
        contractTable.setItems(contractObservableList);
        supplierFilter.getItems().setAll(suppList);
        warehouseFilter.getItems().setAll(warehouseList);
        supplier.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getSupplier().getName()));
        warehouse.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getWarehouse().getName()));
        start.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        end.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getEndDate().toString()));
    }
    public void deleteSelectedContract(){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brisanje ugovora");
        alert.setHeaderText("Brisanje označene ugovora");
        alert.setContentText("Jeste li sigurno da želire obrisati označeni ugovor?");
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent() && answer.get()== ButtonType.OK){
                contractRepository.delete(contractTable.getSelectionModel().getSelectedItem());
                contractTable.getItems().remove((contractTable.getSelectionModel().getSelectedItem()));
            }
    }
    @FXML
    protected void switchToSceneAddContact() {
        ScreenManagerController.switchTo(View.ADDCONTRACT);
    }
    @FXML
    protected void switchToSceneUpdateContract() {
        if(contractTable.getSelectionModel().getSelectedItem() == null) return;
        ScreenManagerController.switchToWithData(View.UPDATECONTRACT, contractTable.getSelectionModel().getSelectedItem());
    }
    @FXML
    protected void filterAll(){
        Optional<Supplier> supplierF = Optional.ofNullable(supplierFilter.getSelectionModel().getSelectedItem());
        Optional<Warehouse> warehouseF = Optional.ofNullable(warehouseFilter.getSelectionModel().getSelectedItem());
        Optional<LocalDate> startF = Optional.ofNullable(startFilter.getValue());
        Optional<LocalDate> endF = Optional.ofNullable(endFilter.getValue());
        Stream<Contract> theStream = contracts.stream();

        if(supplierF.isPresent()){
            theStream = theStream.filter(x -> x.getSupplier().getId().equals(supplierF.get().getId()));
        }
        if(warehouseF.isPresent()){
            theStream = theStream.filter(x -> x.getWarehouse().getId().equals(warehouseF.get().getId()));
        }
        if (startF.isPresent()) {
            theStream = theStream.filter(x -> x.getStartDate().isAfter(startF.get()));
        }
        if (endF.isPresent()) {
            theStream = theStream.filter(x -> x.getEndDate().isBefore(endF.get()));
        }
        contractObservableList.clear();
        contractObservableList.addAll(theStream.toList());
    }
    @FXML
    protected void resetFilter(){
        supplierFilter.getSelectionModel().clearSelection();
        warehouseFilter.getSelectionModel().clearSelection();
        startFilter.getEditor().clear();
        endFilter.getEditor().clear();
        contractObservableList.setAll(contracts);


    }
}
