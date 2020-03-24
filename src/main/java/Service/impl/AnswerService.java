package Service.impl;

import Config.URLApi;
import Models.Answer.AnswerContact.AnswerContact;
import Models.Answer.AnswerLevelGroup.AnswerLevel;
import Models.Answer.AnswerMultipleChoice.AnswerChoice;
import Models.Answer.AnswerMultipleChoice.AnswerMultipleChoice;
import Models.Answer.AnswerOpen.AnswerOpen;
import Models.Answer.AnswerSingleChoice.AnswerSingleChoice;
import Models.Answer.AnswerTotal;
import Models.Answer.SubAnswer;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AnswerService {
    public long getAnswerTotalID() throws IOException {
        long idAnswer = 0;
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        HttpPost request = new HttpPost(URLApi.GET_ID_ANSWER);//connect to api
        JSONObject jsonObject = new JSONObject();// create jsonOb to pass as a para of StringEntity

        jsonObject.put("deviceKioskId", 1);
        jsonObject.put("deviceType", 1);
        jsonObject.put("deviceWebId", 0);


        StringEntity params =new StringEntity(jsonObject.toString());

        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = httpClient.execute(request);//executing the request
        JSONParser jsonParser = new JSONParser();
        try{
            JSONObject responseJS = (JSONObject) jsonParser.parse(EntityUtils.toString(response.getEntity()));
            idAnswer =(long) responseJS.get("data");
            return idAnswer;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return idAnswer;
    }

    public long getTypeAnswerAsCode(long code) {
        long result = 0;
        String URL = "http://103.9.86.61:8080/admin_srs/api/v1/public/survey/find-by-id?id=" + code;
        try{
            byte[] responseByte = Request.Get(URL).execute().returnContent().asBytes();
            String response = new String(responseByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            JSONObject responseJS = (JSONObject) jsonParser.parse(response);
            JSONObject data = (JSONObject) responseJS.get("data");
            result = (long) data.get("type");
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long uploadAnswerTotal(AnswerTotal data){

        JSONObject answerTotalJson = new JSONObject();
        answerTotalJson.put("answerTotalId", data.getAnswerTotalID());

        ArrayList<SubAnswer> listSubAnswer = data.getListAnswer();

        ArrayList<JSONObject> listLevelAnswer = new ArrayList<>();
        ArrayList<JSONObject> listContactAnswer = new ArrayList<>();
        ArrayList<JSONObject> listOpenAnswer = new ArrayList<>();
        ArrayList<JSONObject> listSingleAnswer = new ArrayList<>();
        ArrayList<JSONObject> listMultipleAnswer = new ArrayList<>();

        listSubAnswer.forEach(subAnswer -> {
            long typeSubAnswerAsCode = getTypeAnswerAsCode(subAnswer.getSubAnswerID());
            if(typeSubAnswerAsCode==1||typeSubAnswerAsCode==2||typeSubAnswerAsCode==3||typeSubAnswerAsCode==4||typeSubAnswerAsCode==7){
                JSONObject levelAnswerJson = new JSONObject();
                levelAnswerJson.put("level", ((AnswerLevel) subAnswer).getLevel());
                levelAnswerJson.put("surveyID", subAnswer.getSubAnswerID());
                listLevelAnswer.add(levelAnswerJson);

            }else if(typeSubAnswerAsCode==6){
                JSONObject singleAnswerJson = new JSONObject();
                singleAnswerJson.put("sampleAnswerId", ((AnswerSingleChoice) subAnswer).getSampleAnswerID());
                singleAnswerJson.put("surveyId", subAnswer.getSubAnswerID());
                listSingleAnswer.add(singleAnswerJson);
            }else if(typeSubAnswerAsCode == 5){
                JSONObject multipleAnswerJson = new JSONObject();
                multipleAnswerJson.put("surveyId", subAnswer.getSubAnswerID());

                ArrayList<AnswerChoice> listChoice = ((AnswerMultipleChoice) subAnswer).getListAnswerMultiChoice();
                ArrayList<Long> listChoiceID = new ArrayList<>();
                listChoice.forEach(answerChoice -> {
                    long id = answerChoice.getSampleAnswerID();
                    listChoiceID.add(id);
                });

                multipleAnswerJson.put("sampleAnswerIds", listChoiceID);
                listMultipleAnswer.add(multipleAnswerJson);
            }else if(typeSubAnswerAsCode==8) {
                JSONObject openAnswerJson = new JSONObject();
                openAnswerJson.put("surveyId", subAnswer.getSubAnswerID());
                openAnswerJson.put("answer", ((AnswerOpen) subAnswer).getContentAnswer());
                listOpenAnswer.add(openAnswerJson);
            } else if(typeSubAnswerAsCode==9){
                JSONObject contactAnswerJson = new JSONObject();
                contactAnswerJson.put("name", ((AnswerContact) subAnswer).getName());
                contactAnswerJson.put("email", ((AnswerContact) subAnswer).getEmail());
                contactAnswerJson.put("address", ((AnswerContact) subAnswer).getAddress());
                contactAnswerJson.put("phone", ((AnswerContact) subAnswer).getPhone());
                contactAnswerJson.put("surveyId", ((AnswerContact) subAnswer).getSubAnswerID());
                listContactAnswer.add(contactAnswerJson);
            }


        });


        answerTotalJson.put("answerGroupLevelForms", listLevelAnswer);
        answerTotalJson.put("answerContactForms", listContactAnswer);
        answerTotalJson.put("answerMultiChoiceForms", listMultipleAnswer);
        answerTotalJson.put("answerOpenForms", listOpenAnswer);
        answerTotalJson.put("answerSingleChoiceForms", listSingleAnswer);


        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URLApi.UPLOAD_ANSWER);

        try{
            request.addHeader("content-type", "application/json");
            StringEntity answerEntity = new StringEntity(answerTotalJson.toJSONString(), "UTF-8");
            request.setEntity(answerEntity);
            HttpResponse response = httpClient.execute(request);//executing the request
            return response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 400;
    }
}
