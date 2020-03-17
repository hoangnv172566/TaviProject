package Models.Answer.AnswerMultipleChoice;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class AnswerChoice  {
    private SimpleLongProperty sampleAnswerID;
    private SimpleLongProperty ord;

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

    public LongProperty ordProperty(){
        if(ord == null){
            ord = new SimpleLongProperty(0);
        }
        return ord;
    }

    public long getOrd(){
        return ordProperty().get();
    }
    public void setOrd(long value){
        ordProperty().set(value);
    }

}
