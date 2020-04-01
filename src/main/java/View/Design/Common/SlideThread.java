package View.Design.Common;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SlideThread implements Runnable {

    String fileImagePath;
    AnchorPane anchorPane;
    long timeSleep;
    public void setTimeSleep(long timeSleep){
        this.timeSleep = timeSleep;
    }
    public void setAnchorPane(AnchorPane anchorPane){
        this.anchorPane = anchorPane;
    }
    public void setFileImagePath(String fileImagePath) {
        this.fileImagePath = fileImagePath;
    }

    @Override
    public synchronized void run() {

            Platform.runLater(() -> {
                    try{
                        anchorPane.setBackground(null);
                        Thread.sleep(timeSleep);
                        File file = new File(fileImagePath);
                        FileInputStream fis = new FileInputStream(file);
                        Image image = new Image(fis);
                        BackgroundImage backgroundImage = new BackgroundImage(image,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(100.0, 100.0, true, true, true, true));
                        Background background = new Background(backgroundImage);
                        anchorPane.setBackground(background);
                        fis.close();

                    }catch(IOException | InterruptedException ignored){

                    }

                });



        }
}

