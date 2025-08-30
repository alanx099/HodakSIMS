package sims.hodaksims.controller.update;

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
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateWarehouse<T extends Warehouse> extends AbstractUpdateController<T> {
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
    private Label naslov;
    @FXML
    private Label idPromjene;

    private final CategoryRepository<Category> allCategories = new CategoryRepository<>();
    private  ObservableList<Category> loadedCategories = FXCollections.observableArrayList(allCategories.findAll());
    private final ObservableList<WareCapacity> currCapacity = FXCollections.observableArrayList();
    private final WarehouseRepository<Warehouse> wareRepo = new WarehouseRepository<>();
    public void initialize(){
        capacityTable.setItems(currCapacity);
        categories.setItems(loadedCategories);
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

    @Override
    public void setFields(T item) {
        name.setText(item.getName());
        country.setText(item.getCountry());
        city.setText(item.getCity());
        postalNb.setText(item.getPostalCode());
        street.setText(item.getStreetName());
        streetNb.setText(item.getStreetNumber());
        currCapacity.addAll(item.getCapacity());
        capacityTable.setItems(currCapacity);
        naslov.setText("Ažuriranje skladišta: " + item.getName());
        idPromjene.setText(item.getId().toString());
        Set<String> kategorijeString =  currCapacity.stream().map(x->x.getCategory().getName()).collect(Collectors.toSet());
            loadedCategories = FXCollections.observableArrayList(loadedCategories.stream()
                    .filter(x -> !kategorijeString.contains(x.getName())).toList());

        categories.setItems(loadedCategories);

    }
    @FXML
    public void beforePushToTable() {
        Map<String, String> required = Map.of("Kategorija", Objects.toString(this.categories.getSelectionModel().getSelectedItem(), ""),"Količina", this.setAmmount.getText());
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        Map<String, String> numbersMap = Map.of("Kolicina", this.setAmmount.getText());
        Boolean numbers = InputVerifyUtil.checkForNumber(numbersMap);
        if (Boolean.TRUE.equals(requiredCheck) && Boolean.TRUE.equals(numbers)) {
            pushToTable();
        }
    }
    public void pushToTable(){
        Category curCat = categories.getValue();
        Integer ammount = Integer.parseInt(setAmmount.getText());
        WareCapacity capacityToPush = new WareCapacity(curCat, ammount);
        loadedCategories.remove(categories.getSelectionModel().getSelectedItem());
        currCapacity.add(capacityToPush);
    }
    public void deleteSelectedCapacity(){
        if(capacityTable.getSelectionModel().getSelectedItem()==null)return;
        WareCapacity cap = capacityTable.getSelectionModel().getSelectedItem();
        loadedCategories.add(cap.getCategory());
        currCapacity.remove(cap);
    }
    @FXML
    public void beforeInsert(){
        Map<String, String> required = Map.of("Ime",name.getText(),"Država",country.getText(), "Grad",city.getText(),"Poštanski broj", postalNb.getText(),"Ulica",street.getText(),"Kučni broj", streetNb.getText(), "Kapacitet", currCapacity.isEmpty()?"":"true" );
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)){
            insertToDb();
        }
    }
    public void insertToDb(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ažuriranje skladišta");
        alert.setHeaderText("Ažuriranjeskladišta");
        alert.setContentText("Jeste li sigurni da ažurirati skladište: " + name.getText());
        Optional<ButtonType> answer= alert.showAndWait();

        if(answer.isPresent() && answer.get()== ButtonType.OK){
            Long id = Long.parseLong(idPromjene.getText());
            String wName = name.getText();
            String wCountry = country.getText();
            String wCity = city.getText();
            String wPostal = postalNb.getText();
            String wStreet = street.getText();
            String wStreetNb = streetNb.getText();

            Warehouse curWare = new Warehouse(wName, wCity, wPostal, wStreetNb, wCountry ,wStreet, currCapacity);
            curWare.setId(id);
            wareRepo.update(curWare);
            switchToSceneListSkaldiste();
        }
    }
    @FXML
    protected void switchToSceneListSkaldiste() {
        ScreenManagerController.switchTo(View.LISTWAREHOUSE);
    }

}
