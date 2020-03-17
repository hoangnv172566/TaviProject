package Models.Answer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class SubAnswer {
    private SimpleLongProperty subAnswerID;
//    private SimpleIntegerProperty answerTotalID;
//    private SimpleIntegerProperty surveyID;
    private SimpleLongProperty ord;


    //subAnswerID
    public LongProperty subAnswerIDProperty(){
        if(subAnswerID == null){
            subAnswerID = new SimpleLongProperty();
        }
        return subAnswerID;
    }
    public long getSubAnswerID(){
        return subAnswerIDProperty().get();
    }
    public void setSubAnswerID(long value){
        subAnswerIDProperty().set(value);
    }


    //answerTotalID
//    public IntegerProperty answerTotalIDProperty(){
//        if(answerTotalID == null){
//            answerTotalID = new SimpleIntegerProperty();
//        }
//        return answerTotalID;
//    }
//    public int getAnsTotalID(){
//        return answerTotalIDProperty().get();
//    }
//    public void setAnswerTotalID(int value){
//        answerTotalIDProperty().set(value);
//    }


    //surveyID
//    public IntegerProperty surveyIDProperty(){
//        if(surveyID == null){
//            surveyID = new SimpleIntegerProperty();
//        }
//        return surveyID;
//    }
//    public int getSurveyID(){
//        return surveyIDProperty().get();
//    }
//    public void setSurveyID(int value){
//        surveyIDProperty().set(value);
//    }

    //ord
    public LongProperty ordProperty(){
        if(ord == null){
            ord = new SimpleLongProperty();
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
