package Models.Survey.Choice;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NPSChoice implements Choice{
    private SimpleStringProperty enContentChoice;
    private SimpleStringProperty viContentChoice;

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


    @Override
    public void getChoice() {

    }
}
