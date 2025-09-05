package sims.hodaksims.model;

/**
 * Klasa SupplierContact koristimo kako bi kontakt podatke
 * od dostavljača spojili s odgovarajućim podatcima
 */
public class SupplierContact extends Entity {
    private String name;
    private String email;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    /**
     * SupplierContact kontakti dostavljača konstruktor
     * @param name name
     * @param email email
     * @param phone phone
     * @param address address
     */
    public SupplierContact(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * setName postavi ime
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getEmail dohvati email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail postavi email
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getPhone dohvati telefon
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setPhone postavi telefon
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * getAddress dohvati adresu
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * setAddress postavi adressu
     * @param address address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SupplierContact{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
