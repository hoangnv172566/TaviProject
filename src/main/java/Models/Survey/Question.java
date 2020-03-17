package Models.Survey;

import Models.Survey.Choice.Choice;
import javafx.beans.property.*;

import java.util.ArrayList;

public class Question {
    //infor for mapping
    private SimpleLongProperty questionID;
    private SimpleLongProperty ord;
    private SimpleBooleanProperty enable;

    //display on the scene
    private SimpleStringProperty viContent;
    private SimpleStringProperty enContent;
    private SimpleStringProperty type;
    private ArrayList<Models.Survey.Choice.Choice> listchoice;
    private SimpleLongProperty maxLevel;

    public LongProperty questionIDProperty(){
        if(questionID == null){
            questionID = new SimpleLongProperty();
        }
        return questionID;
    }

    public void setQuestionID(long value){
        questionIDProperty().set(value);
    }
    public long getQuestionID(){
        return questionIDProperty().get();
    }


    public LongProperty ordProperty(){
        if(ord == null){
            ord = new SimpleLongProperty();
        }
        return ord;
    }

    public void setOrd(long value){
        ordProperty().set(value);
    }
    public long getOrd(){
        return ordProperty().get();
    }

    public BooleanProperty enableProperty(){
        if(enable == null){
            enable = new SimpleBooleanProperty(true);
        }
        return enable;
    }

    public void setEnable(boolean value){
        enableProperty().set(value);
    }
    public boolean getEnable(){
        return enableProperty().get();
    }

    public String getViContent(){
        return viContentProperty().get();
    }

    public void setViContent(String data){
        viContentProperty().set(data);
    }

    public StringProperty viContentProperty(){
        if(viContent == null){
            viContent = new SimpleStringProperty();
        }
        return viContent;
    }

    public String getEnContent(){
        return enContentProperty().get();
    }

    public void setEnContent(String data){
        enContentProperty().set(data);
    }

    public StringProperty enContentProperty(){
        if(enContent == null){
            enContent = new SimpleStringProperty();
        }
        return enContent;
    }

    public String getType(){
        return typeProperty().get();
    }
    public void setType(String data){
        typeProperty().set(data);
    }

    public StringProperty typeProperty(){
        if(type == null){
            type = new SimpleStringProperty();
        }
        return type;

    }

    public ArrayList<Models.Survey.Choice.Choice> getChoice(){
        return this.listchoice;
    }
    public void setChoice(ArrayList<Choice> data){
        if(listchoice == null){
            listchoice = new ArrayList<>();
        }
        listchoice.addAll(data);
    }

    public LongProperty maxLevelProperty(){
        if(maxLevel == null){
            maxLevel = new SimpleLongProperty();
        }
        return maxLevel;
    }
    public void setMaxLevel(long value){
        maxLevelProperty().set(value);
    }
    public long getMaxLevel(){
        return maxLevelProperty().get();
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", ord=" + ord +
                ", enable=" + enable +
                ", viContent=" + viContent +
                ", enContent=" + enContent +
                ", type=" + type +
                ", listchoice=" + listchoice +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
