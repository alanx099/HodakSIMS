package sims.hodaksims.controller.add;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.Category;
import sims.hodaksims.model.View;
import sims.hodaksims.repository.CategoryRepository;
import sims.hodaksims.utils.InputVerifyUtil;

import java.util.Map;
import java.util.Objects;

public class AddCategory {
    static final Logger log = LoggerFactory.getLogger(AddCategory.class);

    @FXML
    Label title;
    @FXML
    TextField name;
    @FXML
    TextField description;
    private final CategoryRepository<Category> cRep = new CategoryRepository<>();

    @FXML
    protected void switchToSceneListCategory() {
        ScreenManagerController.switchTo(View.LISTCATEGORY);
    }
    @FXML
    public void insertToDb(){
        String cName = name.getText();
        String cDesc = description.getText();
        Category cat = new Category(cName,cDesc);
        cRep.save(cat);
        this.switchToSceneListCategory();
    }
    public void beforeInsert(){
        Map<String, String> required = Map.of("Ime",  name.getText(), "Opis", description.getText());
        Boolean requiredCheck = InputVerifyUtil.checkForRequired(required);
        if (Boolean.TRUE.equals(requiredCheck)) {
            insertToDb();
        }
    }
}

