package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product klasa
 */
public class Product extends Entity  implements Logable {
    private String sku;
    private String name;
    private BigDecimal price;
    private Category category;
    private List<Supplier> suppliers;
    //todo napraviti da je Big decimal price u bazi


    public Product(String sku, String name, BigDecimal price, Category category) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String sku, String name, BigDecimal price, Category category, List<Supplier> suppliers) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
        this.suppliers = suppliers;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", suppliers=" + suppliers +
                '}';
    }

    @Override
    public String[] changesToString() {
        StringBuilder props = new StringBuilder();
        props.append("sku='").append(sku).append('\'')
                .append(", name='").append(name).append('\'')
                .append(", price=").append(price)
                .append(", category=").append(category);
        for(Supplier supplier : suppliers) {
            props.append(supplier.changesToString()[0]);
        }
        return props.toString().split(",");
    }
}
