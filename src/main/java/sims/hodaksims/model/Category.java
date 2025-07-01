package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.util.List;

/**
 * Klasa Category služi za kategoriziranje naših proizvoda u sustavu
 */
public class Category extends Entity implements Logable{
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
        return  name;
    }

    /**
     * @return
     */
    @Override
    public String[] changesToString() {
        String props = new String("name='" + name + '\'' +  ",description='" + description + '\'');

       return props.split(",");
    }
}
