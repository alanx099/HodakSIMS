package sims.hodaksims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.model.User;
import sims.hodaksims.model.UserRoles;
import sims.hodaksims.model.View;
import sims.hodaksims.repository.UsersRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


public class RegisterController  {
    /**
     * Klasa za upravljanje register prozora
     *
     * <p>Ova klasa služi za kontrolu register prozora, na ovom prozoru unosimo nove korisnike</p>
     */
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    @FXML
    TextField userNameField;
    @FXML
    TextField passwordField;
    @FXML
    ComboBox<UserRoles> rolesBox;

    public void initialize(){
        rolesBox.getItems().addAll(UserRoles.values());
    }

    /**
     * Go back meoda usmjeruje korisnika natrag na početni login ekran ukoliko nije potrebno
     * dodavanje korisnika
     */
    public void goBack(){
        ScreenManagerController.switchTo(View.LOGIN);
    }

    /**
     * enterUser Metoda unosi korisnika u daoteku te nakon uspješnog unosa vraća korisnika na ekran za prijavu
     */
    public void enterUser(){
        String userName = userNameField.getText();
        String password = passwordField.getText();
        UserRoles role = rolesBox.getValue();
        UsersRepository<User> userRep  = new UsersRepository<>();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            User newUser = new User.UserBuilder(userName).setPassword(Arrays.toString(hashBytes)).setRole(role).build();
            List<User> updatedUsers = userRep.findAll();
            updatedUsers.add(newUser);
            userRep.save(updatedUsers);

        } catch (NoSuchAlgorithmException e){
            log.info(e.getMessage());
        }
        ScreenManagerController.switchTo(View.LOGIN);
    }

}
