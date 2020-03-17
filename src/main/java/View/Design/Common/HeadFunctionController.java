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
import java.util.ResourceBundle;

public class HeadFunctionController extends Application implements Initializable {
    @FXML private Button exit;
    @FXML private Button minimize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserService userService = new UserService();
        exit.setOnAction(e-> {
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            try{
                File file = new File("src/main/java/Data/UserData.txt");
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
                file.delete();
            } catch (FileNotFoundException ex) {
                stage.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }


            File companyData = new File("src/main/java/Data/Company/Company.json");
            companyData.delete();

            stage.close();
        });
        minimize.setOnAction(event -> {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HeadFunction.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
