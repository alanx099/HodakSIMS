package sims.hodaksims.model;

public class CurrentUser {
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
