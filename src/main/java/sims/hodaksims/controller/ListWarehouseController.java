package sims.hodaksims.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.WareCapacity;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.List;
import java.util.Optional;

public class ListWarehouseController  {
    @FXML
    private TextField name;
    @FXML
    private TextField country;
    @FXML
    private TextField city;
    @FXML
    private TextField postalNb;
    @FXML
    private TextField street;
    @FXML
    private TextField streetNb;
    @FXML
    private ComboBox<Category> categories;
    @FXML
    private TableView<WareCapacity> capacityTable;
    @FXML
    private TableColumn<WareCapacity, String> categoryColumn;
    @FXML
    private TableColumn<WareCapacity, String> ammountColumn;
    @FXML
    private TextField setAmmount;
    @FXML
    private TableView<Warehouse> wareTable;
    @FXML
    private TableColumn<Warehouse, String> tableName;
    @FXML
    private TableColumn<Warehouse, String> tableCountry;
    @FXML
    private TableColumn<Warehouse, String> tableCity;
    @FXML
    private TableColumn<Warehouse, String> tablePostal;
    @FXML
    private TableColumn<Warehouse, String> tableStreet;
    @FXML
    private TableColumn<Warehouse, String> tableStreetNumber;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private final List<Category> loadedCategories = allCategories.findAll();
    private final ObservableList<WareCapacity> currCapacity = FXCollections.observableArrayList();
    private final WarehouseRepository<Warehouse> wareRepo = new WarehouseRepository<>();
    private final List<Warehouse> wareHoses = wareRepo.findAll();
    private final ObservableList<Warehouse> wareObv= FXCollections.observableArrayList(wareHoses);
    public void initialize(){
            capacityTable.getItems().addAll(currCapacity);
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
            categoryColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getCategory().getName()));
            ammountColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getCapacity().toString()));
            wareTable.getItems().addAll(wareObv);
            tableName.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getName()));
            tableCountry.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getCountry()));
            tableCity.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getCity()));
            tablePostal.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getPostalCode()));
            tableStreet.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getStreetName()));
            tableStreetNumber.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue()).getStreetNumber()));
    }
    public void showCapacity(){
        Optional<List<WareCapacity>> selectedCategories = Optional.ofNullable(wareTable.getSelectionModel().getSelectedItem().getCapacity());

        if(selectedCategories.isPresent()){
            currCapacity.clear();
            currCapacity.addAll(selectedCategories.get());
            capacityTable.setItems(currCapacity);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Nije unesen kapacitet");
            alert.setContentText("Navedeno skladište nema unesen kapacitet");
            alert.showAndWait();
        }
    }
    public void pushToTable(){
        Category curCat = categories.getValue();
        Integer ammount = Integer.parseInt(setAmmount.getText());
        WareCapacity capacityToPush = new WareCapacity(curCat, ammount);
        currCapacity.add(capacityToPush);
        capacityTable.setItems(currCapacity);

    }
    public void deleteSelectedCapacity(){
        capacityTable.getItems().removeAll((capacityTable.getSelectionModel().getSelectedItem()));
    }
    public void insertToDb(){
            String wName = name.getText();
            String wCountry = country.getText();
            String wCity = city.getText();
            String wPostal = postalNb.getText();
            String wStreet = street.getText();
            String wStreetNb = streetNb.getText();

            Warehouse curWare = new Warehouse(wName, wCountry, wCity, wPostal, wStreet, wStreetNb, currCapacity);
            wareRepo.save(curWare);
    }

}
