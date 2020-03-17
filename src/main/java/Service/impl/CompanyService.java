package Service.impl;

import Config.URLApi;
import Models.Company.Company;
import Models.User.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CompanyService {
    private UserService userService;

    public Company getCompanyInfor(User user){
        Company company;
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        HttpPost request = new HttpPost(URLApi.LOGIN);//connect to api
        JSONObject jsonObject = new JSONObject();// create jsonOb to pass as a para of StringEntity

        jsonObject.put("password", user.getPassword());
        jsonObject.put("username", user.getUsername());

        try{
            StringEntity params =new StringEntity(jsonObject.toString());

            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);//executing the request

            StringEntity data = new StringEntity(EntityUtils.toString(response.getEntity()));

            JSONParser jsonParser = new JSONParser();
            JSONObject dataJson = (JSONObject) jsonParser.parse(EntityUtils.toString(data));
            String rawData = dataJson.toString();

            byte[] dataAsByte = rawData.getBytes();
            String newDataAfterChanging = new String(dataAsByte, StandardCharsets.UTF_8);
            JSONObject newDataAsJsonObject = (JSONObject) jsonParser.parse(newDataAfterChanging);
            JSONObject infor = (JSONObject) newDataAsJsonObject.get("data");
            JSONObject companyInfor = (JSONObject) infor.get("company");
            String name = (String) companyInfor.get("name");
            long idCompany = (long) companyInfor.get("id");
            String imgLogo = (String) companyInfor.get("imgLogo");

            //mapping data to Company object
            company = new Company();
            company.setNameOfCompany(name);
            company.setCompanyID(idCompany);
            company.setImgLogo(imgLogo.trim());

            return company;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

}
