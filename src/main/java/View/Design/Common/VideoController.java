package View.Design.Common;

import View.Design.Client.Questions.CQuestionsController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoController extends Application implements Initializable {

    @FXML private Pane parentVideo;
    @FXML private AnchorPane coverLayout;
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
    private String getPathVideo(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/main/java/Data/WaitingScene/WaitingSceneData.txt")));
            String pathVideo = bufferedReader.readLine();
            bufferedReader.close();
            return pathVideo;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coverVideo = new Pane();
        String pathVideo = getPathVideo();
        System.out.println(pathVideo);

        File file = new File(pathVideo);

        MediaView mediaView = createMediaView(coverVideo);
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setSmooth(true);
        mediaView.setMediaPlayer(mediaPlayer);

        parentVideo.setPadding(new Insets(20.0));
        coverVideo.getChildren().add(mediaView);
        parentVideo.getChildren().add(coverVideo);

        double horizontalPadding = (parentVideo.getWidth() - coverVideo.getWidth())/2;
        parentVideo.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = (double) newValue;
            System.out.println((newWidth - coverVideo.getWidth())/2);
        });
//        parentVideo.setPadding(new Insets());
        coverLayout.setOnMouseClicked(e->{
//            setScene(CQuestionsController.getParent(), e);
        });

        coverLayout.setOnTouchPressed(e->{

        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        String content_Url = "<iframe width=\"560\" height=\"315\" src=\"https://vuighe.net/onmyou-taisenki/tap-1-ha-than-bach-ho-kogenta\" frameborder=\"0\" allowfullscreen></iframe>";
//        WebView webView = new WebView();
//
//        webView.getEngine().loadContent(content_Url);
//
//        StackPane root = new StackPane();
//        root.getChildren().add(webView);
//
//        Scene scene = new Scene(root, 300, 250);
//
//        primaryStage.setTitle("http://java-buddy.blogspot.com/");
//        primaryStage.setScene(scene);
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();

   }
}
