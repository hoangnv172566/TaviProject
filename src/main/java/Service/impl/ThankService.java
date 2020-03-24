package Service.impl;

import Config.URLApi;
import Models.Thanks.Thanks;
import Models.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThankService {

    private String convertUTF8(String src){
        return new String(src.getBytes(), StandardCharsets.UTF_8);
    }

    private User getUserData() throws IOException {
        Path path = Paths.get("Data", "User");
        File file = new File(path.toAbsolutePath().toString() + "\\UserData.json");
        return new ObjectMapper().readValue(file, User.class);
    }

    public int gettingContentThank(){
        try{
            User user = getUserData();

            byte[] responseByte = Request.Get(URLApi.THANKS + user.getThankID()).execute().returnContent().asBytes();
            String response = new String(responseByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            JSONObject responseJSon = (JSONObject) jsonParser.parse(response);
            JSONObject data = (JSONObject) responseJSon.get("data");
            String content = (String) data.get("content");

            Thanks thanks = new Thanks();
            thanks.setContent(content);
            Path path = Paths.get("Data", "Thanks");
            Files.createDirectories(path);
            File thankData = new File(path.toAbsolutePath().toString() + "\\ThankData.json");
            if(!thankData.exists()){
                thankData.createNewFile();
            }
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(thankData, thanks);
            return 200;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return 400;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 500;
    }

}
