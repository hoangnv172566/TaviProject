package Service.impl;

import Models.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        if(response.getStatusLine().getStatusCode() == 200){
            String responseString = EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            try{
                JSONObject responseJs = (JSONObject) parser.parse(responseString);
                JSONObject data = (JSONObject) responseJs.get("data");
                JSONObject appCollectionData = (JSONObject) data.get("appCollection");
                JSONObject surveyTotalData = (JSONObject) data.get("surveyTotal");
                JSONObject companyData = (JSONObject) data.get("company");


                long appCollectionID = (long) appCollectionData.get("id");
                long surveyTotalID = (long) surveyTotalData.get("id");
                long companyID = (long) companyData.get("id");
                long thankID = (long) data.get("thankStyle");

                User newUser = new User();
                newUser.setUsername(user.getUsername());
                newUser.setPassword(user.getPassword());
                newUser.setCollectionID(appCollectionID);
                newUser.setCompanyID(companyID);
                newUser.setSurveyID(surveyTotalID);
                newUser.setThankID(thankID);

                //write data if success
                Path path = Paths.get("Data", "User");
                Files.createDirectories(path);
                File userFile = new File(path.toAbsolutePath().toString() + "\\UserData.json");
                if(userFile.exists()){
                    userFile.createNewFile();
                }
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(userFile, newUser);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

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

}
