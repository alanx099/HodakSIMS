package sims.hodaksims.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.RepositoryIntegrityException;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.UserRoles;
import sims.hodaksims.model.View;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MenuController {
    @FXML
    Menu user;
    @FXML
    /**
     * switchToSceneListSkaldiste prebaci na LISTWAREHOUSE
     */
    protected void switchToSceneListSkaldiste(){
        ScreenManagerController.switchTo(View.LISTWAREHOUSE);
    }

    /**
     * switchToSceneAddSkaldiste prebaci na ADDWAREHOUSE
     */
    @FXML
    protected void switchToSceneAddSkaldiste() {
        ScreenManagerController.switchTo(View.ADDWAREHOUSE);
    }
    @FXML
    /**
     * switchToSceneListCategory prebaci LISTCATEGORY
     */
    protected void switchToSceneListCategory() {
        ScreenManagerController.switchTo(View.LISTCATEGORY);
    }
    @FXML
    /**
     * switchToSceneAddCategory prebaci ADDCATEGORY
     */
    protected void switchToSceneAddCategory() {
        ScreenManagerController.switchTo(View.ADDCATEGORY);
    }

    /**
     * switchToSceneChanges prebaci na LISTCHANGES
     */
    @FXML
    protected void switchToSceneChanges() {
        ScreenManagerController.switchTo(View.LISTCHANGES);
    }

    /**
     * switchToSceneAddSupplier prebaci na ADDSUPPLIER
     */
    @FXML
    protected void switchToSceneAddSupplier() {
        ScreenManagerController.switchTo(View.ADDSUPPLIER);
    }

    /**
     * switchToSceneListSupplier prebaci na  LISTSUPPLIER
     */
    @FXML
    protected void switchToSceneListSupplier() {
        ScreenManagerController.switchTo(View.LISTSUPPLIER);
    }

    /**
     * switchToSceneAddContract prebaci na ADDCONTRACT
     */
    @FXML
    protected void switchToSceneAddContract() {
        ScreenManagerController.switchTo(View.ADDCONTRACT);
    }

    /**
     * switchToSceneListContract prebaci na LISTCONTRACT
     */
    @FXML
    protected void switchToSceneListContract() { ScreenManagerController.switchTo(View.LISTCONTRACT);}
    /**
     * switchToSceneAddProduct prebaci na ADDPRODUCT
     */
    @FXML
    protected void switchToSceneAddProduct(){ScreenManagerController.switchTo(View.ADDPRODUCT);}
    /**
     * switchToSceneListProduct prebaci na  LISTPRODUCT
     */
    @FXML
    protected void switchToSceneListProduct() {ScreenManagerController.switchTo(View.LISTPRODUCT);}

    /**
     * switchToSceneInventory prebaci na INVENTORY
     */
    @FXML
    protected void switchToSceneInventory() {ScreenManagerController.switchTo(View.INVENTORY);}
    /**
     * switchToSceneRegister prebaci na REGISTER
     */
    @FXML
    protected void switchToSceneRegister() {ScreenManagerController.switchTo(View.REGISTER);}

    @FXML
    protected void switchToSceneLogout() {ScreenManagerController.switchTo(View.LOGIN);}

    @FXML
    Menu regCor;

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    /**
     * initialize postavlja podatke u javafx izbornike
     */
    public void initialize(){
        if(CurrentUser.getInstance().getUserCur().getRole().equals(UserRoles.ADMIN)){
            log.info(CurrentUser.getInstance().getUserCur().getRole().getName());
            regCor.setDisable(false);
        }
        user.setText("Welcome "+ CurrentUser.getInstance().getUserCur().getUsername() + "!" + "     Current Time    " +  LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> user.setText("Welcome "+ CurrentUser.getInstance().getUserCur().getUsername() + "!" + "     Current Time    " +  LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
        timer.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timer.play();
    }

}
