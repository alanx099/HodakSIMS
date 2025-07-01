package sims.hodaksims.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.View;
import sims.hodaksims.model.WareCapacity;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListCategory {
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> descroptionColumn;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private final List<Category> loadedCategories = allCategories.findAll();
    private final ObservableList<Category> obvCategories = FXCollections.observableArrayList();
    public void initialize(){
        obvCategories.addAll(loadedCategories);
        categoryTable.getItems().addAll(obvCategories);
        nameColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getName()));
        descroptionColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getDescription()));
    }
    public void deleteSelectedCategory(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brisanje kategorije");
        alert.setHeaderText("Brisanje označene kategorije");
        alert.setContentText("Jeste li sigurno da želire obrisati kategoriju: " + categoryTable.getSelectionModel().getSelectedItem().getName());
        Optional<ButtonType> answer= alert.showAndWait();
        if(answer.isPresent()){
            if(answer.get()== ButtonType.OK){
                allCategories.delete(categoryTable.getSelectionModel().getSelectedItem());
                categoryTable.getItems().remove((categoryTable.getSelectionModel().getSelectedItem()));

            }
        }

    }
    @FXML
    protected void switchToSceneAddCategory() {
        ScreenManagerController.switchTo(View.ADDCATEGORY);
    }
    @FXML
    protected void switchToSceneUpdateCategory() {
        ScreenManagerController.switchToWithData(View.UPDATECATEGORY, categoryTable.getSelectionModel().getSelectedItem());
    }

}
