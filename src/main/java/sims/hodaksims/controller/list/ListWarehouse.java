package sims.hodaksims.controller.list;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListWarehouse {
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
    @FXML
    protected void deleteSelectedWareHouse(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brisanje skladišta");
        alert.setHeaderText("Brisanje označenog skladišta");
        alert.setContentText("Jeste li sigurno da želite obrisati skladište: " + wareTable.getSelectionModel().getSelectedItem().getName());
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent() && answer.get()== ButtonType.OK){
                wareRepo.delete(wareTable.getSelectionModel().getSelectedItem());
                wareTable.getItems().remove((wareTable.getSelectionModel().getSelectedItem()));
            }
    }
    @FXML
    protected void filterAll(){
        Optional<String> nameF = Optional.ofNullable(name.getText());
        Optional<String> countryF = Optional.ofNullable(country.getText());
        Optional<String> cityF = Optional.ofNullable(city.getText());
        Optional<String> postalNbF = Optional.ofNullable(postalNb.getText());
        Optional<String> streetF = Optional.ofNullable(street.getText());
        Optional<String> streetNbF = Optional.ofNullable(streetNb.getText());
        Optional<String> setAmmountF = Optional.ofNullable(setAmmount.getText());
        Optional<Category> categoriesF = Optional.ofNullable(categories.getSelectionModel().getSelectedItem());

        Stream<Warehouse> filteri = wareHoses.stream();
        if(nameF.isPresent()){
            filteri = filteri.filter(x -> x.getName().toUpperCase().contains(name.getText().toUpperCase()));
        }
        if(countryF.isPresent()){
            filteri = filteri.filter(x -> x.getCountry().toUpperCase().contains(country.getText().toUpperCase()));
        }
        if(cityF.isPresent()){
            filteri = filteri.filter(x -> x.getCity().toUpperCase().contains(city.getText().toUpperCase()));
        }
        if(postalNbF.isPresent()){
            filteri = filteri.filter(x -> x.getPostalCode().toUpperCase().contains(postalNb.getText().toUpperCase()));
        }
        if(streetF.isPresent()){
            filteri = filteri.filter(x -> x.getStreetName().toUpperCase().contains(street.getText().toUpperCase()));
        }
        if(streetNbF.isPresent()){
            filteri = filteri.filter(x -> x.getStreetNumber().toUpperCase().contains(streetNb.getText().toUpperCase()));
        }
        if(categoriesF.isPresent()){
            filteri = filteri.filter(x -> x.getCapacity().stream().map(y -> y.getCategory().getName()).toList().contains(categoriesF.get().getName()));
        }
        if(setAmmountF.isPresent() && categoriesF.isPresent() && !setAmmountF.get().isEmpty()) {
                filteri = filteri.filter(x -> {
                    Map<String, Integer> real = x.getCapacity().stream()
                            .collect(Collectors.toMap(
                                    y -> y.getCategory().getName(),
                                    WareCapacity::getCapacity
                            ));
                    return real.containsKey(categoriesF.get().getName()) && real.get(categoriesF.get().getName()) > Integer.parseInt(setAmmountF.get());
                });
            }
        wareObv.setAll(filteri.toList());
        wareTable.setItems(wareObv);

    }
    @FXML
    protected void filterReset(){
        name.clear();
        country.clear();
        city.clear();
        postalNb.clear();
        street.clear();
        streetNb.clear();
        setAmmount.clear();
        categories.getSelectionModel().clearSelection();
        wareObv.setAll(wareHoses);
        wareTable.setItems(wareObv);
    }
    @FXML
    protected void addNewWarehouse(){
        ScreenManagerController.switchTo(View.ADDWAREHOUSE);
    }
    @FXML
    protected void updateWareHouse(){
        ScreenManagerController.switchToWithData(View.UPADTEWAREHOUSE, wareTable.getSelectionModel().getSelectedItem());
    }


}
