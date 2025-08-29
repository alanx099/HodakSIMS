package sims.hodaksims.controller.list;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.ProductRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ListProduct {
    @FXML
    private TextField sku;
    @FXML
    private TextField name;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private ComboBox<Category> categories;
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> suppName;
    @FXML
    private TableColumn<Supplier, String> suppOIB;

    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> tableSKU;
    @FXML
    private TableColumn<Product, String> tableName;
    @FXML
    private TableColumn<Product, String> tableCategory;
    @FXML
    private TableColumn<Product, String> tablePrice;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private final List<Category> loadedCategories = allCategories.findAll();
    private final ObservableList<Supplier> currSuppliers = FXCollections.observableArrayList();
    private final ProductRepository<Product> prodRepo = new ProductRepository<>();
    private final List<Product> products = prodRepo.findAll();
    private final ObservableList<Product> prodObv= FXCollections.observableArrayList(products);
    public void initialize(){
            supplierTable.setItems(currSuppliers);
            categories.getItems().addAll(loadedCategories);
            categories.setCellFactory(cellData-> new ListCell<>(){
                @Override
                protected void updateItem(Category category, boolean b) {
                    super.updateItem(category, b);
                    if(b || category==null){
                    setText(null);
                    }else{
                        setText(category.getName());
                    }
                }
            });
            tableSKU.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getSku()));
            tableName.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getName()));
            tablePrice.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getPrice().toString()));
            tableCategory.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getCategory().getName()));
            productsTable.setItems(prodObv);
            suppName.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getName()));
            suppOIB.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getOib()));

    }
    public void showSuppliers(){
        Optional<List<Supplier>> selectedCategories = Optional.ofNullable(productsTable.getSelectionModel().getSelectedItem().getSuppliers());

        if(selectedCategories.isPresent()){
            currSuppliers.clear();
            currSuppliers.addAll(selectedCategories.get());
            supplierTable.setItems(currSuppliers);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Nije unesen dobavljač");
            alert.setContentText("Navedeno proizvod nema unesen dobavljača");
            alert.showAndWait();
        }
    }

    @FXML
    protected void deleteSelectedProduct(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brisanje skladišta");
        alert.setHeaderText("Brisanje označenog proizvoda");
        alert.setContentText("Jeste li sigurno da želite obrisati proizvod: " + productsTable.getSelectionModel().getSelectedItem().getName());
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent() && answer.get()== ButtonType.OK){
                prodRepo.delete(productsTable.getSelectionModel().getSelectedItem());
            productsTable.getItems().remove((productsTable.getSelectionModel().getSelectedItem()));
            }
    }
    @FXML
    protected void beforeFilter(){
        Map<String, String> numbersMap = Map.of("Cijena od", this.priceFrom.getText(), "Cijena do", this.priceTo.getText());
        Boolean numbers = InputVerifyUtil.checkForDecimal(numbersMap);
        if (Boolean.TRUE.equals(numbers)) {
            filterAll();
        }
    }

    @FXML
    protected void filterAll(){
        Optional<String> nameF = Optional.ofNullable(name.getText());
        Optional<String> skuF = Optional.ofNullable(sku.getText());
        Optional<BigDecimal> priceFromF = Optional.ofNullable(priceFrom.getText().isEmpty() ? null : BigDecimalParser.parse(priceFrom.getText()));
        Optional<BigDecimal> priceToF = Optional.ofNullable(priceTo.getText().isEmpty() ? null :BigDecimalParser.parse(priceTo.getText()));
        Optional<Category> categoriesF = Optional.ofNullable(categories.getSelectionModel().getSelectedItem());

        Stream<Product> filteri = products.stream();
        if(nameF.isPresent()){
            filteri = filteri.filter(x -> x.getName().toUpperCase().contains(name.getText().toUpperCase()));
        }
        if(skuF.isPresent()){
            filteri = filteri.filter(x -> x.getSku().toUpperCase().contains(sku.getText().toUpperCase()));
        }
        if(categoriesF.isPresent()){
            filteri = filteri.filter(x -> x.getCategory().getName().equals(categoriesF.get().getName()));
        }
        if(priceFromF.isPresent() ){
            filteri = filteri.filter(x -> x.getPrice().compareTo(priceFromF.get()) >= 0);
        }
        if(priceToF.isPresent()){
            filteri = filteri.filter(x -> x.getPrice().compareTo(priceToF.get()) <= 0);
        }

        prodObv.setAll(filteri.toList());
        productsTable.setItems(prodObv);

    }
    @FXML
    protected void filterReset(){
        name.clear();
        sku.clear();
        priceFrom.clear();
        priceTo.clear();
        categories.getSelectionModel().clearSelection();
        prodObv.setAll(products);
        productsTable.setItems(prodObv);
    }
    @FXML
    protected void addNewProduct(){
        ScreenManagerController.switchTo(View.ADDPRODUCT);
    }



}
