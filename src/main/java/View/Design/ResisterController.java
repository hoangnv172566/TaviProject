package View.Design;

import View.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResisterController implements Initializable {
//    @FXML public static AnchorPane resisterPane;
    @FXML private Button backBtn;
    @FXML private Button resisterBtn;

    private static Stage windows = new Stage();
    public static void setup(){
        try{
            Parent root = FXMLLoader.load(ResisterController.class.getResource("Resister.fxml"));
            windows.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Khong tim thay trang chủ!");
        }
    }
    public static void display(){
        setup();
        windows.setTitle("Trang chủ");
        windows.show();
    }
    public static void close(){
        windows.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backBtn.setOnAction(e->backPreviousWindow(e));
        resisterBtn.setOnAction(e->commitResister(e));
    }

    private void commitResister(ActionEvent event) {
        Label label = new Label("Đăng ký thành công!");
        Button button = new Button("Trở về trang đăng ký");
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, button);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.show();
        backPreviousWindow(event);
    }

    private void backPreviousWindow(ActionEvent event) {

        try{
            Parent root = FXMLLoader.load(LoginController.class.getResource("Login.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
