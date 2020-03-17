package View.Design.DesignUntil;

import javafx.event.ActionEvent;
import javafx.scene.Parent;

public interface IDisplay {
    Parent display(String url);
    void setScene(Parent root, ActionEvent event);
}
