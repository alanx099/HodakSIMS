package sims.hodaksims.model;

import java.util.List;

public class Warehouse extends Entity{
    private String name;
    private String city;
    private String postalCode;
    private String streetNumber;
    private String country;
    private String streetName;
    private List<WareCapacity> capacity;
    private List<Product> inventar;

    public Warehouse(String name, String city, String postalCode, String streetNumber, String country, String streetName) {
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.streetNumber = streetNumber;
        this.country = country;
        this.streetName = streetName;
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
        public List<Product> getInventar() {
        return inventar;
    }

    public void setInventar(List<Product> inventar) {
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
        return "Warehouse{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", country='" + country + '\'' +
                ", streetName='" + streetName + '\'' +
                ", capacity=" + capacity +
                ", inventar=" + inventar +
                '}';
    }
}
