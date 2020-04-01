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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class QSyncdataController implements Initializable {
    @FXML private Button syncDataButton;
    @FXML private Button setWaitingSceneButton;
    @FXML private Button syncQuestion;

    private Desktop desktop = Desktop.getDesktop();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        syncDataButton.setOnAction(event -> {
            Path imageCollection = Paths.get("Data", "Collection\\Image");
            Path videoCollection = Paths.get("Data", "Collection\\Video");
            File[] listImageFile = imageCollection.toFile().listFiles();
            File[] listVideoFile = videoCollection.toFile().listFiles();
            assert  listImageFile !=null;
            for (File file : listImageFile) {
                file.delete();
            }
            for(File f :listVideoFile){
                f.delete();
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
