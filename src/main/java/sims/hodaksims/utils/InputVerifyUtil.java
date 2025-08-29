package sims.hodaksims.utils;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.update.UpdateContract;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

public class InputVerifyUtil {
    private InputVerifyUtil() {
    }
    final Logger log = LoggerFactory.getLogger(InputVerifyUtil.class);
    public static Boolean checkForNumber(Map<String, String> checkThese){

        List<String> result = checkThese.keySet().stream().filter(
                x->{ try{
                    if(checkThese.get(x).isEmpty()) {
                        return false;
                    }
                    Integer.parseInt(checkThese.get(x));
                    return false;
                    }catch(NumberFormatException _) {
                    return true;
                     }}
        ).toList();
        if(!result.isEmpty()){
            alertNumber(result);
        }
        return result.isEmpty();
    }
    public static Boolean checkForDecimal(Map<String, String> checkThese){

        List<String> result = checkThese.keySet().stream().filter(
                x->{ try{
                    if(checkThese.get(x).isEmpty()) {
                        return false;
                    }
                    BigDecimalParser.parse(checkThese.get(x));
                    return false;
                }catch(NumberFormatException _) {
                    return true;
                }}
        ).toList();
        if(!result.isEmpty()){
            alertNumber(result);
        }
        return result.isEmpty();
    }
    public static void alertNumber(List<String> polja){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogreška");
        alert.setHeaderText("Koristite molim vas numeričke vrijednosti za ova polja\n");
        alert.setContentText("Neispravni:");
        for (String p:polja){
            alert.setContentText(alert.getContentText() +"\n"+p );
        }
        alert.showAndWait();
    }
    public static Boolean checkForRequired(Map<String, String> checkThese){
        List<String> result = checkThese.keySet().stream().filter(
                x-> checkThese.get(x).isEmpty()
        ).toList();
        if(!result.isEmpty()){
            alertNull(result);
        }
        return result.isEmpty();
    }
    public static void alertNull(List<String> polja){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pogreška");
        alert.setHeaderText("Koristite molim vas ispunite ova polja");
        alert.setContentText("Neispunjeni:");
        for (String p:polja){
            alert.setContentText(alert.getContentText() +"\n"+p );
        }
        alert.showAndWait();
    }

}
