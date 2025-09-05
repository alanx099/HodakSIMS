package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

/**
 * Klasa WareCApacity opisuje sklatište na način da se zna koju kategoriju
 * stvari podržava i koliko je trenutno iskorišteno
 */
public class WareCapacity extends Entity implements Logable {
    private Integer capacity;
    private Category category;

    /**
     * getCapacity  dohvati kapacitet
     * @return capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * WareCapacity konstruktor
     * @param category category
     * @param capacity capacity
     */
    public WareCapacity(Category category, Integer capacity) {
        this.category = category;
        this.capacity = capacity;
    }

    /**
     * setCapacity postavi kapacitet
     * @param capacity capacity
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * getCategory dohvati kategoriju
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * setCategory postavi kategoriju
     * @param category category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "WareCapacity{" +
                "capacity=" + capacity +
                ", category=" + category +
                '}';
    }

    @Override
    public String[] changesToString() {
        String props=
                ",category=" + category +
                " capacity=" + capacity
                ;

        return new String[]{props};
    }
}
