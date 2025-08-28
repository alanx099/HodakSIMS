package sims.hodaksims.model;

import sims.hodaksims.interfaces.Logable;

import java.util.*;

/**
 * Warehouse klasa utjelovljuje entitete naših skladišta
 * te unutar seve sadrži liste kapaciteta i proizvoda unutar
 * skladišta
 */
public class Warehouse extends Entity implements Logable {
    private String name;
    private String city;
    private String postalCode;
    private String streetNumber;
    private String country;
    private String streetName;
    private List<WareCapacity> capacity;
    private Set<Product> inventar;

    public Warehouse(String name, String city, String postalCode, String streetNumber, String country, String streetName) {
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.streetNumber = streetNumber;
        this.country = country;
        this.streetName = streetName;
        this.capacity = new ArrayList<>();
        this.inventar = new HashSet<>();
    }
    public Warehouse(String name, String city, String postalCode, String streetNumber, String country, String streetName, List<WareCapacity> capacity) {
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.streetNumber = streetNumber;
        this.country = country;
        this.streetName = streetName;
        this.capacity = capacity;
    }
        public Set<Product> getInventar() {
        return inventar;
    }

    public void setInventar(Set<Product> inventar) {
        this.inventar = inventar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WareCapacity> getCapacity() {
        return capacity;
    }

    public void setCapacity(List<WareCapacity> capacity) {
        this.capacity = capacity;
    }
    public void addCapacity(WareCapacity capacity) {
        this.capacity.add(capacity);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String[] changesToString() {
        StringBuilder props = new StringBuilder();
        props.append("name='").append(name).append('\'')
                .append(", city='").append(city).append('\'')
                .append(", postalCode='").append(postalCode).append('\'')
                .append(", streetNumber='").append(streetNumber).append('\'')
                .append(", country='").append(country).append('\'')
                .append(", streetName='").append(streetName).append('\'');
              //  .append(", inventar=").append(inventar);
        for(WareCapacity cap : capacity){
            props.append(cap.changesToString()[0]);

        }
        return props.toString().split(",");
    }
}
