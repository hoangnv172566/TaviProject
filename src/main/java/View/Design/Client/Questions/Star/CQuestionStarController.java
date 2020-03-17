package View.Design.Client.Questions.Star;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionStarController {
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionStarController.class.getResource("C-Question-Star.fxml"));
        } catch (IOException e) {
            System.out.println("can't load C-question-Star");
            return null;
        }
    }

}
