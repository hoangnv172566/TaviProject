package View.Design.Common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

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
