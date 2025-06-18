package sims.hodaksims.model;

public class WareCapacity extends Entity{
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
}
