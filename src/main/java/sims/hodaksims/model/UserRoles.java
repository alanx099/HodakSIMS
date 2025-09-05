package sims.hodaksims.model;

/**
 * User role enumeracija prikazuje moguće role unutar aplikacijes
 */
public enum UserRoles {
    USER,ADMIN;

    /**
     * getName dohvati ime
     * @return
     */
    public String getName(){
        return this.toString();
    }
    public boolean equals(UserRoles j, UserRoles k){
        return j.toString().compareTo(k.toString()) == 0;
    }
}
