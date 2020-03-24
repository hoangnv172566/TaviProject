package Models.Survey.Choice;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class CSATChoice extends Choice implements Serializable {

    private String enContentChoice;
    private String viContentChoice;
    private String urlImg;

    public String getEnContentChoice() {
        return enContentChoice;
    }

    public void setEnContentChoice(String enContentChoice) {
        this.enContentChoice = enContentChoice;
    }

    public String getViContentChoice() {
        return viContentChoice;
    }

    public void setViContentChoice(String viContentChoice) {
        this.viContentChoice = viContentChoice;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    //    public CSATChoice(){
//        urlImg = new SimpleStringProperty();
//        urlImg.set("src/main/resources/View/Image/facebook.png");
//    }
//    public String getEnContentChoice(){
//        return enContentChoiceProperty().get();
//    }
//    public void setEnContentChoice(String data){
//        enContentChoiceProperty().set(data);
//    }
//
//    public StringProperty enContentChoiceProperty(){
//        if(enContentChoice == null){
//            enContentChoice = new SimpleStringProperty();
//        }
//        return enContentChoice;
//    }
//
//    public String getViContentChoice(){
//        return viContentChoiceProperty().get();
//    }
//    public void setViContentChoice(String data){
//        viContentChoiceProperty().set(data);
//    }
//
//    public StringProperty viContentChoiceProperty(){
//        if(viContentChoice == null){
//            viContentChoice = new SimpleStringProperty();
//        }
//        return viContentChoice;
//    }
//
//
//    public String getUrlImg(){
//        return urlImgProperty().get();
//    }
//    public void setUrlImg(String data){
//        urlImgProperty().set(data);
//    }
//    public StringProperty urlImgProperty(){
//        if(urlImg== null){
//            urlImg = new SimpleStringProperty();
//        }
//        return urlImg;
//    }

}
