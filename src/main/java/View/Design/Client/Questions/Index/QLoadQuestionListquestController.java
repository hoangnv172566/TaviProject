package View.Design.Client.Questions.Index;

import Models.Survey.Survey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class QLoadQuestionListquestController implements Initializable {

    @FXML private TableView<Survey> listSurvey;
//    @FXML private TableColumn<Survey, String> ID;
    @FXML private TableColumn<Survey, String> nameSurveyCol;
    @FXML private TableColumn<Survey, String> kindOfSurveyCol;
//    @FXML private TableColumn<Survey, String> createdDateOfSurveyCol;
    @FXML private TableColumn<Survey, String> statusCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listSurvey.setEditable(false);
        Survey survey = new Survey();
        survey.setContentSurvey("today is thursday");

        ObservableList<Survey> listSurveyInTable = FXCollections.observableArrayList();
        listSurvey.setItems(listSurveyInTable);
        listSurveyInTable.add(survey);
        listSurveyInTable.get(0);
        //set Value for column
        nameSurveyCol.setCellValueFactory(new PropertyValueFactory<>("contentSurvey"));
        nameSurveyCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }






}
