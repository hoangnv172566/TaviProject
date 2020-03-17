package View.Design.Client.Questions.CES;

import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionsCESController {
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionsCESController.class.getResource("C-Question-CES.fxml"));

        } catch (IOException e){
            System.out.println("can't load C-Question-CES");
            return null;
        }
    }
}
