package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sims.hodaksims.interfaces.Auth;
import sims.hodaksims.model.User;
import sims.hodaksims.model.View;

public class LoginController {
    @FXML
    TextField userName;
    @FXML
    TextField password;
    @FXML
    Button login;


    public void goToWelcome(){
        String user = userName.getText();
        String pass = password.getText();
        User currUser = new User.UserBuilder(user).setPassword(pass).build();
        if(Auth.authorization(currUser)){
            ScreenManagerController.switchTo(View.WELCOME);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Korisnički podatci neispravni");
            alert.setContentText("Neispravni");
            alert.showAndWait();
        }
    }
    public void goToRegister(){
        ScreenManagerController.switchTo(View.REGISTER);
    }

}
