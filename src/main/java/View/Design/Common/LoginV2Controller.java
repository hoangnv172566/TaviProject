package View.Design.Common;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginV2Controller {

    public static Parent getParent() {
        try {
            return FXMLLoader.load(LoginV2Controller.class.getResource("Login-v2.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
