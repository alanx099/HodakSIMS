package sims.hodaksims.controller.list;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sims.hodaksims.model.ChangeLog;
import sims.hodaksims.model.UserRoles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Klasa za prikaz promjena unutar aplikacije
 */
public class ListChanges {
    @FXML
    Button resetFilter;
    @FXML
    Button filterChanges;
    @FXML
    DatePicker dateFrom;
    @FXML
    DatePicker dateTo;
    @FXML
    TextField changeText;
    @FXML
    ComboBox<UserRoles> rolePicker;
    @FXML
    private TextArea changeExpanded;
    @FXML
    private TableView<ChangeLog> changesTable;
    @FXML
    private TableColumn<ChangeLog, String> roleColumn;
    @FXML
    private TableColumn<ChangeLog, String> changesColumn;
    @FXML
    private TableColumn<ChangeLog, String> timeChangedColumn;


    private final List<ChangeLog> loadedChanges = ChangeLog.loadChangeLog();
    private final ObservableList<ChangeLog> obvChanges = FXCollections.observableArrayList();
    private final ObservableList<UserRoles> roles =FXCollections.observableArrayList();
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize(){
        obvChanges.addAll(loadedChanges);
        roles.addAll(UserRoles.values());
        changesTable.getItems().addAll(obvChanges);
        roleColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getRoleNew().getName()));
        changesColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getPromjena()));
        timeChangedColumn.setCellValueFactory(cellData-> new SimpleStringProperty((cellData.getValue()).getDateLog().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"))));
        timeChangedColumn.setSortType(TableColumn.SortType.DESCENDING);
        changesTable.getSortOrder().add(timeChangedColumn);
        changesTable.sort();
        rolePicker.setItems(roles);
    }
    @FXML
    /**
     * Prebacuje odabranu promjenu u veći ekran
     */
    public void setBigDescription(){
        Optional<ChangeLog> row = Optional.ofNullable(changesTable.getSelectionModel().getSelectedItem());
        if(row.isPresent()){
            changeExpanded.setText(row.get().getPromjena());
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem s prikazom");
            alert.setHeaderText("Nema odabira");
            alert.setContentText("Molimo odaberite promjenu");
            alert.showAndWait();
        }

    }
    @FXML
    /**
     * resetFilters briše postavljene filtere
     */
    public void resetFilters(){
        rolePicker.getSelectionModel().clearSelection();
        dateFrom.setValue(null);
        dateTo.setValue(null);
        changeText.clear();
        obvChanges.setAll(loadedChanges);
        changesTable.setItems(obvChanges);
    }
    @FXML
    /**
     * applyFilters aktivira odabrane filtere
     */
    public void applyFilters(){
        Optional<String> changes = Optional.ofNullable(changeText.getText());
        Optional<UserRoles> rolesF = Optional.ofNullable(rolePicker.getSelectionModel().getSelectedItem());
        Optional<LocalDate> from = Optional.ofNullable(dateFrom.getValue());
        Optional<LocalDate> to = Optional.ofNullable(dateTo.getValue());
        Stream<ChangeLog> filteri = loadedChanges.stream();
        if(changes.isPresent()){
            filteri = filteri.filter(x -> x.getPromjena().toUpperCase().contains(changeText.getText().toUpperCase()));
        }
        if(rolesF.isPresent()){
            filteri = filteri.filter(x-> x.getRoleNew() == rolePicker.getSelectionModel().getSelectedItem());
        }
        if(from.isPresent()){
            filteri = filteri.filter(x-> x.getDateLog().toLocalDate().isAfter(dateFrom.getValue()));
        }
        if(to.isPresent()){
            filteri = filteri.filter(x-> x.getDateLog().toLocalDate().isBefore(dateTo.getValue()));
        }
        final List<ChangeLog> filtered = filteri.toList();
        obvChanges.setAll(filtered);
        changesTable.setItems(obvChanges);
        changesTable.getSortOrder().add(timeChangedColumn);
        changesTable.sort();
    }
}
