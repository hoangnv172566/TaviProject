package View.Design.Client.Questions.CSAT;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionCSATChoiceController {
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionCSATChoiceController.class.getResource("C-Question-CSAT-Choice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void addChoice(){}


}
