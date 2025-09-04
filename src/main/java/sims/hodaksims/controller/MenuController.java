package sims.hodaksims.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.util.Duration;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.View;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MenuController {
    @FXML
    Menu user;

    @FXML
    protected void switchToSceneListSkaldiste(){
        ScreenManagerController.switchTo(View.LISTWAREHOUSE);
    }

    @FXML
    protected void switchToSceneAddSkaldiste() {
        ScreenManagerController.switchTo(View.ADDWAREHOUSE);
    }
    @FXML
    protected void switchToSceneListCategory() {
        ScreenManagerController.switchTo(View.LISTCATEGORY);
    }
    @FXML
    protected void switchToSceneAddCategory() {
        ScreenManagerController.switchTo(View.ADDCATEGORY);
    }
    @FXML
    protected void logout() {
        ScreenManagerController.switchTo(View.LISTCHANGES);
    }
    @FXML
    protected void switchToSceneChanges() {
        ScreenManagerController.switchTo(View.LISTCHANGES);
    }
    @FXML
    protected void switchToSceneAddSupplier() {
        ScreenManagerController.switchTo(View.ADDSUPPLIER);
    }
    @FXML
    protected void switchToSceneListSupplier() {
        ScreenManagerController.switchTo(View.LISTSUPPLIER);
    }
    @FXML
    protected void switchToSceneAddContract() {
        ScreenManagerController.switchTo(View.ADDCONTRACT);
    }
    @FXML
    protected void switchToSceneListContract() { ScreenManagerController.switchTo(View.LISTCONTRACT);}
    @FXML
    protected void switchToSceneAddProduct(){ScreenManagerController.switchTo(View.ADDPRODUCT);}
    @FXML
    protected void switchToSceneListProduct() {ScreenManagerController.switchTo(View.LISTPRODUCT);}
    @FXML
    protected void switchToSceneInventory() {ScreenManagerController.switchTo(View.INVENTORY);}
    public void initialize(){

        user.setText("Welcome "+ CurrentUser.getInstance().getUserCur().getUsername() + "!" + "     Current Time    " +  LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            user.setText("Welcome "+ CurrentUser.getInstance().getUserCur().getUsername() + "!" + "     Current Time    " +  LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }

}
