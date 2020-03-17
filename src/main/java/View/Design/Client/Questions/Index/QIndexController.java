package View.Design.Client.Questions.Index;

import Config.URLApi;
import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.User.User;
import Service.impl.CollectionService;
import Service.impl.UserService;
import Utils.FileMethod;
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
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QIndexController extends Application implements Initializable {
    @FXML private Button loadQuesBtn;
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
        stage.setScene(new Scene(parent));
        stage.setFullScreen(true);
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
            FileReader readUserInfo = new FileReader(new File("src/main/java/Data/Answer/UserData.txt"));
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
                        FileMethod.createDirectory("src/main/java/Data/Collection/Image");
                        String pathImageFile = "src/main/java/Data/Collection/Image" + fileName;
                        FileMethod.saveFileFromURL(pathImageFile, webUrl);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(!isLink(collectionItem.getUrl())){
                        String fileName = "/" + collectionItem.getUrl();
                        String webUrl = host + fileName;
                        try {
                            FileMethod.createDirectory("src/main/java/Data/Collection/Video");
                            String pathImageFile = "src/main/java/Data/Collection/Video" + fileName;
                            FileMethod.saveFileFromURL(pathImageFile, webUrl);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            FileMethod.createDirectory("src/main/java/Data/Collection/Video");
                            String pathImageFile = "src/main/java/Data/Collection/Video" +"/CreatedVideoAt"+LocalDateTime.now().getHour() + "h" +LocalDateTime.now().getMinute() + "m" +LocalDateTime.now().getSecond()+"s"+ ".mp4";
                            System.out.println(pathImageFile);
                            FileMethod.saveFileFromURL(pathImageFile, collectionItem.getUrl());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }





            });





        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        syncData();

        //set event for button
        loadQuesBtn.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
        });

        logoutBtn.setOnAction(e->{
            UserService userService = new UserService();
            try{
                File file = new File("src/main/java/Data/UserData.txt");
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
                file.delete();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
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
        });

        syncDataBtn.setOnAction(event -> {
            if(indexFunctionPane.isVisible()&&!syncDataParent.isVisible()){
                indexFunctionPane.setVisible(false);
                syncDataParent.setVisible(true);
            }
        });


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
