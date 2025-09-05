package sims.hodaksims.controller.update;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.Product;
import sims.hodaksims.model.Supplier;
import sims.hodaksims.model.View;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.ProductRepository;
import sims.hodaksims.repository.SupplierRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * UpdateProduct je kontroler klasa za ažuriranje proizvoda
 */
public class UpdateProduct <T extends Product> extends AbstractUpdateController<T> {
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
    @FXML
    private Label title;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private final SupplierRepository<Supplier> suppRepo = new SupplierRepository<>();
    private final ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    private  ObservableList<Supplier> suppliersList = FXCollections.observableArrayList(suppRepo.findAll());
    private Long id;
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

    }

    @Override
    public void setFields(T item) {
        title.setText("Trenutno ažuriramo:"+item.getName()+"Id:"+item.getId().toString() );
        name.setText(item.getName());
        sku.setText(item.getSku());
        price.setText(item.getPrice().toString());
        category.setValue(item.getCategory());
        suppliers.addAll(item.getSuppliers());
        Set<String> supplierOibString =  suppliers.stream().map(x->x.getOib()).collect(Collectors.toSet());
        suppliersList = FXCollections.observableArrayList(suppliersList.stream()
                .filter(x -> !supplierOibString.contains(x.getOib())).toList());
        supplierDropdown.setItems(suppliersList);
        id= item.getId();
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
     * insertToDb ažurira podatke u bazi
     */
    public void insertToDb(){
        ProductRepository<Product> prodRepo = new ProductRepository<>();
        Product finalItem = new Product(this.sku.getText(), this.name.getText(), BigDecimalParser.parse(price.getText()),category.getSelectionModel().getSelectedItem(), suppliers);
        finalItem.setId(id);
        prodRepo.update(finalItem);
        switchToSceneListProducts();
    }
    @FXML
    /**
     * prebacuje korisnika na ekran s proizvodima
     */
    protected void switchToSceneListProducts() {
        ScreenManagerController.switchTo(View.LISTPRODUCT);
    }
}
