package View.Design.Client.Questions.Open;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionOpenController {
    public static Parent getParent(){
        try {
            return FXMLLoader.load(CQuestionOpenController.class.getResource("C-Question-Open.fxml"));
        } catch (IOException e) {
            System.out.println("file open khoong thay");
            return null;
        }
    }

}
