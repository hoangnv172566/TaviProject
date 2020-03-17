package Models.Answer.AnswerMultipleChoice;

import Models.Answer.SubAnswer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class AnswerMultipleChoice extends SubAnswer {
//    private SimpleIntegerProperty sampleAnswerID;
    private ArrayList<AnswerChoice> listAnswerMultiChoice;


    public AnswerMultipleChoice(){
        listAnswerMultiChoice = new ArrayList<>();
    }
//    public IntegerProperty sampleAnswerIDProperty(){
//        if(sampleAnswerID == null){
//            sampleAnswerID = new SimpleIntegerProperty(0);
//        }
//        return sampleAnswerID;
//    }
//    public int getSampleAnswerID(){
//        return sampleAnswerIDProperty().get();
//    }
//    public void setSampleAnswerID(int value){
//        sampleAnswerIDProperty().set(value);
//    }

    public ArrayList<AnswerChoice> getListAnswerMultiChoice(){
        if(listAnswerMultiChoice == null){
            listAnswerMultiChoice = new ArrayList<>();
        }
        return listAnswerMultiChoice;
    }
    public void setListAnswerMultiChoice(ArrayList<AnswerChoice> value){
        listAnswerMultiChoice = new ArrayList<>();
        if(value !=null){
            listAnswerMultiChoice.addAll(value);
        }
    }

}
