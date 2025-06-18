package sims.hodaksims.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Supplier klasa nam definira naše dobavljače proizvoda
 */
public class Supplier extends Entity{
    private String name;
    private String oib;
    private Integer minOrder;
    private Integer deliveryTime;
    private LocalDateTime joined;
    private List<SupplierContact> contact;
    //private Set<Contract> contracts;
    private Set<Warehouse> warehouses;

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


    public LocalDateTime getJoined() {
        return joined;
    }

    public void setJoined(LocalDateTime joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", oib='" + oib + '\'' +
                ", joined=" + joined +
                '}';
    }
}
