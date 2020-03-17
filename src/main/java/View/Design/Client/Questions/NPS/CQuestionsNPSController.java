package View.Design.Client.Questions.NPS;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CQuestionsNPSController extends Application implements Initializable  {
    @FXML
    Button test;
    @FXML
    HBox listQuestions;


    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionsNPSController.class.getResource("C-Question-NPS.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test.setOnAction(e->{
            listQuestions.getChildren().add( CQuestionsNPSChoiceController.getParent());
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
