package sims.hodaksims.controller.list;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.exceptions.RepositoryIntegrityException;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.View;
import sims.hodaksims.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ListCategory {
    @FXML
    TextField nameFilter;
    @FXML
    TextField descriptionFilter;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> descroptionColumn;

    private static Logger log = LoggerFactory.getLogger(ListCategory.class);

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
        if(answer.isPresent() && answer.get()== ButtonType.OK){
                try {
                    allCategories.delete(categoryTable.getSelectionModel().getSelectedItem());
                } catch (RepositoryIntegrityException | RepositoryAccessException e){
                    log.info(e.getMessage());
                }
                }
                categoryTable.getItems().remove((categoryTable.getSelectionModel().getSelectedItem()));
        }


    @FXML
    protected void switchToSceneAddCategory() {
        ScreenManagerController.switchTo(View.ADDCATEGORY);
    }
    @FXML
    protected void switchToSceneUpdateCategory() {
        if(categoryTable.getSelectionModel().getSelectedItem() == null) return;

        ScreenManagerController.switchToWithData(View.UPDATECATEGORY, categoryTable.getSelectionModel().getSelectedItem());
    }
    @FXML
    protected void filterAll(){
        Optional<String> nameF = Optional.ofNullable(nameFilter.getText());
        Optional<String> descF = Optional.ofNullable(descriptionFilter.getText());
        Stream<Category> theStream = loadedCategories.stream();
        if(nameF.isPresent()){
            theStream = theStream.filter(x -> x.getName().toUpperCase().contains(nameF.get().toUpperCase()));
        }
        if(descF.isPresent()){
            theStream = theStream.filter(x -> x.getDescription().toUpperCase().contains(descF.get().toUpperCase()));
        }

        obvCategories.clear();
        obvCategories.addAll(theStream.toList());
        categoryTable.getItems().clear();
        categoryTable.getItems().addAll(obvCategories);
    }
    @FXML
    protected void resetFilter(){
        nameFilter.clear();
        descriptionFilter.clear();
        obvCategories.setAll(loadedCategories);
        categoryTable.setItems(obvCategories);
    }
}
