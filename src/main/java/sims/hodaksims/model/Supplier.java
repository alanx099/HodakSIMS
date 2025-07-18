package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.time.LocalDate;
import java.util.Set;

/**
 * Supplier klasa nam definira naše dobavljače proizvoda
 */
public class Supplier extends Entity implements Logable {
    private String name;
    private String oib;
    private Integer minOrder;
    private Integer deliveryTime;
    private LocalDate joined;
    private Set<SupplierContact> contacts;

    public Set<SupplierContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<SupplierContact> contacts) {
        this.contacts = contacts;
    }

    public Supplier(String name, String oib, Integer minOrder, Integer deliveryTime, LocalDate joined) {
        this.name = name;
        this.oib = oib;
        this.minOrder = minOrder;
        this.deliveryTime = deliveryTime;
        this.joined = joined;
    }

    public Supplier(Set<SupplierContact> contacts, String name, String oib, Integer minOrder, Integer deliveryTime) {
        this.contacts = contacts;
        this.deliveryTime = deliveryTime;
        this.minOrder = minOrder;
        this.oib = oib;
        this.name = name;
    }

    public Integer getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setJoined(LocalDate joined) {
        this.joined = joined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }


    public LocalDate getJoined() {
        return joined;
    }


    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", oib='" + oib + '\'' +
                ", minOrder=" + minOrder +
                ", deliveryTime=" + deliveryTime +
                ", joined=" + joined +
                ", contacts=" + contacts +
                '}';
    }

    @Override
    public String[] changesToString() {
        String props = ("name='" + name + '\'' +  ",oib='" + oib + '\'' + ",minOrder=" + minOrder + ",deliveryTime=" + deliveryTime + ",joined=" + joined);
        return props.split(",");
    }
}
