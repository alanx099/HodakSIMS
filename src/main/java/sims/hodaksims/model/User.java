package sims.hodaksims.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.interfaces.Auth;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class User extends Entity implements Serializable, Auth {
    /**
     * User klasa namjenjena za instancijanje korisnika
     * <p>
     *     Ova klasa nam pomaže s baratanjem s korisnicima vrlo intuitivno.
     * </p>
     * <p>
     *     Ova klasa koristi Builder design pattern kako bi instanciranje bilo
     *     jednostavno i brzo. Uz to ova klasa implementira sučelja Serializable
     *     i Auth. Serializable za nas služi kako bi lako dohvatili podatke iz
     *     datoteke, te sučelje auth kako bi autentificirali korisnike prilikom
     *     prijave
     * </p>
     */
    private static final Logger log = LoggerFactory.getLogger(User.class);
    private String username;
    private String password;
    private UserRoles role;
    private static List<User> userList;
    private User(UserBuilder builder) {
        this.username=builder.username;
        this.password=builder.password;
        this.role = builder.role;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    /**
     * Metoda za spremanje listu korisnika u binarnu datoteku koja sadrži naše korisnike
     * @param users
     * @throws IOException
     */
    public static void saveUsers(List<User> users) throws IOException {
        try(FileOutputStream fileOut = new FileOutputStream("users.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut)){
        out.writeObject(users);
        }
    }

    /**
     * Metoda koja učitava korisnike u statičnu varijablu user ali također i vraća tu istu listu korisnika
     * @return List<User>
     * @throws IOException
     */
    public static List<User> loadUsers()throws IOException {
        try (
            FileInputStream fileIn = new FileInputStream("users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn)
        ){
            User.userList = (List<User>) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return User.userList;
    }



    public static class UserBuilder{
        private final String username;
        private String password;
        private UserRoles role;
        public  UserBuilder(String user){
            this.username = user;
        }
        public UserBuilder setPassword(String pwd){
            this.password = pwd;
            return this;
        }
        public UserBuilder setRole(UserRoles roleSet){
            this.role = roleSet;
            return this;
        }

        public User build(){
            return new User(this);
        }

    }

}
