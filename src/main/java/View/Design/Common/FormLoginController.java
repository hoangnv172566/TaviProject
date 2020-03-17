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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FormLoginController implements Initializable{
    @FXML private AnchorPane loginPane;
    @FXML private Button loginBtn;
    @FXML private Button resisterBtn;
    @FXML private Label forgettingPassBtn;
    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordTF;


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
        stage.setScene(new Scene(parent));
        stage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        UserService userService = new UserService();
        resisterBtn.setDisable(true);
        forgettingPassBtn.setDisable(true);
        loginBtn.setOnAction(event ->{
            User user = getUserData();
            //create a temporary file to store user Information to logout
            try{
                File file = new File("src/main/java/Data/UserData.txt");
                if(file.exists()){

                }else{
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

            } catch (IOException e) {
                e.printStackTrace();
            }

            //announce login status in case login fails
            try {
                // if login success, load question index
                if(userService.login(user, URLApi.LOGIN) == 200){
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

                    StageConfigure.fullStage(stage);

                    Scene scene = new Scene(QIndexController.getParent());
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.show();
                }else{
                    System.out.println("Tài khoản hoặc mật khẩu không chính xác!...");
                }

            } catch (IOException e) {
                System.out.println("Tài khoản hoặc mật khẩu không chính xác");
            }


            //getting company data and save to file


            CompanyService companyService = new CompanyService();
            Company company = companyService.getCompanyInfor(user);
            FileMethod.saveFile("src/main/java/Data/Company/", "Company.json", company);

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
