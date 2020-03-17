package View.Design.Common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormResisterController implements Initializable {
    @FXML private Button back;
    @FXML private Button confirmBtn;
    @FXML TextField email;
    @FXML TextField pass;
    @FXML TextField passCf;
    @FXML TextField phoneNumber;

    public static Parent getParrent(){
        try{
            return FXMLLoader.load(FormResisterController.class.getResource("Form-Resister.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(e->backToLogin());
        confirmBtn.setOnAction(e->signUpSuccess());

    }
    private void backToLogin() {
        StackPane parentLoginAndResisterPane = (StackPane) back.getParent().getParent().getParent();
        Pane loginPane = (Pane) parentLoginAndResisterPane.getChildren().get(0);
        Pane resisterPane = (Pane)  parentLoginAndResisterPane.getChildren().get(1);
        //show Login tab
        if((resisterPane.isVisible())&(!loginPane.isVisible())){
            resisterPane.setVisible(false);
            loginPane.setVisible(true);

        }
    }

    private void signUpSuccess() {
        //after check value, announce success and back to Login for user to login
        // back to login
        StackPane parentLoginAndResisterPane = (StackPane) back.getParent().getParent().getParent();
        Pane loginPane = (Pane) parentLoginAndResisterPane.getChildren().get(0);
        Pane resisterPane = (Pane)  parentLoginAndResisterPane.getChildren().get(1);
        //show Login tab
        if((resisterPane.isVisible())&(!loginPane.isVisible())){
            resisterPane.setVisible(false);
            loginPane.setVisible(true);
        }
    }


}
