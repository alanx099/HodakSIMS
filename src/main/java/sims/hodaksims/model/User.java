package sims.hodaksims.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.interfaces.Authenticable;
import sims.hodaksims.repository.UsersRepository;

import java.io.*;
import java.util.List;
import java.util.Objects;

public non-sealed class User extends Entity implements Serializable, Authenticable {
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
    private String username;
    private String password;
    private UserRoles role;

    /**
     * Privatni konstrukor koji se poziva u podklasi
     * 
     * @param builder
     */
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
     * Javno dostupna klasa za buildanje user klase
     */
    public static class UserBuilder extends Entity{
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
        public UserBuilder settId(Long uId){
            this.setId(uId);
            return this;
        }
        /**
         * završna metoda za instanciranje user objekta
         * @return
         */
        public User build(){
            return new User(this);
        }

    }

}
