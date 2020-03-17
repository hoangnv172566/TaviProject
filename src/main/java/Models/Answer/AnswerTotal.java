package Models.Answer;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class AnswerTotal {
    private SimpleLongProperty answerTotalID;
    private SimpleStringProperty nameOfAnswerTotal;
    private SimpleLongProperty surveyTotalID;
    private SimpleLongProperty deviceKioskID;
    private ArrayList<SubAnswer> listAnswer;

    public LongProperty answerTotalIDProperty(){
        if(answerTotalID == null){
            answerTotalID = new SimpleLongProperty(0);
        }
        return answerTotalID;
    }

    public long getAnswerTotalID(){
        return answerTotalIDProperty().get();
    }

    public void setAnswerTotalID(long value){
        answerTotalIDProperty().set(value);
    }

    public StringProperty nameOfAnswerTotalProperty(){
        if(nameOfAnswerTotal == null){
            nameOfAnswerTotal = new SimpleStringProperty();
        }
        return nameOfAnswerTotal;
    }

    public String getNameOfAnswerTotal(){
        return nameOfAnswerTotalProperty().get();
    }

    public void setNameOfAnswerTotal(String value){
        nameOfAnswerTotalProperty().set(value);
    }


    public LongProperty surveyTotalIDProperty(){
        if(surveyTotalID == null){
            surveyTotalID = new SimpleLongProperty(0);
        }
        return surveyTotalID;
    }

    public long getSurveyTotalID(){
        return surveyTotalIDProperty().get();
    }

    public void setSurveyTotalID(long value){
        surveyTotalIDProperty().set(value);
    }


    public LongProperty deviceKioskIDProperty(){
        if(deviceKioskID == null){
            deviceKioskID = new SimpleLongProperty();
        }
        return deviceKioskID;
    }

    public long getDeviceKioskID(){
        return deviceKioskIDProperty().get();
    }

    public void setDeviceKioskID(long value){
        deviceKioskIDProperty().set(value);
    }

    public ArrayList<SubAnswer> getListAnswer(){
        return listAnswer;
    }
    public void setListAnswer(ArrayList<SubAnswer> data){
        if(listAnswer == null){
            listAnswer = new ArrayList<>();
        }
        listAnswer.addAll(data);
    }

    @Override
    public String toString() {
        return "AnswerTotal{" +
                "nameOfAnswerTotal=" + nameOfAnswerTotal +
                ", surveyTotalID=" + surveyTotalID +
                ", deviceKioskID=" + deviceKioskID +
                ", listAnswer=" + listAnswer +
                '}';
    }
}
