package View.Design.Client.Questions.NPS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CQuestionsNPSChoiceController implements Initializable {

    public static  Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionsNPSChoiceController.class.getResource("C-Question-NPS-Choice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
