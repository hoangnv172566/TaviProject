package Service.impl;

import Models.User.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.io.IOException;

public class UserService  {



    public int login(User user, String API) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        HttpPost request = new HttpPost(API);//connect to api
        JSONObject jsonObject = new JSONObject();// create jsonOb to pass as a para of StringEntity

        jsonObject.put("password", user.getPassword());
        jsonObject.put("username", user.getUsername());

        StringEntity params =new StringEntity(jsonObject.toString());

        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = httpClient.execute(request);//executing the request
        return response.getStatusLine().getStatusCode();

    }

    public void logout(User user, String API) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(API);
        JSONObject userJson = new JSONObject();

        userJson.put("password", user.getPassword());
        userJson.put("username", user.getUsername());

        StringEntity params = new StringEntity(userJson.toString());

        request.addHeader("content-type", "application/json");
        request.setEntity(params);

       httpClient.execute(request);
    }

    public boolean checkLogin(User user){
        if(checkUsername(user.getUsername())){
            if(checkPassword(user.getPassword())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }


    }

    private boolean checkPassword(String password) {
        return false;
    }

    private boolean checkUsername(String username) {
        return false;
    }

    public User getUser(){
        return null;
    }


}
