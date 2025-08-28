package sims.hodaksims.controller.update;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
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

public class UpdateCategory<T extends Category> extends AbstractUpdateController<T> {
    static final Logger log = LoggerFactory.getLogger(UpdateCategory.class);
    private final CategoryRepository<Category> cRep = new CategoryRepository<>();
    @FXML
    DatePicker contractStart;
    @FXML
    DatePicker contractEnd;
    @FXML
    Label title;
    @FXML
    TextField name;
    @FXML
    TextField description;
    public void setFields(T edit){
        title.setText(edit.getId().toString());
        name.setText(edit.getName());
        description.setText(edit.getDescription());
    }

    @FXML
    protected void switchToSceneListCategory() {
        ScreenManagerController.switchTo(View.LISTCATEGORY);
    }

    @FXML
    public void insertToDb(){
        Category nCat = new Category(name.getText(), description.getText());
        nCat.setId(Long.parseLong(title.getText()));
        cRep.update(nCat);
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
