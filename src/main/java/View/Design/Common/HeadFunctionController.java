package View.Design.Common;

import Config.URLApi;
import Models.User.User;
import Service.impl.UserService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class HeadFunctionController implements Initializable {
    @FXML private Button exit;
    @FXML private Button minimize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserService userService = new UserService();

        exit.setOnAction(e-> {
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            try{
                Path path = Paths.get("Data", "User");
                File file = new File(path.toAbsolutePath().toString(), "\\UserData.txt");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JSONParser jsonParser = new JSONParser();
                JSONObject userDataJson = (JSONObject) jsonParser.parse(bufferedReader.readLine());

                User user = new User();
                user.setPassword((String) userDataJson.get("password"));
                user.setUsername((String) userDataJson.get("username"));
                userService.logout(user, URLApi.LOGOUT);

                bufferedReader.close();
                fileReader.close();

                Path listPathFile = Paths.get("Data");
                File[] listFiles = new File(listPathFile.toAbsolutePath().toString()).listFiles();

                for (File fileE:listFiles){
                    if(fileE.isDirectory()){
                        File[] listFileData = fileE.listFiles();
                        for(File f :listFileData){
                            f.delete();
                        }
                    }
                }

            } catch (IOException ex) {
                stage.close();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            stage.close();
        });

        minimize.setOnAction(event -> {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });
    }

}
