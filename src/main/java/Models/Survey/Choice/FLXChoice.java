package Models.Survey.Choice;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FLXChoice implements Choice{
    private SimpleStringProperty enContentChoice;
    private SimpleStringProperty viContentChoice;
    private SimpleStringProperty urlImg;

    public FLXChoice(){
        urlImg = new SimpleStringProperty();
        urlImg.set("src/main/resources/View/Image/facebook.png");
    }
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


    public String getUrlImg(){
        return urlImgProperty().get();
    }
    public void setUrlImg(String data){
        urlImgProperty().set(data);
    }
    public StringProperty urlImgProperty(){
        if(urlImg== null){
            urlImg = new SimpleStringProperty();
        }
        return urlImg;
    }


    @Override
    public void getChoice() {

    }
}
