package sims.hodaksims.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.exceptions.RepositoryAccessException;
import sims.hodaksims.model.User;
import sims.hodaksims.model.UserRoles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UsersRepository<T extends User>{
    private static final Logger log = LoggerFactory.getLogger(UsersRepository.class);
    private static final String USERS_FILE_PATH = "dat/users.txt";
    private static final Integer ROWS_PER_USER = 4;
    public T findById(Long var1) throws RepositoryAccessException {
        return null;
    }

    public List<T> findAll() throws RepositoryAccessException {
        List<T> users = new ArrayList<>();

        try(Stream<String> stream = Files.lines(Path.of(USERS_FILE_PATH))){
                List<String> fileRows = stream.toList();
                for(Integer userNumber = 0; userNumber < (fileRows.size() / ROWS_PER_USER); userNumber++)
                {
                Long id = Long.parseLong(fileRows.get(userNumber * ROWS_PER_USER));
                String username =  fileRows.get(userNumber * ROWS_PER_USER+1);
                String password = fileRows.get(userNumber * ROWS_PER_USER+2);
                String role = fileRows.get(userNumber * ROWS_PER_USER+3);
                User user =  new User.UserBuilder(username).setPassword(password).setRole(UserRoles.valueOf(role)).setId(id).build();
                users.add((T)user);
                }
        }catch(IOException _){
            log.error("Problem opening file " + USERS_FILE_PATH);
        }

        return users;
    }

    public void save(List<T> users) throws RepositoryAccessException/*Duplicate user error, Weak password*/ {
            try(PrintWriter writer = new PrintWriter(USERS_FILE_PATH)){
                for(T user : users){
                    writer.println(user.getId());
                    writer.println(user.getUsername());
                    writer.println(user.getPassword());
                    writer.println(user.getRole());
                }
                writer.flush();
            }catch(FileNotFoundException _){

                log.error("Problem accesing file " + USERS_FILE_PATH);
            }
    }
}
