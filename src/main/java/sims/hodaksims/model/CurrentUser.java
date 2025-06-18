package sims.hodaksims.model;

public class CurrentUser {
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
    private CurrentUser(User user){
        this.userCur = user;
    }
    public static CurrentUser getInstance(User user){
        if (instance == null) {
            instance = new CurrentUser(user);
        }
        return instance;
    }
    public static CurrentUser getInstance(){
        return instance;
    }

    public User getUserCur() {
        return userCur;
    }

    public void setUserCur(User userCur) {
        this.userCur = userCur;
    }
}
