package sims.hodaksims.controller.add;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AddSupplier je kontroller klasa za unos dobavljača
 */
public class AddSupplier {
    @FXML
    private TextField name;
    @FXML
    private TextField oib;
    @FXML
    private TextField minOrder;
    @FXML
    private TextField orderTime;
    @FXML
    private TextField contName;
    @FXML
    private TextField contEmail;
    @FXML
    private TextField contTel;
    @FXML
    private TextField contAddress;
    @FXML
    private TableView<SupplierContact> contactTable;
    @FXML
    private TableColumn<SupplierContact, String> colName;
    @FXML
    private TableColumn<SupplierContact, String> colEmail;
    @FXML
    private TableColumn<SupplierContact, String> colTel;
    @FXML
    private TableColumn<SupplierContact, String> colAddress;


    SupplierRepository<Supplier> supplierRep = new SupplierRepository<>();
    ObservableList<SupplierContact> obvContacts = FXCollections.observableArrayList();
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize(){
    contactTable.setItems(obvContacts);
    colName.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getName()));
    colEmail.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getEmail()));
    colTel.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getPhone()));
    colAddress.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getAddress()));
    }
    /**
     * pushToTable ubacuje odabrane vrijednosti u tablicu
     */
    public void pushToTable(){
    String nameC = this.contName.getText();
    String email = this.contEmail.getText();
    String tel = this.contTel.getText();
    String address = this.contAddress.getText();
    SupplierContact contact = new SupplierContact(nameC, email, tel, address);
    obvContacts.add(contact);
    contactTable.setItems(obvContacts);
    contName.clear();
    contEmail.clear();
    contTel.clear();
    contAddress.clear();
    }
    /**
     * Briše odabranu stavku iz tablice
     */
    public void deleteSelectedCapacity(){
    obvContacts.remove(contactTable.getSelectionModel().getSelectedItem());
    }
    /**
     * beforePushToTable provjerava odabrane vrijednosti
     */
    public void beforePushToTable(){
        Map<String, String> required = Map.of("Ime", this.contName.getText(), "Email", this.contEmail.getText(),"Adresa", this.contAddress.getText());
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)){
            this.pushToTable();
        }
    }
    /**
     * beforeInsert metoda koja se poziva kako bi se provjerili podatci prije unosa
     */
    public void beforeInsert(){
        Map<String, String> required = Map.of("Ime", this.name.getText(), "OIB", this.oib.getText(),"Minimalna naruđba", this.minOrder.getText(), "Vrijeme naruđbe", this.orderTime.getText(),"Osoba za kontakt",obvContacts.isEmpty()?"":"true" );
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        Map<String, String> numbersMap = Map.of("Minimalna naruđba", this.minOrder.getText(), "Vrijeme naruđbe", this.orderTime.getText());
        Boolean numbers = InputVerifyUtil.checkForNumber(numbersMap);
        if (Boolean.TRUE.equals(requiredCheck)&& Boolean.TRUE.equals(numbers)){
                 insertToDb();
        }

    }
    /**
     * insertToDb unosi podatke u bazu
     */
    public void insertToDb(){
        Set<SupplierContact> contactSet = new HashSet<>(obvContacts);
        String nameNew = this.name.getText();
        String oibNew = this.oib.getText();
        Integer minOrderNew = Integer.parseInt(this.minOrder.getText());
        Integer orderTimeNew = Integer.parseInt(this.orderTime.getText());
        Supplier curSup = new Supplier(contactSet, nameNew, oibNew, minOrderNew, orderTimeNew);
        supplierRep.save(curSup);
        switchToSceneListSupplier();
    }
    @FXML
    /**
     * switchToSceneListWarehouse prebacuje ekran na listu dobavljača
     */
    protected void switchToSceneListSupplier() {
        ScreenManagerController.switchTo(View.LISTSUPPLIER);
    }

}
