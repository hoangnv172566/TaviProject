package View.Design.Common;

import Models.Collection.Collection;
import Models.Collection.CollectionItem;
import Models.User.User;
import Service.impl.CollectionService;
import Utils.FileMethod;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SplashScreenController extends Application implements Initializable {
    @FXML private VBox parentVBox;

    public static Parent getParent(){
        try{
            return FXMLLoader.load(SplashScreenController.class.getResource("SplashScreen.fxml"));
        } catch (IOException e) {
            return null;
        }
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
            File userFile = new File(path.toAbsolutePath().toString() + "\\UserData.json");
            ObjectMapper objectMapper = new ObjectMapper();
            userInfo = objectMapper.readValue(userFile, User.class);

            //mapping collection
            CollectionService collectionService = new CollectionService();
            int stt = collectionService.getCollectionInfo(userInfo.getCollectionID());

            if (stt == 200) {
                Path collectionDir = Paths.get("Data", "Collection\\Info");
                File collectionFile = new File(collectionDir.toAbsolutePath().toString() + "\\CollectionData.json");
                Collection collection = objectMapper.readValue(collectionFile, Collection.class);

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

                            } catch (MalformedURLException e) {
                                System.out.println("Không đúng định dạng URL");
                            } catch (IOException e) {
                                System.out.println("Không ghi được file!");
                            }
                        }else{
                            try {
                                Path videoPath = Paths.get("Data", "Collection\\Video");
                                Files.createDirectories(videoPath);
                                String fileName = "hihi" + LocalDateTime.now().getSecond();
                                System.out.println(fileName);
                                String pathVideoFile = videoPath.toAbsolutePath().toString()+"\\" + fileName + ".mp4";
                            } catch (IOException e) {
                                e.printStackTrace();
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label label = new Label();
        label.setText("Đang đồng bộ.....");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(-1.0);
        parentVBox.getChildren().add(progressIndicator);



    }

    @Override
    public void start(Stage stage) throws Exception {
        Runnable syncProcess = ()->{
            syncData();
        };
        Thread newThread = new Thread(syncProcess);
        newThread.setDaemon(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newThread.start();

        stage.setScene(new Scene(getParent()));
        stage.show();

    }
}
