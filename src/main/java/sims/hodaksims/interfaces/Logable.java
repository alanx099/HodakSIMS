package sims.hodaksims.interfaces;


/**
 * Logable sučelje koje implementiraju klase kojima želimo pratiti promjene
 */
public interface Logable {
    /**
     * Promjene nad objektom u formatu string arraya
     * @return array za usporedbu
     */
    public String[] changesToString();
}
