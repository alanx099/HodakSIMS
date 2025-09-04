package sims.hodaksims.model;

public enum View {
    /**
     * View enumeracija za laku izmjenu ekrana
     * Ova enumeracija sadrži putanje prema ekranima koji su nam dostupni,
     * također se služimo konstrktorom i getFileName metodi kako bi nam pozivanje
     * unutar koda izgledalo čisto i pregledno.
     */
    INVENTORY("Inventory.fxml"),
    UPDATECONTRACT("UpdateContract.fxml"),
    LISTCONTRACT("ListContract.fxml"),
    ADDCONTRACT("AddContract.fxml"),
    ADDPRODUCT("AddProduct.fxml"),
    UPDATEPRODUCT("UpdateProduct.fxml"),
    LISTPRODUCT("ListProduct.fxml"),
    ADDSUPPLIER("AddSupplier.fxml"),
    UPDATESUPPLIER("UpdateSupplier.fxml"),
    LISTSUPPLIER("ListSupplier.fxml"),
    ADDUSER("AddUser.fxml"),
    UPDATEUSER("UpdateUser.fxml"),
    LISTUSER("ListUser.fxml"),
    ADDROLE("AddRole.fxml"),
    UPADTEWAREHOUSE("UpdateWarehouse.fxml"),
    LISTWAREHOUSE("ListWarehouse.fxml"),
    LISTCHANGES("ListChanges.fxml"),
    UPDATECATEGORY("UpdateCategory.fxml"),
    ADDCATEGORY("AddCategory.fxml"),
    LISTCATEGORY("ListCategory.fxml"),
    ADDWAREHOUSE("AddWarehouse.fxml"),
    LOGIN("Login.fxml"),
    WELCOME("Welcome.fxml"),
    REGISTER("Register.fxml"),
    MENU("Menu.fxml");
    private final String  filename;
    View(String filename){
        this.filename = filename;
    }
    public String getFilename(){
        return filename;
    }
}
