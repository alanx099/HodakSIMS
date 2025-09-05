package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product klasa za proizvode
 */
public class Product extends Entity  implements Logable {
    private String sku;
    private String name;
    private BigDecimal price;
    private Category category;
    private List<Supplier> suppliers;

    /**
     *  Product konstruktor
     * @param sku sku
     * @param name  name
     * @param price price
     * @param category category
     */
    public Product(String sku, String name, BigDecimal price, Category category) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * Product konstruktor s dobavljačima
     * @param sku sku
     * @param name name
     * @param price price
     * @param category category
     * @param suppliers suppliers
     */
    public Product(String sku, String name, BigDecimal price, Category category, List<Supplier> suppliers) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
        this.suppliers = suppliers;
    }

    /**
     * getSuppliers dohvati dobavljače
     * @return
     */
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    /**
     * setSuppliers postavi dobavljače
     * @param suppliers suppliers
     */
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * getSku dohvati sku
     * @return sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * setSku postavi sku
     * @param sku sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * getName dohvbati ime
     * @return name
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

    /**
     * getPrice dohvati cijenu
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * setPrice postavij cijenu
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * getCategory dohvati kategoriju
     * @returncategory
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
        return  name + "  sku:" + sku;
    }

    @Override
    public String[] changesToString() {
        StringBuilder props = new StringBuilder();
        props.append("sku='").append(sku).append('\'')
                .append(", name='").append(name).append('\'')
                .append(", price=").append(price).append('\'')
                .append(", category=").append(category).append('\'');
        for(Supplier supplier : suppliers) {
            props.append(supplier.changesToString()[0]);
        }
        return props.toString().split(",");
    }
}
