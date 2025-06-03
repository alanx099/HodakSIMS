package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.View;

public class WelcomeController {
    @FXML
    Label motd;
    public void initialize(){
        motd.setText(CurrentUser.getInstance().getUserCur().getUsername() + ","  + CurrentUser.getInstance().getUserCur().getRole().toString());

    }

   @FXML
    protected void switchToSceneLogin() {
       ScreenManagerController.switchTo(View.LOGIN);
    }
}