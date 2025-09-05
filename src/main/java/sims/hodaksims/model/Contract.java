package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.time.LocalDate;

/**
 * Contract klasa za ugovorne objekte
 */
public class Contract extends Entity implements Logable {
    private Supplier supplier;
    private Warehouse warehouse;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Contract konstruktor
     * @param supplier supplier
     * @param warehouse warehouse
     * @param startDate startDate
     * @param endDate endDate
     */
    public Contract(Supplier supplier, Warehouse warehouse, LocalDate startDate, LocalDate endDate) {
        this.supplier = supplier;
        this.warehouse = warehouse;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * getSupplier dohvati dobavljača
     * @return supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * setSupplier
     * @param supplier supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    /**
     * getWarehouse dohvati skladište
     * @return warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * setWarehouse postavi skladište
     * @param warehouse  warehouse
     */
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * getStartDate dohvati datum
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * setStartDate postavi datum
     * @param startDate startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * getEndDate dohvati krajnji datum
     * @return endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * setEndDate postavi krajnji datum
     * @param endDate endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "supplier=" + supplier +
                ", warehouse=" + warehouse +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public String[] changesToString() {
            String props = ("supplier=" + supplier + ",warehouse=" + warehouse + ",startDate=" + startDate + ",endDate=" + endDate);
            return props.split(",");
    }
}
