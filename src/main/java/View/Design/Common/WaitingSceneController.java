package View.Design.Common;

import Models.Setting.WaitingScene;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingSceneController extends Application implements Initializable {
    @FXML private TextField timeTf;
    @FXML private TextField pathTf;
    @FXML private Button pathBtn;
    @FXML private Label timeAnnouncement;
    @FXML private Label pathAnnouncement;
    @FXML private Button confirmBtn;
    @FXML private JFXToggleButton switchConfig;
    @FXML private Label pathLb;

    public static Parent getParent(){
        try{
            return FXMLLoader.load(WaitingSceneController.class.getResource("WaitingScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //check Value
    private boolean checkTime(String data){
        if(data!=null){
            try{
                long time = Long.parseLong(data);
                return time > 0 && time <= 18000;
            }catch (Exception er){
                return false;
            }
        }else{
            return false;
        }


    }
    private boolean checkPath(String path){
        if(path!=null){
            try{
                File file = new File(path);
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read();
                fileInputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }else{
            return false;
        }

    }
    private boolean checkData(WaitingScene waitingScene){
        String timeStr = String.valueOf(waitingScene.getTime());
        ArrayList<String> listPath = waitingScene.getPath();
        if(checkTime(timeStr)){
            for (String s : listPath) {
                if (!checkPath(s)) {
                    return false;
                }
            }
        }else{
            return false;
        }

        return true;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WaitingScene waitingScene = new WaitingScene();
        ArrayList<String> listPath = new ArrayList<>();
        timeTf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(timeTf.getText().equals("")){
                    timeAnnouncement.setText("(*) Không được để trống!");
                    timeAnnouncement.setVisible(true);
                }else{
                    if(checkTime(timeTf.getText())){
                        waitingScene.setTime(Long.parseLong(timeTf.getText()));
                    }else{
                        timeAnnouncement.setText("(*) Chỉ được nhập số, giới hạn là 30 phút (1800 giây)");
                        timeAnnouncement.setVisible(true);
                    }
                }
            }else{
                timeAnnouncement.setVisible(false);
            }

        });

        pathBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp4", "*.mp4"));
            Path videoPathInit = Paths.get("Data", "Collection\\Video");
            File initDirectory = new File(videoPathInit.toAbsolutePath().toString());

            Stage stage = new Stage();
            stage.centerOnScreen();

            fileChooser.setTitle("Chọn màn hình chờ");
            if(initDirectory.isDirectory()){
                fileChooser.setInitialDirectory(initDirectory);
            }

            File selectedFile = fileChooser.showOpenDialog(stage);

            if(selectedFile!=null){
                listPath.clear();
                listPath.add(selectedFile.getAbsolutePath());
                waitingScene.setPath(listPath);
                pathTf.setText(waitingScene.getPath().get(0));
            }
        });

        waitingScene.setType(false);

        switchConfig.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                switchConfig.setText("Hình ảnh");
                pathLb.setText("Đường dẫn tới Hình ảnh:");
                waitingScene.setType(true);
                pathBtn.setOnAction(event -> {
                    pathAnnouncement.setVisible(false);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Tất cả hình ảnh", "*.*"));
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpeg", "*.jpeg"));
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png", "*.png"));
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg", "*.jpg"));
                    Path videoPathInit = Paths.get("Data", "Collection\\Image");

                    File initDirectory = new File(videoPathInit.toAbsolutePath().toString());

                    Stage stage = new Stage();
                    stage.centerOnScreen();

                    fileChooser.setTitle("Chọn màn hình chờ");

                    if(initDirectory.isDirectory()){
                        fileChooser.setInitialDirectory(initDirectory);
                    }

                    List<File> selectedFile = fileChooser.showOpenMultipleDialog(stage);
                    StringBuilder sb = new StringBuilder("File: ");
                    if(selectedFile!=null){
                        listPath.clear();
                        selectedFile.forEach(file -> {
                            try{
                                String fileType = Files.probeContentType(file.toPath());
                                if(fileType.contains("image")){
                                    listPath.add(file.getAbsolutePath());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sb.append(file.getName() + " ");
                            pathTf.setText(String.valueOf(sb));
                        });
                        waitingScene.setPath(listPath);
                    }else{
                        pathAnnouncement.setText("(*) Hãy chọn hình ảnh!");
                        pathAnnouncement.setVisible(true);
                    }
                });

            }else{
                switchConfig.setText("Video");
                pathLb.setText("Đường dẫn tới Video:");
                waitingScene.setType(false);
                pathBtn.setOnAction(event -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp4", "*.mp4"));
                    Path videoPathInit = Paths.get("Data", "Collection\\Video");
                    File initDirectory = new File(videoPathInit.toAbsolutePath().toString());

                    Stage stage = new Stage();
                    stage.centerOnScreen();

                    fileChooser.setTitle("Chọn màn hình chờ");
                    if(initDirectory.isDirectory()){
                        fileChooser.setInitialDirectory(initDirectory);
                    }

                    File selectedFile = fileChooser.showOpenDialog(stage);

                    if(selectedFile!=null){
                        listPath.clear();
                        listPath.add(selectedFile.getAbsolutePath());
                        waitingScene.setPath(listPath);
                        pathTf.setText(waitingScene.getPath().get(0));
                    }
                });

            }
        });

        confirmBtn.setOnAction(event -> {
            if(checkData(waitingScene)){
                Path videoPath = Paths.get("Setting", "WaitingScene");
                try{
                    Files.createDirectories(videoPath);
                    File videoFile = new File(videoPath.toAbsolutePath().toString() + "\\WaitingScene.json");
                    if(videoFile.exists()){
                        videoFile.createNewFile();
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(videoFile, waitingScene);
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.close();
                }catch (IOException er){
                    System.out.println("Không thể ghi dữ liệu waiting scene");
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Thời gian hoặc đường dẫn không chính xác!");
                alert.showAndWait();
            }
        });

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
