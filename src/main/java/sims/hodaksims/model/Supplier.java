package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.time.LocalDate;
import java.util.Objects;
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

    /***
     * Supplier konstruktor
     * @param selectedItem selectedItem
     */
    public Supplier(Supplier selectedItem) {
        super.setId(selectedItem.getId());
        this.name = selectedItem.getName();
        this.oib = selectedItem.getOib();
        this.deliveryTime = selectedItem.getDeliveryTime();

    }

    /**
     * getContacts dohvati kontakte
     * @return
     */
    public Set<SupplierContact> getContacts() {
        return contacts;
    }

    /**
     * Postavi kontakte
     * @param contacts  kontakte
     */
    public void setContacts(Set<SupplierContact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Supplier Konstruktor
     * @param name name
     * @param oib oib
     * @param minOrder minOrder
     * @param deliveryTime deliveryTime
     * @param joined joined
     */
    public Supplier(String name, String oib, Integer minOrder, Integer deliveryTime, LocalDate joined) {
        this.name = name;
        this.oib = oib;
        this.minOrder = minOrder;
        this.deliveryTime = deliveryTime;
        this.joined = joined;
    }

    /**
     * Supplier konstruktor
     * @param contacts contacts
     * @param name name
     * @param oib oib
     * @param minOrder minOrder
     * @param deliveryTime deliveryTime
     */
    public Supplier(Set<SupplierContact> contacts, String name, String oib, Integer minOrder, Integer deliveryTime) {
        this.contacts = contacts;
        this.deliveryTime = deliveryTime;
        this.minOrder = minOrder;
        this.oib = oib;
        this.name = name;
    }

    /**
     * getMinOrder dohvati minimalnu naruđbu
     * @return minOrder
     */
    public Integer getMinOrder() {
        return minOrder;
    }

    /**
     * setMinOrder postavi minimalnu naruđbu
     * @param minOrder minOrder
     */
    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    /**
     * getDeliveryTime dohvati vrijeme dostave
     * @return
     */
    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * setDeliveryTime postavi vijeme dostave
     * @param deliveryTime deliveryTime
     */
    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * setJoined postavi datum unosa
     * @param joined joined
     */
    public void setJoined(LocalDate joined) {
        this.joined = joined;
    }

    /**
     * getName dohvati ime
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
     * getOib dohvati oib
     * @return
     */
    public String getOib() {
        return oib;
    }

    /**
     * setOib postavi oib
     * @param oib oib
     */
    public void setOib(String oib) {
        this.oib = oib;
    }

    /**
     * getJoined dohvati datum pristupa
     * @return
     */
    public LocalDate getJoined() {
        return joined;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(name, supplier.name) && Objects.equals(oib, supplier.oib) && Objects.equals(minOrder, supplier.minOrder) && Objects.equals(deliveryTime, supplier.deliveryTime) && Objects.equals(joined, supplier.joined) && Objects.equals(contacts, supplier.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, oib, minOrder, deliveryTime, joined, contacts);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String[] changesToString() {
        String props = ("name='" + name + '\'' +  ",oib='" + oib + '\'' + ",minOrder=" + minOrder + ",deliveryTime=" + deliveryTime + ",joined=" + joined);
        return props.split(",");
    }
}
