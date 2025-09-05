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
    private Set<Pair<Product, Integer>> inventar;

    /**
     * Warehouse konstruktor
     * @param name name
     * @param city city
     * @param postalCode postalCode
     * @param streetNumber streetNumber
     * @param country country
     * @param streetName streetNames
     */
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

    /**
     * Warehouse konstruktor
     * @param name name
     * @param city city
     * @param postalCode postalCode
     * @param streetNumber streetNumber
     * @param country country
     * @param streetName streetName
     * @param capacity capacitys
     */
    public Warehouse(String name, String city, String postalCode, String streetNumber, String country, String streetName, List<WareCapacity> capacity) {
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.streetNumber = streetNumber;
        this.country = country;
        this.streetName = streetName;
        this.capacity = capacity;
    }

    /**
     * setInventar postavi inventar
     * @param inventar
     */
    public void setInventar(Set<Pair<Product, Integer>> inventar) {
        this.inventar = inventar;
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
     * getCapacity dohvati kapacitet
     * @return capacity
     */
    public List<WareCapacity> getCapacity() {
        return capacity;
    }

    /**
     * setCapacity postavi kapacitet
     * @param capacity capacity
     */
    public void setCapacity(List<WareCapacity> capacity) {
        this.capacity = capacity;
    }

    /**
     * addCapacity dodaj kapacitet
     * @param capacity capacity
     */
    public void addCapacity(WareCapacity capacity) {
        this.capacity.add(capacity);
    }

    /**
     * getCity dohvati grad
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * setCity postavi grad
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * getPostalCode dohvati postanski broj
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * setPostalCode postavi postanski broj
     * @param postalCode postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * getStreetNumber dohvati kučni broj
     * @return streetNumber
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * setStreetNumber postavi kučni broj
     * @param streetNumber streetNumber
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * getCountry dohvati državu
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * setCountry postavi državu
     * @param country country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getStreetName dohvati ulicu
     * @return streetName
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * setStreetName postavi ulicu
     * @param streetName streetName
     */
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
        for(WareCapacity cap : capacity){
            props.append(cap.changesToString()[0]);
        }
        return props.toString().split(",");
    }

    /**
     * getInventarClass vraca inventar od objekta
     * @return inventar
     */
    public Set<Pair<Product, Integer>> getInventarClass() {
        return inventar;
    }
}
