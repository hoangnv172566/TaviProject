package Models.Answer.AnswerContact;

import Models.Answer.SubAnswer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AnswerContact extends SubAnswer {
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;
    private SimpleStringProperty address;


    //name
    public StringProperty nameProperty(){
        if(name == null){
            name = new SimpleStringProperty();
        }
        return name;
    }

    public String getName(){
        return nameProperty().get();
    }
    public void setName(String value){
        nameProperty().set(value);
    }

    //phone
    public StringProperty phoneProperty(){
        if(phone == null){
            phone = new SimpleStringProperty();
        }
        return phone;
    }

    public String getPhone(){
        return phoneProperty().get();
    }
    public void setPhone(String value){
        phoneProperty().set(value);
    }

    //email
    public StringProperty emailProperty(){
        if(email == null){
            email = new SimpleStringProperty();
        }
        return email;
    }

    public String getEmail(){
        return emailProperty().get();
    }
    public void setEmail(String value){
        emailProperty().set(value);
    }

    //address
    public StringProperty addressProperty(){
        if(address == null){
            address = new SimpleStringProperty();
        }
        return address;
    }

    public String getAddress(){
        return addressProperty().get();
    }
    public void setAddress(String value){
        addressProperty().set(value);
    }

}
