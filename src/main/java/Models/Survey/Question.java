package Models.Survey;

import Models.Survey.Choice.Choice;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    //info for mapping
    private long questionID;
    private long ord;
    private boolean enable;
    private boolean require;

    //display on the scene
    private String viContent;
    private String enContent;
    private String type;
    private ArrayList<Choice> listchoice;
    private long maxLevel;

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public long getOrd() {
        return ord;
    }

    public void setOrd(long ord) {
        this.ord = ord;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public String getViContent() {
        return viContent;
    }

    public void setViContent(String viContent) {
        this.viContent = viContent;
    }

    public String getEnContent() {
        return enContent;
    }

    public void setEnContent(String enContent) {
        this.enContent = enContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Choice> getChoice() {
        return listchoice;
    }

    public void setChoice(ArrayList<Choice> listchoice) {
        this.listchoice = listchoice;
    }

    public long getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(long maxLevel) {
        this.maxLevel = maxLevel;
    }

//    public LongProperty questionIDProperty(){
//        if(questionID == null){
//            questionID = new SimpleLongProperty();
//        }
//        return questionID;
//    }
//
//    public void setQuestionID(long value){
//        questionIDProperty().set(value);
//    }
//    public long getQuestionID(){
//        return questionIDProperty().get();
//    }
//
//
//    public LongProperty ordProperty(){
//        if(ord == null){
//            ord = new SimpleLongProperty();
//        }
//        return ord;
//    }
//
//    public void setOrd(long value){
//        ordProperty().set(value);
//    }
//    public long getOrd(){
//        return ordProperty().get();
//    }
//
//    public BooleanProperty enableProperty(){
//        if(enable == null){
//            enable = new SimpleBooleanProperty(true);
//        }
//        return enable;
//    }
//
//    public void setEnable(boolean value){
//        enableProperty().set(value);
//    }
//    public boolean getEnable(){
//        return enableProperty().get();
//    }
//
//    public String getViContent(){
//        return viContentProperty().get();
//    }
//
//    public void setViContent(String data){
//        viContentProperty().set(data);
//    }
//
//    public StringProperty viContentProperty(){
//        if(viContent == null){
//            viContent = new SimpleStringProperty();
//        }
//        return viContent;
//    }
//
//    public String getEnContent(){
//        return enContentProperty().get();
//    }
//
//    public void setEnContent(String data){
//        enContentProperty().set(data);
//    }
//
//    public StringProperty enContentProperty(){
//        if(enContent == null){
//            enContent = new SimpleStringProperty();
//        }
//        return enContent;
//    }
//
//    public String getType(){
//        return typeProperty().get();
//    }
//    public void setType(String data){
//        typeProperty().set(data);
//    }
//
//    public StringProperty typeProperty(){
//        if(type == null){
//            type = new SimpleStringProperty();
//        }
//        return type;
//
//    }
//
//    public ArrayList<Models.Survey.Choice.Choice> getChoice(){
//        return this.listchoice;
//    }
//    public void setChoice(ArrayList<Choice> data){
//        if(listchoice == null){
//            listchoice = new ArrayList<>();
//        }
//        listchoice.addAll(data);
//    }
//
//    public LongProperty maxLevelProperty(){
//        if(maxLevel == null){
//            maxLevel = new SimpleLongProperty();
//        }
//        return maxLevel;
//    }
//    public void setMaxLevel(long value){
//        maxLevelProperty().set(value);
//    }
//    public long getMaxLevel(){
//        return maxLevelProperty().get();
//    }
//    public boolean isEnable() {
//        return enable.get();
//    }
//
//    public boolean isRequire() {
//        return require;
//    }
//
//    public void setRequire(boolean require) {
//        this.require = require;
//    }
//
//    public ArrayList<Choice> getListchoice() {
//        return listchoice;
//    }
//
//    public void setListchoice(ArrayList<Choice> listchoice) {
//        this.listchoice = listchoice;
//    }


    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", ord=" + ord +
                ", enable=" + enable +
                ", require=" + require +
                ", viContent=" + viContent +
                ", enContent=" + enContent +
                ", type=" + type +
                ", listchoice=" + listchoice +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
