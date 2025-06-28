package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.View;

public class MenuController {
    @FXML
    Menu user;

    @FXML
    protected void switchToSceneAddSkaldiste() {
        ScreenManagerController.switchTo(View.ADDWAREHOUSE);
    }
    @FXML
    protected void logout() {
        ScreenManagerController.switchTo(View.LOGIN);
    }
    public void initialize(){
        user.setText("Welcome "+ CurrentUser.getInstance().getUserCur().getUsername() + "!");
    }

}
