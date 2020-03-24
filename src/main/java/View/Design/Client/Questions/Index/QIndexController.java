package View.Design.Client.Questions.Index;

import Config.URLApi;
import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.Setting.WaitingScene;
import Models.Survey.Survey;
import Models.User.User;
import Service.impl.CollectionService;
import Service.impl.SurveyService;
import Service.impl.ThankService;
import Service.impl.UserService;
import Utils.FileMethod;
import Utils.StageConfigure;
import View.Design.Client.Questions.CQuestionsController;
import View.Design.Common.LoginV2Controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class QIndexController implements Initializable {
    @FXML private Button loadQuesBtn;
    @FXML private Button logoutBtn;
    @FXML private VBox indexFunctionPane;
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

    //set Effect Button
    private void setEffect(){
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

    //check Data
    private boolean isLink(String fileName){
        return !fileName.startsWith("File");
    }

    private boolean checkExistData(String fileDir){
        File file = new File(fileDir);
        if(file.exists()){
            return new File(fileDir).listFiles()!=null;
        }else{
            return false;
        }
    }

    //syncing
    private User getUserData() throws IOException {
        Path path = Paths.get("Data", "User");
        File userFile = new File(path.toAbsolutePath().toString() + "\\UserData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(userFile, User.class);

    }

    private void syncData(){
        loadQuesBtn.setDisable(true);
        try {
            User userInfo = getUserData();
            //mapping collection
            CollectionService collectionService = new CollectionService();
            int stt = collectionService.getCollectionInfo(userInfo.getCollectionID());

            if (stt == 200) {
                Path collectionDir = Paths.get("Data", "Collection\\Info");
                File collectionFile = new File(collectionDir.toAbsolutePath().toString() + "\\CollectionData.json");
                Collection collection = new ObjectMapper().readValue(collectionFile, Collection.class);
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
                                String pathImageFile = videoPath.toAbsolutePath().toString() +  fileName;
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
                                System.out.println(collectionItem.getUrl());
                                String fileName = "VideoNo" + collectionItem.getStt();
                                System.out.println(fileName);
                                String pathVideoFile = videoPath.toAbsolutePath().toString() + "\\" + fileName + ".mp4";
                                FileMethod.saveFileFromURL(pathVideoFile, collectionItem.getUrl());
                            } catch (IOException e) {
                                System.out.println("link not found");
                            }
                        }
                    }

                });


            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadQuesBtn.setDisable(false);

    }

    private void synQuestionData(){
        try{
            User userInfo = getUserData();
            SurveyService surveyService = new SurveyService();
            Survey survey = surveyService.getSurvey(userInfo.getSurveyID());

            Path path = Paths.get("Data", "Survey");
            Files.createDirectories(path);
            File surveyData = new File(path.toAbsolutePath().toString()+"\\SurveyData.txt");
            FileOutputStream fos = new FileOutputStream(surveyData);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(survey);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void syncThanksData(){
        new ThankService().gettingContentThank();
    }

    //set default waiting Scene

    private void setDefaultWaitingScene(){
        try{
            Path path = Paths.get("FixedSetting", "DefaultWaitingScene");
            Files.createDirectories(path);
            File defaultWaitingSceneSetting = new File(path.toAbsolutePath().toString() + "\\WaitingScene.json");
            if(!defaultWaitingSceneSetting.exists()){
                defaultWaitingSceneSetting.createNewFile();
            }
            WaitingScene waitingScene = new WaitingScene();
            ArrayList<String> listPath = new ArrayList<>();
            listPath.add(path.toAbsolutePath().toString() + "\\File_introIT.mp4");
            waitingScene.setPath(listPath);
            waitingScene.setType(false);
            waitingScene.setTime(300);

            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(defaultWaitingSceneSetting, waitingScene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //setEffect
        setEffect();

        //set default Waiting scene setting
        setDefaultWaitingScene();

        //set event for button
        loadQuesBtn.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
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


        //checking syncing if it's needed
        //checking thanks
        String thanksPath = Paths.get("Data", "Thanks").toAbsolutePath().toString();

        System.out.println("Đang đồng bộ Lời cảm ơn...");
        syncThanksData();
        System.out.println("Lời cảm ơn đã được đồng bộ!");


        //checking survey
        String surveyPath = Paths.get("Data", "Survey").toAbsolutePath().toString();

            System.out.println("Đang đồng bộ câu hỏi...");
            synQuestionData();
            System.out.println("Đồng bộ câu hỏi đã xong...");


        //checking collection
        String videoPath = Paths.get("Data", "Collection\\Video").toAbsolutePath().toString();
        String imagePath = Paths.get("Data", "Collection\\Image").toAbsolutePath().toString();
        if(!(checkExistData(videoPath)&&checkExistData(imagePath))){
            Runnable syncProcess = ()->{
                System.out.println("Đang đồng bộ Bộ sưu tập...");
                syncData();
                System.out.println("Hoàn tất quá trình đồng bộ!");
            };
            Thread newThread = new Thread(syncProcess);
            newThread.setDaemon(true);
            newThread.start();
        }

    }

}
