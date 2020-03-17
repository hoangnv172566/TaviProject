package View.Design.Client.Questions.Index;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

        setWaitingSceneButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp4", "*.mp4"));
            File initDirectory = new File("src/main/java/Data/Collection");
            Stage stage = new Stage();
            stage.centerOnScreen();
            fileChooser.setTitle("Chọn màn hình chờ");

            if(initDirectory.isDirectory()){
                fileChooser.setInitialDirectory(initDirectory);
            }

            File selectectedFile = fileChooser.showOpenDialog(stage);


            if(selectectedFile!=null){
                String path = selectectedFile.getPath();
                try {
                   BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("src/main/java/Data/WaitingScene/WaitingSceneData.txt")));
                   bufferedWriter.write(selectectedFile.getAbsolutePath());
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }





        });

    }
}
