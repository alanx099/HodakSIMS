package sims.hodaksims.model;

/**
 * Klasa Category služi za kategoriziranje naših proizvoda u sustavu
 */
public class Category extends Entity{
    private String name;
    private String description;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
