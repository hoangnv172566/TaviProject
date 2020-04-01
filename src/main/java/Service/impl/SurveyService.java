package Service.impl;

import Config.URLApi;
import Models.Survey.Choice.*;
import Models.Survey.ContactField;
import Models.Survey.Question;
import Models.Survey.Survey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
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
import java.util.ArrayList;

public class SurveyService {

    public JSONObject getdata(long idSurvey) throws IOException, ParseException {
            byte[] responseByte =  Request.Get("http://103.9.86.61:8080/admin_srs/api/v1/public/survey-display/find-by-survey-total?id=" + idSurvey)
                    .execute().returnContent().asBytes();
            String response = new String(responseByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(response);
            return (JSONObject) result.get("data");
    }

    public long getOrderQuestion(long surveyTotalID, long questionID, boolean enable){
        try{
            byte[] responeByte= Request.Get("http://103.9.86.61:8080/admin_srs/api/v1/public/survey-total-has-survey/search?survey-total-id=" + String.valueOf(surveyTotalID) + "&survey-id="+ String.valueOf(questionID) +"&enable=" + String.valueOf(enable)).execute().returnContent().asBytes();
            String response =new String(responeByte, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();

            JSONObject responseJson = (JSONObject) jsonParser.parse(response);
            ArrayList<JSONObject> data = (ArrayList<JSONObject>) responseJson.get("data");
            JSONObject inforJSON = (JSONObject) data.get(0);
            long ord = (long) inforJSON.get("ord");

            return ord;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String format(String data){
        if(data.equals("1")){
            return "CSAT";
        }
        if(data.equals("2")){
            return "NPS";
        }
        if(data.equals("3")){
            return "CES";
        }
        if(data.equals("4")){
            return "FLX";
        }
        if(data.equals("5")){
            return "MULTIPLE_CHOICE";
        }
        if(data.equals("6")){
            return "SINGLE_CHOICE";
        }
        if(data.equals("7")){
            return "STAR";
        }
        if(data.equals("8")){
            return "OPEN";
        }
        if(data.equals("9")){
            return "CONTACT";
        }
        return null;
    }

    public Survey getSurvey(long idSurvey){
        try{
            JSONObject data = getdata(idSurvey);

            String name = (String) ((JSONObject) data.get("surveyTotal")).get("name") ;
            System.out.println(name);

            ArrayList<JSONObject> surveyRecordList = (ArrayList<JSONObject>) data.get("surveyRecordList");
            Survey survey = new Survey();

            survey.setIdSurvey(idSurvey);
            survey.setContentSurvey(name);

            Question question;
            ArrayList<Question> listQuestions = new ArrayList<>();

            for(JSONObject ques:surveyRecordList){

                question = new Question();
                JSONObject questionJSon = (JSONObject) ques.get("survey");

                String viContent = (String) questionJSon.get("viQuestion");
                question.setViContent(viContent);

                String enContent = (String) questionJSon.get("enQuestion");
                question.setEnContent(enContent);

                long questionID = (long) questionJSon.get("id");
                question.setQuestionID(questionID);

                boolean enable = (boolean) questionJSon.get("enable");
                question.setEnable(enable);

                long ord = getOrderQuestion(survey.getIdSurvey(), question.getQuestionID(), enable);
                question.setOrd(ord);

                String typeQuestion = format(String.valueOf(questionJSon.get("type")));
                question.setType(typeQuestion);

                boolean require = (boolean) ques.get("require");
                question.setRequire(require);

                //adding choice depends on type of question
                ArrayList<JSONObject> sampleAnswerArr = (ArrayList<JSONObject>) ques.get("sampleAnswers");

                ArrayList<Choice> listChoice = new ArrayList<>();;
                if (sampleAnswerArr != null) {
                    for(JSONObject sampleAnswer :sampleAnswerArr){
                        if(question.getType().equals("MULTIPLE_CHOICE")) {

                            MutipleChoice choice = new MutipleChoice();

                            String enMultiContent = (String) sampleAnswer.get("enTitle");
                            choice.setEnContentChoice(enMultiContent);

                            String viMultiContent = (String) sampleAnswer.get("viTitle");
                            choice.setViContentChoice(viMultiContent);

                            long sampleAnswerID = (long) sampleAnswer.get("id");
                            choice.setSampleAnswerID(sampleAnswerID);


                            long ordSampleAnswer = (long) sampleAnswer.get("ord");
                            choice.setOrd(ordSampleAnswer);

                            listChoice.add(choice);
                        } else if(question.getType().equals("SINGLE_CHOICE")){
                            SingleChoice choice = new SingleChoice();

                            String enCESContent = (String) sampleAnswer.get("enTitle");
                            choice.setEnContentChoice(enCESContent);

                            String viCESContent = (String) sampleAnswer.get("viTitle");
                            choice.setViContentChoice(viCESContent);

                            long sampleAnswerID = (long) sampleAnswer.get("id");
                            choice.setSampleAnswerID(sampleAnswerID);


                            long ordSampleAnswer = (long) sampleAnswer.get("ord");
                            choice.setOrd(ordSampleAnswer);


                            listChoice.add(choice);
                        }

                    }
                }
                question.setChoice(listChoice);

                if(question.getType().equals("FLX")){
                    long i = (long) questionJSon.get("maxLevel");
                    question.setMaxLevel(i);
                }
                listQuestions.add(question);
                if(question.getType().equals("CONTACT")){
                    JSONObject contactFieldJs = (JSONObject) questionJSon.get("contactField");
                    if(contactFieldJs!=null){
                        ContactField contactField = new ContactField();
                        boolean nameRequire = (boolean) contactFieldJs.get("nameRequire");

                        boolean phoneRequire = (boolean) contactFieldJs.get("phoneRequire");

                        boolean emailRequire = (boolean) contactFieldJs.get("emailRequire");

                        boolean addressRequire = (boolean) contactFieldJs.get("addressRequire");

                        contactField.setNameRequire(nameRequire);
                        contactField.setEmailRequire(emailRequire);
                        contactField.setAddressRequire(addressRequire);
                        contactField.setPhoneRequire(phoneRequire);

                        question.setContactField(contactField);
                    }
                }
            }

            survey.setListQuestion(listQuestions);

            Path path = Paths.get("Data", "Survey");
            Files.createDirectories(path);

            File surveyFile = new File(path.toAbsolutePath().toString() + "\\SurveyData.json");
            if(!surveyFile.exists()){
                surveyFile.createNewFile();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(surveyFile, survey);

            return survey;
        }catch (IOException | ParseException er){
            return null;
        }

    }


}
