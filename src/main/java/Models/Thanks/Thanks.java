package Models.Thanks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Thanks {
    private SimpleStringProperty content;

    public String getContent(){
       return contentProperty().get();


    }
    public void setContent(String value){
        contentProperty().set(value);
    }
    public StringProperty contentProperty(){
        if(content == null){
            content = new SimpleStringProperty();
        }
        return content;
    }
}
