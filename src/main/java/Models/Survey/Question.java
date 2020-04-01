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
    private ContactField contactField;

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

    public ContactField getContactField() {
        return contactField;
    }

    public void setContactField(ContactField contactField) {
        this.contactField = contactField;
    }

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
