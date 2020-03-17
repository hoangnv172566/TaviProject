package View.Design.DesignUntil;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;

public interface IIndexFunction {
    boolean checkExistentUsername(String data);
    boolean checkBlank();
    boolean checkSignIn(String username, String password);
}
