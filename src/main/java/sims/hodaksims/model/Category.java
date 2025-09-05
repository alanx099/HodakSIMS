package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;


/**
 * Klasa Category služi za kategoriziranje naših proizvoda u sustavu
 */
public class Category extends Entity implements Logable{
    private String name;
    private String description;

    /**
     * Category konstruktor
     * @param name name
     * @param description description
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }


    /**
     * getDescription dohvati desctiption
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription postavi description
     * @param description descriptions
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getName dohvati ime
     * @return ime
     */
    public String getName() {
        return name;
    }

    /**
     * setName postavi ime
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name;
    }

    @Override
    public String[] changesToString() {
        String props = "name='" + name + '\'' +  ",description='" + description + '\'';
       return props.split(",");
    }
}
