package Utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StageConfigure {
    public static void fullStage(Stage stage){
        Screen screen = Screen.getPrimary();
        Rectangle2D rectangle2D = screen.getVisualBounds();
        stage.setX(rectangle2D.getMinX());
        stage.setY(rectangle2D.getMinY());
        stage.setHeight(rectangle2D.getHeight());
        stage.setWidth(rectangle2D.getWidth());

    }
}
