package View.Design.Client.Questions.CSAT;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CQuestionsCSATController implements Initializable {
    @FXML private HBox listChoices;

    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionsCSATController.class.getResource("C-Questions-CSAT.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
