package sims.hodaksims.controller.add;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.View;
import sims.hodaksims.model.WareCapacity;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;

public class AddWarehouse {
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

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private  ObservableList<Category> loadedCategories = FXCollections.observableArrayList(allCategories.findAll());
    private final ObservableList<WareCapacity> currCapacity = FXCollections.observableArrayList();
    private final List<WareCapacity> insertList = new ArrayList<>();
    private final WarehouseRepository<Warehouse> wareRepo = new WarehouseRepository<>();
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
    }

    public void pushToTable(){
        Category curCat = categories.getValue();
        Integer ammount = Integer.parseInt(setAmmount.getText());
        WareCapacity capacityToPush = new WareCapacity(curCat, ammount);
        loadedCategories.remove(categories.getValue());

        categories.setItems(loadedCategories);
        insertList.add(capacityToPush);
        currCapacity.clear();
        currCapacity.addAll(insertList);
        capacityTable.setItems(currCapacity);

    }
    public void deleteSelectedCapacity(){
        loadedCategories.add(capacityTable.getSelectionModel().getSelectedItem().getCategory());
        categories.setItems(loadedCategories);
        insertList.remove(capacityTable.getSelectionModel().getSelectedItem());
        currCapacity.setAll(insertList);
        capacityTable.setItems(currCapacity);

    }

    public void insertToDb(){
            String wName = name.getText();
            String wCountry = country.getText();
            String wCity = city.getText();
            String wPostal = postalNb.getText();
            String wStreet = street.getText();
            String wStreetNb = streetNb.getText();

            Warehouse curWare = new Warehouse(wName, wCity, wPostal, wStreetNb, wCountry ,wStreet,insertList);
            wareRepo.save(curWare);
            switchToSceneAddSkaldiste();
    }
    @FXML
    protected void switchToSceneAddSkaldiste() {
        ScreenManagerController.switchTo(View.LISTWAREHOUSE);
    }

}
