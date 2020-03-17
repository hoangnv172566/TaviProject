package View.Design.Common;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgettingPassController implements Initializable {
    @FXML private Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(e->{
            StackPane parentLoginAndResisterPane = (StackPane) back.getParent().getParent().getParent();
            Pane loginPane = (Pane) parentLoginAndResisterPane.getChildren().get(0);
            Pane forgettingPassPane = (Pane)  parentLoginAndResisterPane.getChildren().get(2);
            //show Login tab
            if((forgettingPassPane.isVisible())&(!loginPane.isVisible())) {
                forgettingPassPane.setVisible(false);
                loginPane.setVisible(true);
            }
        });
    }
}
