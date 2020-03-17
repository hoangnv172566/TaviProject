package Service.impl;

import Config.URLApi;
import Models.Thanks.Thanks;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ThankService {

    private String convertUTF8(String src){
        return new String(src.getBytes(), StandardCharsets.UTF_8);
    }
    public String gettingContentThank(){
        try{
            byte[] responseByte = Request.Get(URLApi.THANKS).execute().returnContent().asBytes();
            String response = new String(responseByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            JSONObject responseJSon = (JSONObject) jsonParser.parse(response);
            JSONObject data = (JSONObject) responseJSon.get("data");
            String content = (String) data.get("content");
            return content;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Thanks gettingThank(){
        Thanks loiCamOn = new Thanks();
        loiCamOn.setContent(gettingContentThank());
        return loiCamOn;
    }
}
