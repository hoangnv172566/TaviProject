package Models.Survey.Choice;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SingleChoice implements Choice{
    private SimpleStringProperty enContentChoice;
    private SimpleStringProperty viContentChoice;
    private SimpleLongProperty ord;
    private SimpleLongProperty sampleAnswerID;

    public String getEnContentChoice(){
        return enContentChoiceProperty().get();
    }
    public void setEnContentChoice(String data){
        enContentChoiceProperty().set(data);
    }

    public StringProperty enContentChoiceProperty(){
        if(enContentChoice == null){
            enContentChoice = new SimpleStringProperty();
        }
        return enContentChoice;
    }
    public String getViContentChoice(){
        return viContentChoiceProperty().get();
    }
    public void setViContentChoice(String data){
        viContentChoiceProperty().set(data);
    }

    public StringProperty viContentChoiceProperty(){
        if(viContentChoice == null){
            viContentChoice = new SimpleStringProperty();
        }
        return viContentChoice;
    }


    public LongProperty ordProperty(){
        if(ord == null){
            ord = new SimpleLongProperty(0);
        }
        return ord;
    }

    public void setOrd(long value){
        ordProperty().set(value);
    }
    public long getOrd(){
        return ordProperty().get();
    }

    public LongProperty sampleAnswerIDProperty(){
        if(sampleAnswerID == null){
            sampleAnswerID = new SimpleLongProperty();
        }
        return sampleAnswerID;
    }

    public void setSampleAnswerID(long value){
        sampleAnswerIDProperty().set(value);
    }
    public long getSampleAnswerID(){
        return sampleAnswerIDProperty().get();
    }

    @Override
    public String toString() {
        return "SingleChoice{" +
                "enContentChoice=" + enContentChoice +
                ", viContentChoice=" + viContentChoice +
                ", ord=" + ord +
                ", sampleAnswerID=" + sampleAnswerID +
                '}';
    }

    @Override
    public void getChoice() {

    }
}
