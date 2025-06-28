package sims.hodaksims.interfaces;
import sims.hodaksims.exceptions.BadCredentialsException;
import sims.hodaksims.model.CurrentUser;
import sims.hodaksims.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.repository.UsersRepository;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public sealed interface Authenticable permits CurrentUser, User {
    static final Logger log = LoggerFactory.getLogger(Authenticable.class);
    UsersRepository<User> userRep = new UsersRepository<>();
    default void authenticate(User usrAuth) throws BadCredentialsException {
        try{
            log.info("Kikiriki7");
            List<User> currUsers = userRep.findAll();

            User userCur = currUsers.stream().filter(user -> user.getPassword().equals(usrAuth.getPassword())&&user.getUsername().equals(usrAuth.getUsername())).findFirst().orElseThrow(()-> new BadCredentialsException("Incorrect username or password"));
            CurrentUser.getInstance(userCur);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
    }

}
