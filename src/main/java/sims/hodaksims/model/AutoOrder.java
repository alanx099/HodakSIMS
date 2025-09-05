package sims.hodaksims.model;

public class AutoOrder {
    Warehouse warehouse;
    Supplier supplier;
    Product product;
    Integer quantity;
    Integer trigger;

    public AutoOrder(Warehouse warehouse, Supplier supplier, Product product, Integer quantity, Integer trigger) {
        this.warehouse = warehouse;
        this.supplier = supplier;
        this.product = product;
        this.quantity = quantity;
        this.trigger = trigger;
    }

    public AutoOrder() {

    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTrigger() {
        return trigger;
    }

    public void setTrigger(Integer trigger) {
        this.trigger = trigger;
    }
}
