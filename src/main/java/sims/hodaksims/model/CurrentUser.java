package sims.hodaksims.model;
import sims.hodaksims.interfaces.Authenticable;

public non-sealed class CurrentUser implements Authenticable  {
    /**
     * Singleton klasa koja prati trenutnog korisnika
     * <p>
     *     Ova klasa implementira singleteon design pattern koji osigurava da postoji samo
     *     jedna instanca trenutnog korisnika, te uz male modifikacije je ta instanca dostupna
     *     kroz cijelu aplikaciju što je praktično kod autorizacije i korištenje rola.
     * </p>
     */
    private static CurrentUser instance;
    private User userCur;

    /**
     * CurrentUser konstruktor
     * @param user
     */
    private CurrentUser(User user){
        this.userCur = user;
    }

    /**
     * getInstance dohvati instancu korisnika
     * @param user user
     * @return instance
     */
    public static CurrentUser getInstance(User user){
        if (instance == null) {
            instance = new CurrentUser(user);
        }
        return instance;
    }

    /**
     * CurrentUser dohvati instancu
     * @return instance
     */
    public static CurrentUser getInstance(){
        return instance;
    }

    /**
     * getUserCur dohvati trenutnog korisnika
     * @return User
     */
    public User getUserCur() {
        return userCur;
    }

    /**
     * setUserCur postavi korisnika
     * @param userCur userCur
     */
    public void setUserCur(User userCur) {
        this.userCur = userCur;
    }
}
