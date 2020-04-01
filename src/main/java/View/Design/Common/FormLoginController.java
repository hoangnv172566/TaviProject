package View.Design.Common;


import Config.URLApi;
import Models.Company.Company;
import Models.User.User;
import Service.impl.CompanyService;
import Service.impl.UserService;
import Utils.FileMethod;
import Utils.StageConfigure;
import View.Design.Client.Questions.Index.QIndexController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class FormLoginController implements Initializable{
    @FXML private AnchorPane loginPane;
    @FXML private Button loginBtn;
    @FXML private Button resisterBtn;
    @FXML private Label forgettingPassBtn;
    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordTF;
    @FXML private Label announcement;
    @FXML private HBox loginHBox;

    public Parent getParent(){
        try{
            return FXMLLoader.load(FormLoginController.class.getResource("Form-Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private User getUserData(){
        User user = new User();
        if(usernameTF.getText() !=null && passwordTF.getText() != null){
            user.setUsername(usernameTF.getText());
            user.setPassword(passwordTF.getText());
        }
        return user;
    }
    private void setScene(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        StageConfigure.fullStage(stage);
        stage.setScene(new Scene(parent));
        stage.show();

    }

    private boolean checkLocalInfo(String pathFile){
        return new File(pathFile).listFiles()!=null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        autoLoginNextTime();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefHeight(25.0);
        progressIndicator.setPrefWidth(25.0);
        progressIndicator.setProgress(-1);
        progressIndicator.setVisible(false);
        loginHBox.getChildren().add(progressIndicator);

        UserService userService = new UserService();
        resisterBtn.setDisable(true);
        forgettingPassBtn.setDisable(true);


        //login function



        loginBtn.setOnAction(event ->{
            progressIndicator.setVisible(true);
            Path path = Paths.get("Data", "User");
            if(!checkLocalInfo(path.toAbsolutePath().toString())){
                User user = getUserData();
                //announce login status in case login fails
                try {
                    // if login success, load question index
                    if(userService.login(user, URLApi.LOGIN) == 200){
                        //open Stage, full scene
                        progressIndicator.setVisible(true);
                        setScene(QIndexController.getParent(), event);
                    }else{
                        progressIndicator.setVisible(false);
                        announcement.setVisible(true);
                        announcement.setText("Tài khoản hoặc mật khẩu không chính xác");
                    }
                } catch (IOException er) {
                    progressIndicator.setVisible(false);
                    announcement.setVisible(true);
                    announcement.setText("Mất kết nối! Vui lòng thử lại");
                }
            }else{
                File userFileData = new File(path.toAbsolutePath().toString() + "\\UserData.json");
                try{
                    User userInfo = new ObjectMapper().readValue(userFileData, User.class);
                    User userFromKeyboard = getUserData();
                    if(userInfo.getUsername().equals(userFromKeyboard.getUsername())&&userInfo.getPassword().equals(userFromKeyboard.getPassword())){
                        setScene(QIndexController.getParent(), event);
                    }

                } catch (JsonParseException | JsonMappingException e) {
                    System.out.println("Lỗi mapping username Data!");
                } catch (IOException e) {
                    User user = getUserData();
                    //announce login status in case login fails
                    try {
                        // if login success, load question index
                        if(userService.login(user, URLApi.LOGIN) == 200){
                            //open Stage, full scene
                            progressIndicator.setVisible(true);
                            setScene(QIndexController.getParent(), event);
                        }else{
                            progressIndicator.setVisible(false);
                            announcement.setVisible(true);
                            announcement.setText("Tài khoản hoặc mật khẩu không chính xác");
                        }
                    } catch (IOException er) {
                        progressIndicator.setVisible(false);
                        announcement.setVisible(true);
                        announcement.setText("Mất kết nối! Vui lòng thử lại");
                    }
                }
            }


        });

        usernameTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            announcement.setVisible(false);
        });
        passwordTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            announcement.setVisible(false);
        });

        resisterBtn.setOnAction(event -> {
            StackPane parentLoginAndResisterPane = (StackPane) resisterBtn.getParent().getParent().getParent().getParent();
            Pane resisterPane = (Pane) parentLoginAndResisterPane.getChildren().get(1);
            Pane loginPane = (Pane) parentLoginAndResisterPane.getChildren().get(0);
            //show resister tab
            if((!resisterPane.isVisible())&(loginPane.isVisible())){
                resisterPane.setVisible(true);
                loginPane.setVisible(false);
            }
//            loginPane.setOnKeyPressed();
        });

        forgettingPassBtn.setOnMouseClicked(event -> {
            StackPane parentLoginAndResisterPane = (StackPane) resisterBtn.getParent().getParent().getParent().getParent();
            Pane loginPane = (Pane) parentLoginAndResisterPane.getChildren().get(0);
            Pane forgettingPass = (Pane) parentLoginAndResisterPane.getChildren().get(2);
            if((!forgettingPass.isVisible())&(loginPane.isVisible())){
                forgettingPass.setVisible(true);
                loginPane.setVisible(false);
            }

        });

    }

    private void autoLoginNextTime() {
        Path path  = Paths.get("Data", "User");
        if(checkLocalInfo(path.toAbsolutePath().toString())){
            File file = new File(path.toAbsolutePath().toString() + "\\UserData.json");
            try{
                User user = new ObjectMapper().readValue(file, User.class);
                usernameTF.setText(user.getUsername());
                passwordTF.setText(user.getPassword());
            } catch (JsonParseException | JsonMappingException e) {
                System.out.println("auto login failed");
            } catch (IOException e){
                System.out.println("File user's not found");
            }

        }

    }


}
