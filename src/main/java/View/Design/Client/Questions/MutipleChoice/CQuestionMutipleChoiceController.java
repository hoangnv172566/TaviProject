package View.Design.Client.Questions.MutipleChoice;

import View.Design.Client.Questions.SingleChoice.CQuestionSingleChoiceController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CQuestionMutipleChoiceController extends Application implements Initializable {
    @FXML private HBox listChoices;
    private CheckBox choice;
    private int numberChoices;
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionMutipleChoiceController.class.getResource("C-Question-MutipleChoice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int getNumberChoice(){
        return 5;
    }
    private String getNameOfChoice(){return "Lựa chọn thứ ";}
    private void genChoice() {
        listChoices.getChildren().clear();
        numberChoices = getNumberChoice();// se ghép cùng API

        // generate Radio Button
        for (int i = 0; i < numberChoices; i++) {
            CheckBox checkBtn = new CheckBox(getNameOfChoice() + (i+1));
            listChoices.getChildren().add(checkBtn);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genChoice();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
