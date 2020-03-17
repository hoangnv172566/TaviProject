package View.Design.DesignUntil.impl;

import View.Design.DesignUntil.IDisplay;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Display implements IDisplay {
    @Override
    public Parent display(String url) {
        return null;
    }

    @Override
    public void setScene(Parent root, ActionEvent event) {
        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();

    }
}
