package Models.Answer.AnswerLevelGroup;

import Models.Answer.SubAnswer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AnswerLevel extends SubAnswer {
    private SimpleIntegerProperty level;

    public IntegerProperty levelProperty(){
        if(level == null){
            level = new SimpleIntegerProperty();
        }
        return level;
    }
    public int getLevel(){
        return levelProperty().get();
    }
    public void setLevel(int value){
        levelProperty().set(value);
    }



}
