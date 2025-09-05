package sims.hodaksims.controller.list;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * ListSupplier klasa za ispis svih dobavljača i
 */
public class ListSupplier {
    @FXML
    private TextField name;
    @FXML
    private TextField oib;
    @FXML
    private TextField minOrderFrom;
    @FXML
    private TextField minOrderTo;
    @FXML
    private TextField orderTimeStart;
    @FXML
    private TextField orderTimeEnd;
    @FXML
    private DatePicker dateJoinedStart;
    @FXML
    private DatePicker dateJoinedEnd;

    @FXML
    private TableView<SupplierContact> contactsTable;
    @FXML
    private TableColumn<SupplierContact, String> nameColumn;
    @FXML
    private TableColumn<SupplierContact, String> emailColumn;
    @FXML
    private TableColumn<SupplierContact, String> telColumn;
    @FXML
    private TableColumn<SupplierContact, String> addresColumn;
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> supplierName;
    @FXML
    private TableColumn<Supplier, String> supplierOIB;
    @FXML
    private TableColumn<Supplier, String> supplierMinOrder;
    @FXML
    private TableColumn<Supplier, String> supplierDeliveryTime;
    @FXML
    private TableColumn<Supplier, String> supplierJoined;

    private static Logger log = LoggerFactory.getLogger(ListSupplier.class);

    private final SupplierRepository<Supplier> supplierRepository  = new SupplierRepository<>();
    private final List<Supplier> suppliers = supplierRepository.findAll();
    private final ObservableList<Supplier> supplierObv = FXCollections.observableArrayList(suppliers);
    private final ObservableList<SupplierContact> currContacts = FXCollections.observableArrayList();
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize(){
        supplierTable.setItems(supplierObv);
        supplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        supplierOIB.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOib()));
        supplierMinOrder.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMinOrder().toString()));
        supplierDeliveryTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeliveryTime().toString()));
        supplierJoined.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJoined().toString()));
        contactsTable.getItems().addAll(currContacts);
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        telColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        addresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
    }
    @FXML
    /**
     * Prikazuje kontakte odabranog dobavljača
     */
    protected void showContacts(){
        if(supplierTable.getSelectionModel().getSelectedItem() == null) return;

        Optional<Set<SupplierContact>> selectedContacts = Optional.ofNullable(supplierTable.getSelectionModel().getSelectedItem().getContacts()).filter(contacts -> !contacts.isEmpty());

        if(selectedContacts.isPresent()){
            currContacts.clear();
            currContacts.addAll(selectedContacts.get());
            contactsTable.setItems(currContacts);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Nije unesen kontakt");
            alert.setContentText("Naveden dobavljač nema kontakte");
            alert.showAndWait();
            log.info("Nema kontakte");
        }
    }
    @FXML
    /**
     * beforeFilter provjerava unose u filteru prije primjene filtera
     */
    protected void beforeFilter(){
        Map<String, String> numbersMap = Map.of("Min. Narudba od", this.minOrderFrom.getText(),"Min. Narudba do", this.minOrderTo.getText(),"Vrijeme naručivanja od", this.orderTimeStart.getText(),"Vrijeme naručivanja do",this.orderTimeEnd.getText());
        Boolean numbers = InputVerifyUtil.checkForNumber(numbersMap);
        if (Boolean.TRUE.equals(numbers)) {
            filterAll();
        }
    }

    @FXML
    /**
     * filterAll primjenjuje vrijednosti iz filtera
     */
    protected void filterAll(){
        Optional<String> nameF = Optional.ofNullable(name.getText());
        Optional<String> oibF = Optional.ofNullable(oib.getText());
        Optional<String> minOrderFromF = Optional.ofNullable(minOrderFrom.getText());
        Optional<String> minOrderToF = Optional.ofNullable(minOrderTo.getText());
        Optional<String> orderTimeStartF = Optional.ofNullable(orderTimeStart.getText());
        Optional<String> orderTimeEndF = Optional.ofNullable(orderTimeEnd.getText());
        Optional<LocalDate> dateJoinedStartF = Optional.ofNullable(dateJoinedStart.getValue());
        Optional<LocalDate> dateJoinedEndF = Optional.ofNullable(dateJoinedEnd.getValue());
        Stream<Supplier> filteri = suppliers.stream();

        if(nameF.isPresent()){
            filteri = filteri.filter(supplier -> supplier.getName().toLowerCase().contains(nameF.get().toLowerCase()));
        }
        if(oibF.isPresent()){
            filteri = filteri.filter(supplier -> supplier.getOib().toLowerCase().contains(oibF.get().toLowerCase()));
        }
            if (minOrderFromF.isPresent() && !minOrderFromF.get().isBlank()) {
                Integer minOrderFromI = Integer.parseInt(minOrderFromF.get());
                filteri = filteri.filter(supplier -> supplier.getMinOrder().compareTo(minOrderFromI) > 0);
            }
            if (minOrderToF.isPresent() && !minOrderToF.get().isBlank()) {
                Integer minOrderToI = Integer.parseInt(minOrderToF.get());
                filteri = filteri.filter(supplier -> supplier.getMinOrder().compareTo(minOrderToI) < 0);
            }
            if (orderTimeEndF.isPresent() && !orderTimeEndF.get().isBlank()) {
                Integer orderTimeEndI = Integer.parseInt(orderTimeEndF.get());
                filteri = filteri.filter(supplier -> supplier.getMinOrder().compareTo(orderTimeEndI) < 0);
            }
            if (orderTimeStartF.isPresent() && !orderTimeStartF.get().isBlank()) {
                Integer orderTimeStartI = Integer.parseInt(orderTimeStartF.get());
                filteri = filteri.filter(supplier -> supplier.getMinOrder().compareTo(orderTimeStartI) > 0);
            }
        if(dateJoinedStartF.isPresent()){
            filteri = filteri.filter(supplier -> supplier.getJoined().isAfter(dateJoinedStartF.get()));
        }
        if(dateJoinedEndF.isPresent()){
            filteri = filteri.filter(supplier -> supplier.getJoined().isBefore(dateJoinedEndF.get()));
        }
        supplierObv.setAll(filteri.toList());
        supplierTable.setItems(supplierObv);
    }
    @FXML
    /**
     * filterReset resetira vrijednosti u filteru
     */
    protected void filterReset(){
        name.clear();
        oib.clear();
        minOrderFrom.clear();
        minOrderTo.clear();
        orderTimeEnd.clear();
        orderTimeStart.clear();
        dateJoinedEnd.setValue(null);
        dateJoinedStart.setValue(null);
        supplierObv.setAll(suppliers);
        supplierTable.setItems(supplierObv);
    }
    @FXML
    /**
     * addNewSupplier prebacuje korisnika na ekran za dodavanje novog dobavljača
     */
    protected void addNewSupplier(){
        ScreenManagerController.switchTo(View.ADDSUPPLIER);
    }
    @FXML
    /**
     * deleteSelectedSupplier briše odabranog dobavljača iz baze
     */
    protected void deleteSelectedSupplier(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brisanje skladišta");
        alert.setHeaderText("Brisanje označenog dobavljača");
        alert.setContentText("Jeste li sigurno da želite obrisati dobavljača: " + supplierTable.getSelectionModel().getSelectedItem().getName());
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent() && answer.get()== ButtonType.OK){
            supplierRepository.delete(supplierTable.getSelectionModel().getSelectedItem());
            supplierTable.getItems().remove((supplierTable.getSelectionModel().getSelectedItem()));
        }
    }
    @FXML
    /**
     * updateSupplierNow prebacuje odabranog korisnika na ekran za ažuriranje Dobavljača
     */
    protected void updateSupplierNow(){
        if(supplierTable.getSelectionModel().getSelectedItem() == null) return;
        ScreenManagerController.switchToWithData(View.UPDATESUPPLIER, supplierTable.getSelectionModel().getSelectedItem());
    }
}
