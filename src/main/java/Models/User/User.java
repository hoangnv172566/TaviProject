package Models.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class User {
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty role;


    public User(){}
    public User(String username, String password){
        this.username.set(username);
        this.password.set(password);
    }

    public String getUsername(){
        return usernameProperty().get();
    }
    public void setUsername(String data){
        usernameProperty().set(data);
    }
    public StringProperty usernameProperty(){
        if(username == null){
            username = new SimpleStringProperty();
        }
        return username;

    }

    public String getPassword(){
        return passwordProperty().get();
    }
    public void setPassword(String data){
        passwordProperty().set(data);
    }
    public StringProperty passwordProperty(){
        if(password == null){
            password = new SimpleStringProperty();
        }
        return password;

    }

}
