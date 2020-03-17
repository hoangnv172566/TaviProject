package View.Design.Client.Questions.SingleChoice;

import View.Design.Client.Questions.CQuestionsController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;


public class CQuestionSingleChoiceController implements Initializable {

    @FXML private HBox listChoices;
    private int numberChoices;
    private ToggleGroup toggleGroup;
    public static Parent getParent(){
        try{
            return FXMLLoader.load(CQuestionSingleChoiceController.class.getResource("C-Question-SingleChoice.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getNumberChoice(){
        return 1;
    }

    private void genChoice() {
        listChoices.getChildren().clear();
        numberChoices = getNumberChoice();// se ghép cùng API
        ArrayList<RadioButton> listRadioButton = new ArrayList<>();
        toggleGroup = new ToggleGroup();

        // generate Radio Button
        for (int i = 0; i < numberChoices; i++) {
            RadioButton radioButton = new RadioButton("Lựa chọn " + (i + 1));
            listRadioButton.add(radioButton);
            listChoices.getChildren().add(radioButton);
        }
        for (RadioButton radioButton : listRadioButton) {
            radioButton.setToggleGroup(toggleGroup);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genChoice();
    }


}
