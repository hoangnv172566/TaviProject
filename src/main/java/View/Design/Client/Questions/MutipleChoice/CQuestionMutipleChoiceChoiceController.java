package View.Design.Client.Questions.MutipleChoice;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionMutipleChoiceChoiceController {
    public static Parent getParent() {
        try{
            return FXMLLoader.load(CQuestionMutipleChoiceChoiceController.class.getResource("C-Question-MutipleChoice-Choice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }
}
