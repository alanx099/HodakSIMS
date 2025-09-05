package sims.hodaksims.controller.add;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.ProductRepository;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.repository.WarehouseRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AddProduct je kontroler klasa za unos proizvoda
 */
public class AddProduct {
    @FXML
    private TextField name;
    @FXML
    private TextField sku;
    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField price;
    @FXML

    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> supplierName;
    @FXML
    private TableColumn<Supplier, String> supplierOib;
    @FXML
    private TableColumn<Supplier, String> supplierMinOrder;
    @FXML
    private TableColumn<Supplier, String> supplierDeliveryTime;
    @FXML
    private ComboBox<Supplier> supplierDropdown;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private final SupplierRepository<Supplier> suppRepo = new SupplierRepository<>();
    private final ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    private final ObservableList<Supplier> suppliersList = FXCollections.observableArrayList(suppRepo.findAll());
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize(){
        category.getItems().addAll(allCategories.findAll());
        supplierTable.setItems(suppliers);
        supplierName.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getName()));
        supplierOib.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getOib()));
        supplierMinOrder.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getMinOrder().toString()));
        supplierDeliveryTime.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getDeliveryTime().toString()));

        supplierDropdown.setItems(suppliersList);

    }

    /**
     * pushToTable ubacuje odabrane vrijednosti u tablicu
     */
    public void pushToTable(){
        if (supplierDropdown.getSelectionModel().getSelectedItem() == null)return;
        Supplier supplier = supplierDropdown.getSelectionModel().getSelectedItem();
        suppliersList.remove(supplier);
        suppliers.add(supplier);
    }

    /**
     * Briše odabranu stavku iz tablice
     */
    public void deleteSelectedCapacity(){
        if(supplierTable.getSelectionModel().getSelectedItem()==null)return;
        Supplier supplier = supplierTable.getSelectionModel().getSelectedItem();
        suppliersList.add(supplier);
        suppliers.remove(supplier);
    }
    /**
     * beforeInsert metoda koja se poziva kako bi se provjerili podatci prije unosa
     */
    public void beforeInsert(){
        Map<String, String> required = Map.of("SKU", this.sku.getText(),"Ime", this.name.getText(),  "Kategorija", Objects.toString(this.category.getSelectionModel().getSelectedItem(), ""),"Cijena", this.price.getText(), "Dobavljači", suppliers.isEmpty()?"":"true");
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        Map<String, String> numbersMap = Map.of("Cijena", this.price.getText());
        Boolean numbers = InputVerifyUtil.checkForDecimal(numbersMap);
        if (Boolean.TRUE.equals(requiredCheck) && Boolean.TRUE.equals(numbers)) {
            insertToDb();
        }
    }
    /**
     * insertToDb unosi podatke u bazu
     */
    public void insertToDb(){
        ProductRepository<Product> prodRepo = new ProductRepository<>();
        Product finalItem = new Product(this.sku.getText(), this.name.getText(), BigDecimalParser.parse(price.getText()),category.getSelectionModel().getSelectedItem(), suppliers);
        prodRepo.save(finalItem);
        switchToSceneListProducts();
    }

    /**
     * prebacuje korisnika na scenu s proizvodima
     */
    @FXML
    protected void switchToSceneListProducts() {
        ScreenManagerController.switchTo(View.LISTPRODUCT);
    }


}
