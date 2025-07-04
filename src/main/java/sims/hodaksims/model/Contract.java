package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.time.LocalDate;

public class Contract extends Entity implements Logable {
    private Supplier supplier;
    private Warehouse warehouse;
    private LocalDate startDate;
    private LocalDate endDate;

    public Contract(Supplier supplier, Warehouse warehouse, LocalDate startDate, LocalDate endDate) {
        this.supplier = supplier;
        this.warehouse = warehouse;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

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
