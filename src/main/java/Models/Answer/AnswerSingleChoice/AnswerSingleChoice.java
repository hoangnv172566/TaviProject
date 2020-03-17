package Models.Answer.AnswerSingleChoice;

import Models.Answer.AnswerMultipleChoice.AnswerChoice;
import Models.Answer.SubAnswer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class AnswerSingleChoice extends SubAnswer {
    private SimpleLongProperty sampleAnswerID;

    public LongProperty sampleAnswerIDProperty(){
        if(sampleAnswerID == null){
            sampleAnswerID = new SimpleLongProperty(0);
        }
        return sampleAnswerID;
    }

    public long getSampleAnswerID(){
        return sampleAnswerIDProperty().get();
    }
    public void setSampleAnswerID(long value){
        sampleAnswerIDProperty().set(value);
    }
}
