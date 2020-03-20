package View.Design.Common;


import Config.URLApi;
import Models.Company.Company;
import Models.User.User;
import Service.impl.CompanyService;
import Service.impl.UserService;
import Utils.FileMethod;
import Utils.StageConfigure;
import View.Design.Client.Questions.Index.QIndexController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class FormLoginController implements Initializable{
    @FXML private AnchorPane loginPane;
    @FXML private Button loginBtn;
    @FXML private Button resisterBtn;
    @FXML private Label forgettingPassBtn;
    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordTF;
    @FXML private Label announcement;

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

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        Desktop desktop = Desktop.getDesktop();

        UserService userService = new UserService();
        resisterBtn.setDisable(true);
        forgettingPassBtn.setDisable(true);
        loginBtn.setOnAction(event ->{
            User user = getUserData();

            //announce login status in case login fails
            try {
                // if login success, load question index
                if(userService.login(user, URLApi.LOGIN) == 200){

                //create a temporary file to store user Information to logout
                    Path path = Paths.get("Data", "User");
                    Files.createDirectories(path);
                    File file = new File(path.toAbsolutePath().toString()+"\\UserData.txt");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    JSONObject userDataJS = new JSONObject();
                    userDataJS.put("password", user.getPassword());
                    userDataJS.put("username", user.getUsername());

                    bufferedWriter.write(userDataJS.toString());

                    bufferedWriter.close();
                    fileWriter.close();

                    //open Stage, full scene
                    setScene(QIndexController.getParent(), event);

                }else{
                    announcement.setVisible(true);
                    announcement.setText("Tài khoản hoặc mật khẩu không chính xác");
                }

            } catch (IOException ignored) {

            }

            //getting company data and save to file
            try{
                Path companyPath = Paths.get("Data", "Company");
                Files.createDirectories(companyPath);
                CompanyService companyService = new CompanyService();
                Company company = companyService.getCompanyInfor(user);
                FileMethod.saveFile(companyPath.toAbsolutePath().toString(), "\\Company.json", company);
            } catch (IOException e) {
                e.printStackTrace();
            }


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


}
