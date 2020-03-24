package View.Design.Client.Thanks;

import Models.Thanks.Thanks;
import Service.impl.ThankService;
import View.Design.Client.Questions.CQuestionsController;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CThanksController implements Initializable {
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

    private Thanks getThankData() throws IOException {
        Path path = Paths.get("Data","Thanks");
        File file = new File(path.toAbsolutePath().toString() + "\\ThankData.json");
        return new ObjectMapper().readValue(file, Thanks.class);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Thanks thanks = getThankData();
            thankContent.setText(thanks.getContent());
        } catch (IOException e) {
            thankContent.setText("Thank You");
        }


        back.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
        });
    }


}
