package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.View;
import sims.hodaksims.model.Warehouse;
import sims.hodaksims.repository.AbstractRepository;
import sims.hodaksims.repository.WarehouseRepository;

import java.util.List;

public class WelcomeController {
    @FXML
    Label motd;
    public void initialize(){
        //this is empty curr
    }

   @FXML
    protected void switchToSceneLogin() {
       ScreenManagerController.switchTo(View.LOGIN);
    }
}