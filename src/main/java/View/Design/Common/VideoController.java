package View.Design.Common;

import Models.Setting.WaitingScene;
import View.Design.Client.Questions.CQuestionsController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class VideoController extends Application implements Initializable {

    @FXML private Pane parentVideo;
    @FXML private AnchorPane coverLayout;
    @FXML private MediaView mediaViewVideo;
    @FXML private JFXToggleButton switchConfig;

    private Pane coverVideo;

    public static Parent getParent() {
        try{
            return FXMLLoader.load(VideoController.class.getResource("Video.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void setScene(Parent parent, MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private MediaView createMediaView(Pane root){
        MediaView mediaView = new MediaView();

        DoubleBinding widthProperty = new DoubleBinding() {
            @Override
            protected double computeValue() {
                return root.getWidth() - (root.getInsets().getLeft() + root.getInsets().getRight());
            }
        };

        DoubleBinding heightProperty = new DoubleBinding() {
            @Override
            protected double computeValue() {
                return root.getHeight() - (root.getInsets().getTop() + root.getInsets().getBottom());
            }
        };

        mediaView.fitWidthProperty().bind(widthProperty);
//        mediaView.fitHeightProperty().bind(heightProperty);

        root.widthProperty().addListener(observable -> {
            widthProperty.invalidate();
        });

        root.heightProperty().addListener(observable -> {
            heightProperty.invalidate();
        });

        Rectangle clipRegion = new Rectangle();
        clipRegion.setArcWidth(10);
        clipRegion.setArcHeight(10);
        clipRegion.widthProperty().bind(widthProperty);
        clipRegion.heightProperty().bind(heightProperty);
        mediaView.setClip(clipRegion);
        // Update calculations on invalidation listener
        // to reposition media view and clip region
        root.insetsProperty().addListener(observable -> {
            widthProperty.invalidate();
            heightProperty.invalidate();
            Insets insets = root.getInsets();
            clipRegion.setX(insets.getRight());
            clipRegion.setY(insets.getTop());
            mediaView.setX(insets.getRight());
            mediaView.setY(insets.getTop());
        });

        return mediaView;
    }
    private WaitingScene getWaitingScene(){
        Alert alert;
        Path path = Paths.get("Setting", "WaitingScene");
        File videoFile = new File(path.toAbsolutePath().toString() + "\\WaitingScene.json");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(videoFile, WaitingScene.class);
        } catch (JsonParseException e) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Không mapping được dữ liệu waitingScene");
            alert.showAndWait();
        } catch (JsonMappingException e) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Không mapping được dữ liệu waitingScene");
            alert.showAndWait();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ko đọc được file!");
            alert.show();
        }
        return null;
    }
    private WaitingScene getDefaultSettingWaitingScene(){
        Path path = Paths.get("FixedSetting","DefaultWaitingScene");
        File video = new File(path.toAbsolutePath().toString() + "\\WaitingScene.json");
        try{
            return new ObjectMapper().readValue(video, WaitingScene.class);
        } catch (JsonParseException | JsonMappingException e) {
            System.out.println("Không thể mapping dữ liệu tại videoController");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Không có cài đặt cho màn hình chờ!");
            alert.showAndWait();
        }
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coverVideo = new Pane();
        WaitingScene waitingScene = getWaitingScene();
        if(waitingScene == null){
            waitingScene = getDefaultSettingWaitingScene();
        }

        String pathVideo = waitingScene.getPath().get(0);

        File file = new File(pathVideo);

        mediaViewVideo = createMediaView(coverVideo);
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
        mediaViewVideo.setSmooth(true);
        mediaViewVideo.setMediaPlayer(mediaPlayer);

        parentVideo.setPadding(new Insets(20.0));
        coverVideo.getChildren().add(mediaViewVideo);
        parentVideo.getChildren().add(coverVideo);

//        parentVideo.setPadding(new Insets());
        mediaViewVideo.setOnMouseClicked(e->{
            mediaPlayer.stop();
            setScene(CQuestionsController.getParent(), e);
        });


    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
