package sims.hodaksims.model;

public enum View {
    /**
     * View enumeracija za laku izmjenu ekrana
     * Ova enumeracija sadrži putanje prema ekranima koji su nam dostupni,
     * također se služimo konstrktorom i getFileName metodi kako bi nam pozivanje
     * unutar koda izgledalo čisto i pregledno.
     */
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
