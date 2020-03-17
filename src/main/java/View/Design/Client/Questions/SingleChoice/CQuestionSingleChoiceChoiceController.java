package View.Design.Client.Questions.SingleChoice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionSingleChoiceChoiceController {
    public static Parent getParent(){
        try{
            return (Parent) FXMLLoader.load(CQuestionSingleChoiceChoiceController.class.getResource("C-Question-SingleChoice-Choice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
