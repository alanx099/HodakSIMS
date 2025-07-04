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

    public SupplierContact(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

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
