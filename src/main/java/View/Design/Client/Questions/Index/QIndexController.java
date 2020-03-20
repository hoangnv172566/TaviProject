package View.Design.Client.Questions.Index;

import Config.URLApi;
import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.User.User;
import Service.impl.CollectionService;
import Service.impl.UserService;
import Utils.FileMethod;
import Utils.StageConfigure;
import View.Design.Client.Questions.CQuestionsController;
import View.Design.Common.LoginV2Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class QIndexController implements Initializable {
    @FXML private Button loadQuesBtn;
    @FXML private Button historyButton;
    @FXML private Button logoutBtn;
    @FXML private VBox indexFunctionPane;
    @FXML private AnchorPane parentLoadQuestPane;
    @FXML private Button syncDataBtn;
    @FXML private Button surveyBtn;
    @FXML private AnchorPane syncDataParent;

    public static Parent getParent() {
        try{
            return FXMLLoader.load(QIndexController.class.getResource("Q-Index.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void setScene(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        StageConfigure.fullStage(stage);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private boolean isLink(String fileName){
        if(fileName.contains("File")){
            return false;
        }
        return true;
    }

    private void syncData(){

        try {
            User userInfo = new User();
            Path path = Paths.get("Data", "User");
            FileReader readUserInfo = new FileReader(new File(path.toAbsolutePath().toString() + "\\UserData.txt"));
            BufferedReader bufferedReader = new BufferedReader(readUserInfo);
            String data = bufferedReader.readLine();
            JSONObject userData = (JSONObject) new JSONParser().parse(data);
            userInfo.setUsername((String) userData.get("username"));
            userInfo.setPassword((String) userData.get("password"));
            bufferedReader.close();
            readUserInfo.close();

            //mapping collection
            CollectionService collectionService = new CollectionService();
            Collection collection = collectionService.getCollectionInfor(userInfo);
            ArrayList<CollectionItem> listCollectionItem = collection.getListCollection();

            //sync data
            String host = "http://103.9.86.61:8080/resources/upload/srs";
            listCollectionItem.forEach(collectionItem -> {
                if(!collectionItem.isVideo()) {
                    String fileName = "/" + collectionItem.getUrl();
                    String webUrl = host + fileName;
                    try {
                        Path imgPath = Paths.get("Data", "Collection\\Image");
                        Files.createDirectories(imgPath);
                        String pathImageFile = imgPath.toAbsolutePath().toString() + "\\" + fileName;
                        FileMethod.saveFileFromURL(pathImageFile, webUrl);

                    } catch (MalformedURLException e) {
                        System.out.println("Sai định dạng URL");
                    } catch (IOException e) {
                        System.out.println("Không định dạng được file!");
                    }
                }else{
                    if(!isLink(collectionItem.getUrl())){
                        String fileName = "/" + collectionItem.getUrl();
                        String webUrl = host + fileName;
                        try {
                            Path videoPath = Paths.get("Data", "Collection\\Video");
                            Files.createDirectories(videoPath);
                            String pathImageFile = videoPath.toAbsolutePath().toString() + fileName;
                            FileMethod.saveFileFromURL(pathImageFile, webUrl);
                        } catch (MalformedURLException e) {
                            System.out.println("Không đúng định dạng URL");
                        } catch (IOException e) {
                            System.out.println("Không ghi được file!");
                        }
                    }else{
                        try {
                            Path videoPath = Paths.get("Data", "Collection\\Video");
                            Files.createDirectories(videoPath);
                            String pathImageFile = videoPath.toAbsolutePath().toString() +"\\CreatedVideoAt"+LocalDateTime.now().getHour() + "h" +LocalDateTime.now().getMinute() + "m" +LocalDateTime.now().getSecond()+"s"+ ".mp4";
                            FileMethod.saveFileFromURL(pathImageFile, collectionItem.getUrl());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            });

        } catch (FileNotFoundException e) {
            System.out.println("Khong tim thay userData");
        } catch (IOException e) {
            System.out.println("khong doc dc file");
        } catch (ParseException e) {
            System.out.println("khong the convert");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        syncData();



        //set style
        String leftBtnStyle = "-fx-border-radius: 0;\n" +
                "    -fx-background-radius: 0;\n" +
                "    -fx-background-color: transparent;\n" +
                "    -fx-text-fill: white;";

        String hoverLeftBtnStyle = "-fx-background-color: white;\n" +
                "    -fx-border-radius: 0;\n" +
                "    -fx-background-radius: 25 0 0 25;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 14;";

        surveyBtn.setStyle(hoverLeftBtnStyle);
        syncDataBtn.setStyle(leftBtnStyle);
        logoutBtn.setStyle(leftBtnStyle);



        //set event for button
        loadQuesBtn.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
        });

        historyButton.setOnAction(event -> {
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.setContentText("Chức năng chưa phát triển");
             alert.showAndWait();
        });

        logoutBtn.setOnAction(e->{
            UserService userService = new UserService();
            try{
                Path userDataPath = Paths.get("Data", "User");
                File file = new File(userDataPath.toAbsolutePath().toString()+"\\UserData.txt");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                JSONParser jsonParser = new JSONParser();
                JSONObject userDataJson = (JSONObject) jsonParser.parse(bufferedReader.readLine());

                User user = new User();
                user.setPassword((String) userDataJson.get("password"));
                user.setUsername((String) userDataJson.get("username"));
                userService.logout(user, URLApi.LOGOUT);

                bufferedReader.close();
                fileReader.close();


                Path listPathFile = Paths.get("Data");
                File[] listFiles = new File(listPathFile.toAbsolutePath().toString()).listFiles();
                for (File fileE:listFiles){
                    if(fileE.isDirectory()){
                        File[] listFileData = fileE.listFiles();
                        if(listFileData!=null){
                            for(File f :listFileData){
                                f.delete();
                            }
                        }

                    }
                }
            } catch (FileNotFoundException ex) {

            } catch (IOException ex) {
                System.out.println("Cann't Open or directory doesn't exist");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            setScene(LoginV2Controller.getParent(), e);
        });

        surveyBtn.setOnAction(event -> {
            if(!indexFunctionPane.isVisible()&&syncDataParent.isVisible()){
                indexFunctionPane.setVisible(true);
                syncDataParent.setVisible(false);
            }
            surveyBtn.setStyle(hoverLeftBtnStyle);
            syncDataBtn.setStyle(leftBtnStyle);
            logoutBtn.setStyle(leftBtnStyle);

        });

        syncDataBtn.setOnAction(event -> {
            if(indexFunctionPane.isVisible()&&!syncDataParent.isVisible()){
                indexFunctionPane.setVisible(false);
                syncDataParent.setVisible(true);
            }
            surveyBtn.setStyle(leftBtnStyle);
            syncDataBtn.setStyle(hoverLeftBtnStyle);
            logoutBtn.setStyle(leftBtnStyle);


        });

        surveyBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                surveyBtn.setStyle(hoverLeftBtnStyle);
            }else if(indexFunctionPane.isVisible()){
                surveyBtn.setStyle(hoverLeftBtnStyle);
            }else{
                surveyBtn.setStyle(leftBtnStyle);
            }


        });

        syncDataBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                syncDataBtn.setStyle(hoverLeftBtnStyle);
            }else if(syncDataParent.isVisible()){
                syncDataBtn.setStyle(hoverLeftBtnStyle);
            }else{
                syncDataBtn.setStyle(leftBtnStyle);
            }


        });

        logoutBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                logoutBtn.setStyle(hoverLeftBtnStyle);
            }else{
                logoutBtn.setStyle(leftBtnStyle);
            }
        });

    }

}
