package sims.hodaksims.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.model.*;
import sims.hodaksims.repository.ContractRepository;
import sims.hodaksims.repository.InventoryDbActions;
import sims.hodaksims.repository.WarehouseRepository;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InventoryController {
    private static Logger log = LoggerFactory.getLogger(InventoryController.class);
    @FXML
    ComboBox<Warehouse> selectedWarehouse;
    @FXML
    ComboBox<Supplier> orderSupplier;
    @FXML
    ComboBox<Product> orderProduct;
    @FXML
    Slider orderAmmountSlider;
    @FXML
    Label orderAmmountLabel;
    @FXML
    ComboBox<Pair<Product, Integer>> productSend;
    @FXML
    Slider productAmmountSlider;
    @FXML
    Label sendAmmountLabel;
    @FXML
    TableView<Pair<Product, Integer>> productTable;
    @FXML
    TableColumn<Pair<Product, Integer>,String> productSku;
    @FXML
    TableColumn<Pair<Product, Integer>,String> productName;
    @FXML
    TableColumn<Pair<Product, Integer>, String> productAmmount;
    @FXML
    TextField productFilterName;
    @FXML
    TextField productFilterSku;
    @FXML
    TextField productFilterAmmountStart;
    @FXML
    TextField productFilterAmmountEnd;
    @FXML
    TextField productFilterMinimumStart;
    @FXML
    TextField productFilterMinimumEnd;
    @FXML
    TableView<WareCapacity> capacityTable;
    @FXML
    TableColumn<WareCapacity, String> categoryCapacity;
    @FXML
    TableColumn<WareCapacity, String> freeCapacity;
    @FXML
    TableColumn<WareCapacity, String> capacatyMax;
    @FXML
    TableColumn<WareCapacity, String> percentageLeft;
    @FXML
    TextArea deliLable;
    WarehouseRepository<Warehouse> warehouseRepository= new WarehouseRepository<>();
    ContractRepository<Contract> contractRepository= new ContractRepository<>();
    ObservableList<Pair<Product, Integer>> productQuantity = FXCollections.observableArrayList();
    Set<Pair<Supplier, List<Product>>> currSuppliers;
    ObservableList<Supplier> currSupplierList = FXCollections.observableArrayList();
    ObservableList<Product> supplierProducts = FXCollections.observableArrayList();
    ObservableList<WareCapacity> inventorySpace = FXCollections.observableArrayList();
    ObservableList<Supplier> cantOrder= FXCollections.observableArrayList();
    ObservableList<Pair<Supplier, Pair<Warehouse,Pair<Product,Integer>>>> realOrder = FXCollections.observableArrayList();

    public void initialize(){
        selectedWarehouseLogic();
        productTableLogic();
        waregouseCapacatyTableLogic();
        productOrderLogic();
        productSendingLogic();

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e ->{
                deliLable.setText("");
            for(Pair<Supplier, Pair<Warehouse,Pair<Product,Integer>>> s: realOrder){
               int curr =  s.getKey().getDeliveryTime();
               if(curr==0){
                   InventoryDbActions.deliver(s.getValue().getKey(), s.getValue().getValue().getKey(), s.getValue().getValue().getValue());
                   productQuantity.setAll(warehouseRepository.getInventory(selectedWarehouse.getSelectionModel().getSelectedItem().getId()));
                   inventorySpace.setAll(selectedWarehouse.getSelectionModel().getSelectedItem().getCapacity());
                   realOrder.remove(s);
               return;
               }
                s.getKey().setDeliveryTime(curr-1);
               deliLable.appendText(s.getKey().getName() + ", " +s.getKey().getDeliveryTime().toString()+"\n");
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    @FXML
    public void sendOrder(){
        if(orderSupplier.getSelectionModel().getSelectedItem()!= null)
        {
            for(Pair<Supplier, Pair<Warehouse,Pair<Product,Integer>>> s: realOrder){
                if (s.getKey().getId().equals(orderSupplier.getSelectionModel().getSelectedItem().getId())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pogreška");
                    alert.setHeaderText("Molimo pričekajte\n");
                    alert.setContentText("Molimo pričekajte ispunjenje naruđbe "+s.getKey().getName());
                    alert.showAndWait();
                    return;
                }
            }
            Integer amount = (int) Math.round(orderAmmountSlider.getValue());
            realOrder.add(new Pair<>(new Supplier(orderSupplier.getSelectionModel().getSelectedItem()), new Pair<> (selectedWarehouse.getSelectionModel().getSelectedItem(),new Pair<>(orderProduct.getSelectionModel().getSelectedItem(), amount))));
        }
    }

    private void selectedWarehouseLogic(){
        selectedWarehouse.getItems().setAll(warehouseRepository.findAll());
        selectedWarehouse.getSelectionModel().selectFirst();
        currSuppliers = contractRepository.getSuppliersFromWarehouse(selectedWarehouse.getSelectionModel().getSelectedItem().getId());
        for(Pair<Supplier, List<Product> >supPair : currSuppliers){
            currSupplierList.add(supPair.getKey());
        }
        productQuantity.setAll(warehouseRepository.getInventory(selectedWarehouse.getSelectionModel().getSelectedItem().getId()));

        selectedWarehouse.setOnAction(event -> {
            clearAll();
            currSupplierList.clear();
            currSuppliers = selectedWarehouse.getSelectionModel().getSelectedItem().toString().isEmpty() ? null: contractRepository.getSuppliersFromWarehouse(selectedWarehouse.getSelectionModel().getSelectedItem().getId());
            if(currSuppliers!= null){
                for(Pair<Supplier, List<Product> >supPair : currSuppliers){
                    currSupplierList.add(supPair.getKey());
                }
                inventorySpace.setAll(selectedWarehouse.getSelectionModel().getSelectedItem().getCapacity());
            }
            if(!selectedWarehouse.getSelectionModel().getSelectedItem().toString().isEmpty()){
                productQuantity.setAll(warehouseRepository.getInventory(selectedWarehouse.getSelectionModel().getSelectedItem().getId()));
            }
        });
    }
    private void productTableLogic(){
        productTable.setItems(productQuantity);
        productSku.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getSku()));
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getName()));
        productAmmount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
    }
    private void waregouseCapacatyTableLogic(){
        inventorySpace.setAll(selectedWarehouse.getSelectionModel().getSelectedItem().getCapacity());
        capacityTable.setItems(inventorySpace);
        categoryCapacity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));
        freeCapacity.setCellValueFactory(cellData -> new SimpleStringProperty(calculateMaxOrder(productQuantity, cellData.getValue().getCategory(), selectedWarehouse.getSelectionModel().getSelectedItem().getCapacity()).toString()));
        capacatyMax.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCapacity().toString()));
        percentageLeft.setCellValueFactory(cellData -> new SimpleStringProperty(calculatePercentage(productQuantity,cellData.getValue()) + "%"));
    }
    private void productSendingLogic(){
        productSend.setItems(productQuantity);
        productSend.setOnAction(event -> {
            if(productSend.getValue()!=null){
                productAmmountSlider.setDisable(false);
                productAmmountSlider.setMax(Double.parseDouble(productSend.getSelectionModel().getSelectedItem().getValue().toString()));
            }
        });
        productAmmountSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sendAmmountLabel.setText(Integer.toString(newValue.intValue()));
        });
    }
    private void productOrderLogic(){
        orderSupplier.setItems(currSupplierList);
        orderProduct.setItems(supplierProducts);
        orderSupplier.setOnAction(event -> {
            if(orderSupplier.getValue() != null){
                orderProduct.setDisable(false);
                supplierProducts.setAll(currSuppliers.stream()
                        .filter(x -> x.getKey().getId().equals(orderSupplier.getValue().getId()))
                        .findFirst()
                        .get().getValue());
            }}
        );
        orderProduct.setOnAction(event ->{
            if(orderProduct.getValue() != null){
                Double max = calculateMaxOrder(productQuantity, orderProduct.getSelectionModel().getSelectedItem().getCategory(), selectedWarehouse.getSelectionModel().getSelectedItem().getCapacity()).doubleValue();
                Double min = orderSupplier.getSelectionModel().getSelectedItem().getMinOrder().doubleValue();
                if(min<= max){
                    orderAmmountSlider.setDisable(false);
                    orderAmmountSlider.setMax(max);
                    orderAmmountSlider.setMin(min);
                }else{
                    clearOrderProduct();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Pogreška");
                    alert.setHeaderText("Molimo odaberite drugi proizvod\n");
                    alert.setContentText("Minimalna naruđba ovog dobavljača je veća od mjesta u skladištu ");
                    alert.showAndWait();
                }

            }
        });

        orderAmmountSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            orderAmmountLabel.setText(Integer.toString(newValue.intValue()));
        });
    }
    private Integer calculateStoredAmmount(ObservableList<Pair<Product, Integer>> products, Category category){
        return products.stream().filter(x -> x.getKey().getCategory().getName().equals(category.getName())).mapToInt(Pair::getValue).sum();
    }
    private Integer calculateMaxOrder(ObservableList<Pair<Product, Integer>> products, Category category, List<WareCapacity> capacity){
        return  capacity.stream().filter(x -> x.getCategory().getName().equals(category.getName())).findFirst().map(WareCapacity::getCapacity).orElse(0) - calculateStoredAmmount(products, category);
    }
    private BigDecimal calculatePercentage(ObservableList<Pair<Product, Integer>> products, WareCapacity wareCapacity){
        BigDecimal categorySum = BigDecimal.valueOf(calculateStoredAmmount(products, wareCapacity.getCategory()));
        BigDecimal productMax =  BigDecimal.valueOf(wareCapacity.getCapacity());
        BigDecimal hundred = BigDecimal.valueOf(100);
        if(productMax.equals(BigDecimal.ZERO)){
            return BigDecimal.ZERO;
        }
        return categorySum.divide(productMax,10, RoundingMode.HALF_UP).multiply(hundred).setScale(2, RoundingMode.HALF_UP);
    }
    public void clearAll(){
        clearOrderProduct();
        productAmmountSlider.setValue(0);
        productAmmountSlider.setDisable(true);
        sendAmmountLabel.setText("Kolicina");
        orderSupplier.getSelectionModel().clearSelection();
        orderProduct.setDisable(true);
        productSend.getSelectionModel().clearSelection();
    }
    public void clearOrderProduct() {
        orderProduct.getSelectionModel().clearSelection();
        orderAmmountLabel.setText("Kolicina");
        orderAmmountSlider.setValue(0);
        orderAmmountSlider.setDisable(true);
    }
    @FXML
    public void filterAll(){}

    @FXML
    public void clearOrderFields(){}
    @FXML
    public void clearSend(){}
    @FXML
    public void filterReset(){}
    @FXML
    public void sendProduct(){}
}
