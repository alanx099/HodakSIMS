package sims.hodaksims.model;

public enum View {
    LOGIN("Login.fxml"),
    WELCOME("Welcome.fxml"),
    REGISTER("Register.fxml");
    private final String  filename;
    View(String filename){
        this.filename = filename;
    }
    public String getFilename(){
        return filename;
    }
}
