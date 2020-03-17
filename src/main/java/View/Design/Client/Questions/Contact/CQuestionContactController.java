package View.Design.Client.Questions.Contact;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionContactController {
    public static Parent getParent() {
        try{
            return FXMLLoader.load(CQuestionContactController.class.getResource("C-Question-Contact.fxml"));
        }catch (IOException e){
            System.out.println("file contact k thay");
            return null;
        }
    }
}
