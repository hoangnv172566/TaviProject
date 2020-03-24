package Service.impl;

import Config.URLApi;
import Models.Company.Company;
import Models.User.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompanyService {
    private UserService userService;

    public int getCompanyInfor(long idCompany) {
        Company company = new Company();
        try{
            Response response = Request.Get(URLApi.COMPANY + idCompany).execute();
            byte[] responseBye = response.returnContent().asBytes();
            String responseString = new String(responseBye, StandardCharsets.UTF_8);
            JSONParser parser = new JSONParser();
            JSONObject responseJs = (JSONObject) parser.parse(responseString);
            JSONObject data = (JSONObject) responseJs.get("data");

            String imgLogo = (String) data.get("imgLogo");
            String name = (String) data.get("name");

            company.setCompanyID(idCompany);
            company.setNameOfCompany(name);
            company.setImgLogo(imgLogo);


            Path path = Paths.get("Data", "Company\\Info");
            Files.createDirectories(path);
            File companyFile = new File(path.toAbsolutePath().toString() + "\\CompanyData.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(companyFile, company);

            return 200;
        }catch (ParseException er){
            er.getMessage();
        }catch (IOException er){
            er.getMessage();
            return 400;
        }

        return 500;
    }

}
