package View.Design.Client.Questions.Index;

import Models.Setting.WaitingScene;
import View.Design.Common.WaitingSceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class QSyncdataController implements Initializable {
    @FXML private Button syncDataButton;
    @FXML private Button setWaitingSceneButton;
    @FXML private Button syncQuestion;
    private Desktop desktop = Desktop.getDesktop();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        syncDataButton.setOnAction(event -> {
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/main/java/Data/WaitingScene/WaitingSceneData.txt")));
                File file = new File(bufferedReader.readLine());
                desktop.open(file);
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        syncDataButton.setDisable(true);
        syncQuestion.setDisable(true);
        setWaitingSceneButton.setOnAction(event -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(WaitingSceneController.getParent()));
            stage.show();
        });

    }
}
