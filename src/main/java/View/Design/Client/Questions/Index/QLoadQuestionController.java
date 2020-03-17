package View.Design.Client.Questions.Index;

import View.Design.Client.Questions.CQuestionsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class QLoadQuestionController implements Initializable {
    @FXML Button searchingBtn;
    @FXML Button displayAllBtn;
    @FXML Button uploadBtn;
    @FXML Button back;

    static void setScent(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(e->{
            AnchorPane parent = (AnchorPane) ((Node)e.getSource()).getParent().getParent().getParent().getParent();
            VBox indexFunctionPane = (VBox) parent.getChildren().get(0);
            AnchorPane parentLoadQuestPane = (AnchorPane) parent.getChildren().get(1);
            if((!indexFunctionPane.isVisible())&&(parentLoadQuestPane.isVisible())){
                indexFunctionPane.setVisible(true);
                parentLoadQuestPane.setVisible(false);
            }

        });
        uploadBtn.setOnAction(e->{
             setScent(CQuestionsController.getParent(), e);
        });
    }
}
