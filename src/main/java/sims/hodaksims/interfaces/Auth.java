package sims.hodaksims.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.User;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface Auth {
    static final Logger log = LoggerFactory.getLogger(Auth.class);
    public static boolean authorization(User usrAuth) {
        try{
            List<User> currUsers = User.loadUsers();
            User userCur = currUsers.stream().filter(user -> user.getPassword().equals(usrAuth.getPassword())&&user.getUsername().equals(usrAuth.getUsername())).findFirst().get();
            CurrentUser.getInstance(userCur);
            return true;
        } catch (IOException | NoSuchElementException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
