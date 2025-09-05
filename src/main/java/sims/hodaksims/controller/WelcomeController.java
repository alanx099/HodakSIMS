package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sims.hodaksims.model.View;

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