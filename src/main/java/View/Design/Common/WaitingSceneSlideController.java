package View.Design.Common;

import Models.Setting.WaitingScene;
import View.Design.Client.Questions.CQuestionsController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class WaitingSceneSlideController extends Application implements Initializable {
    @FXML private AnchorPane backgroundAnchorPane;


    static class ImageAnchorPane{
        private final ReentrantLock lock = new ReentrantLock();
        private AnchorPane anchorPane;
        public void setAnchorPane(AnchorPane anchorPane){
            this.anchorPane = anchorPane;
        }
        public synchronized void setImage(String filePath) throws IOException, InterruptedException {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Image image = new Image(fileInputStream);
            DoubleBinding height = new DoubleBinding() {
                @Override
                protected double computeValue() {
                    return anchorPane.getHeight() -(anchorPane.getInsets().getTop() + anchorPane.getInsets().getBottom());
                }
            };
            DoubleBinding width = new DoubleBinding() {
                @Override
                protected double computeValue() {
                    return anchorPane.getWidth() - (anchorPane.getInsets().getRight() + anchorPane.getInsets().getLeft());
                }
            };

            anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
                height.invalidate();
            });
            anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
                width.invalidate();
            });

            BackgroundImage backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.SPACE,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(100.0, 100.0, false, false, false, true));
            Background background = new Background(backgroundImage);
            anchorPane.setBackground(background);
            fileInputStream.close();
            Thread.sleep(10000);
        }

    }

    public static Parent getParent(){
        try{
            return FXMLLoader.load(WaitingSceneSlideController.class.getResource("WaitingScene-Slide.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private WaitingScene getWaitingScene() throws IOException {
        Path path = Paths.get("Setting", "WaitingScene");
        File videoFile = new File(path.toAbsolutePath().toString() + "\\WaitingScene.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(videoFile, WaitingScene.class);
    }

    private void setScene(Parent parent, MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private synchronized void slideImage() throws InterruptedException {

        WaitingScene waitingScene = null;
        try {
            waitingScene = getWaitingScene();
        } catch (IOException e) {
            System.out.println("Không tìm được file hình ảnh cho waiting scene");
        }

        ArrayList<String>  listPath = waitingScene.getPath();

        ImageAnchorPane imageAnchorPane = new ImageAnchorPane();
        imageAnchorPane.setAnchorPane(backgroundAnchorPane);

        ExecutorService executorService = Executors.newFixedThreadPool(listPath.size());

        for(int i =0; i<listPath.size(); i++){
            int finalI = i;
            Runnable runnable = () -> {
                try {
                    imageAnchorPane.setImage(listPath.get(finalI));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            executorService.submit(thread);
        }

        backgroundAnchorPane.setOnMouseClicked(event -> {
            executorService.shutdown();
            setScene(CQuestionsController.getParent(), event);
        });

        executorService.shutdown();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            slideImage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
