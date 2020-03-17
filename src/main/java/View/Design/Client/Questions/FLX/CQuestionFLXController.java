package View.Design.Client.Questions.FLX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CQuestionFLXController {
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionFLXController.class.getResource("C-Question-FLX.fxml"));
        }catch(IOException e){
            System.out.println("cannot find the FLX file");
            return null;
        }
    }
}
