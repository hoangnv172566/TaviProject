package Models.Answer.AnswerOpen;

import Models.Answer.SubAnswer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AnswerOpen extends SubAnswer {
    private SimpleStringProperty contentAnswer;

    public StringProperty contentAnswerProperty(){
        if(contentAnswer == null){
            contentAnswer = new SimpleStringProperty();
        }
        return contentAnswer;
    }

    public void setContentAnswer(String value){
        contentAnswerProperty().set(value);
    }

    public String getContentAnswer(){
        return contentAnswerProperty().get();
    }
}
