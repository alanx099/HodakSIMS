package sims.hodaksims.controller.update;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.Supplier;
import sims.hodaksims.model.SupplierContact;
import sims.hodaksims.model.View;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.*;
/**
 * AddSupplier je kontroller klasa za ažuriranje dobavljača
 */
public class UpdateSupplier<T extends Supplier> extends AbstractUpdateController<T> {
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
    @FXML
    private Label naslov;
    @FXML
    private Label idPromjene;

    private static Logger log = LoggerFactory.getLogger(UpdateSupplier.class);

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

    @Override
    public void setFields(T item) {
        name.setText(item.getName());
        oib.setText(item.getOib());
        minOrder.setText(item.getMinOrder().toString());
        orderTime.setText(item.getDeliveryTime().toString());
        obvContacts.setAll(item.getContacts());
        naslov.setText("Ažuriranje dobavljača: " + item.getName());
        idPromjene.setText(item.getId().toString());
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
     * pushToTable ubacuje odabrane vrijednosti u tablicu
     */
    public void pushToTable(){
    String nameC = this.contName.getText();
    String email = this.contEmail.getText();
    String tel = this.contTel.getText();
    String address = this.contAddress.getText();
    SupplierContact contact = new SupplierContact(nameC, email, tel, address);
    obvContacts.add(contact);
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
     * beforeInsert metoda koja se poziva kako bi se provjerili podatci prije unosa
     */
    public void beforeInsert(){
        Map<String, String> required = Map.of("Ime", this.name.getText(), "OIB", this.oib.getText(),"Minimalna naruđba", this.minOrder.getText(), "Vrijeme naruđbe", this.orderTime.getText(),"Osoba za kontakt",obvContacts.isEmpty()?"":"true" );
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)){
            Map<String, String> numbersMap = Map.of("Minimalna naruđba", this.minOrder.getText(), "Vrijeme naruđbe", this.orderTime.getText());
            Boolean numbers = InputVerifyUtil.checkForNumber(numbersMap);
            if(Boolean.TRUE.equals(numbers)){
                 insertToDb();
            }
        }else {
            log.info("Krivi unos");
        }
    }
    /**
     * insertToDb ažirira podatke u bazi
     */
    public void insertToDb(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ažuriranje dobavljača");
        alert.setHeaderText("Ažuriranje dobavljača");
        alert.setContentText("Jeste li sigurni da ažurirati dobavljača: " + name.getText());
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent() && answer.get()== ButtonType.OK) {
            Set<SupplierContact> contactSet = new HashSet<>(obvContacts);
            Long ids = Long.parseLong(idPromjene.getText());
            String nameNew = this.name.getText();
            String oibNew = this.oib.getText();
            Integer minOrderNew = Integer.parseInt(this.minOrder.getText());
            Integer orderTimeNew = Integer.parseInt(this.orderTime.getText());

            Supplier curSup = new Supplier(contactSet, nameNew, oibNew, minOrderNew, orderTimeNew);
            curSup.setId(ids);
            supplierRep.update(curSup);
            switchToSceneListSupplier();
        }
    }
    @FXML
    /**
     * switchToSceneListWarehouse prebacuje ekran na listu dobavljača
     */
    protected void switchToSceneListSupplier() {
        ScreenManagerController.switchTo(View.LISTSUPPLIER);
    }

}
