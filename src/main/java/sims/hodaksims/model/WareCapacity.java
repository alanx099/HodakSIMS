package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

/**
 * Klasa WareCApacity opisuje sklatište na način da se zna koju kategoriju
 * stvari podržava i koliko je trenutno iskorišteno
 */
public class WareCapacity extends Entity implements Logable {
    private Integer capacity;
    private Integer filled;
    private Category category;

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getFilled() {
        return filled;
    }

    public void setFilled(Integer filled) {
        this.filled = filled;
    }

    public WareCapacity(Category category, Integer capacity) {
        this.category = category;
        this.capacity = capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Category getCategory() {
        return category;
    }

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
                " category=" + category +
                "capacity=" + capacity +
                ',';

        return props.split(",");
    }
}
