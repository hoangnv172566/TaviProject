package View.Design.Client.Thanks;

import Models.Thanks.Thanks;
import Service.impl.ThankService;
import View.Design.Client.Questions.CQuestionsController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CThanksController extends Application implements Initializable {
    @FXML private Label thankContent;
    @FXML private Button back;

    public static Parent getParent(){
        try{
            return FXMLLoader.load(CThanksController.class.getResource("C-Thanks.fxml"));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void setScene(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void setThank(Thanks thank){
        thankContent.setText(thank.getContent());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ThankService thankService = new ThankService();
        thankContent.setText(thankService.gettingContentThank());

        back.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
