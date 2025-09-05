package sims.hodaksims.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sims.hodaksims.exceptions.BadCredentialsException;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.User;
import sims.hodaksims.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginController {
    /**
     * Login controller klasa koja upravlja login ekranom
     *
     * <p>
     *     Ova klasa upravlja s login ekranom za korisnike.
     * </p>
     */
    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    @FXML
    TextField userName;
    @FXML
    TextField password;
    @FXML
    Button login;

    /**
     * goToWelcome autenticira korisnika i prebacuje ga u aplikaciju
     */
    public void goToWelcome(){
        String user = userName.getText();
        String pass = password.getText();

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(pass.getBytes());

            User currUser = new User.UserBuilder(user).setPassword(Arrays.toString(hashBytes)).build();
            currUser.authenticate(currUser);

        CurrentUser curUsr = CurrentUser.getInstance();
        curUsr.authenticate(currUser);
        ScreenManagerController.switchTo(View.LISTCHANGES);
        }catch(BadCredentialsException _){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Korisnički podatci neispravni");
            alert.setContentText("Neispravni");
            alert.showAndWait();
            log.info("Krivi unos korisničkih podataka ");
        }catch(NoSuchAlgorithmException e){
            log.info(e.getMessage());
        }

    }

    /**
     * goToRegister prebacuje korisnika za registraciju
     */
    public void goToRegister(){
        ScreenManagerController.switchTo(View.REGISTER);
    }

}
