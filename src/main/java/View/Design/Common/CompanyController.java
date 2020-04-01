package View.Design.Common;

import Config.URLApi;
import Models.Company.Company;
import Models.User.User;
import Service.impl.CompanyService;
import Utils.FileMethod;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CompanyController implements Initializable {
    @FXML private ImageView logoImg;
    @FXML private Label name;


    private void syncLogo() throws IOException {
        //get CompanyId
        Path path = Paths.get("Data", "User");
        File file = new File(path.toAbsolutePath().toString() + "\\UserData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        User userData = objectMapper.readValue(file, User.class);
        //get company data from api
        CompanyService companyService = new CompanyService();
        int stt = companyService.getCompanyInfor(userData.getCompanyID());
        if(stt == 200){
            Path companyLogoPath = Paths.get("Data", "Company\\Logo");
            Path companyDataDir = Paths.get("Data", "Company\\Info");
            File companyFile = new File(companyDataDir.toAbsolutePath().toString() + "\\CompanyData.json");

            Company company = objectMapper.readValue(companyFile, Company.class);
            //mapping Data to label
            String urlLogo = URLApi.HOST + "/" + company.getImgLogo();
            try{
                Files.createDirectories(companyLogoPath);
                FileMethod fileMethod = new FileMethod() {
                    @Override
                    public void showInfoProcess() {

                    }
                };
                fileMethod.saveFileFromURL(companyLogoPath.toAbsolutePath().toString() + "\\" + company.getImgLogo(), urlLogo);

            } catch (MalformedURLException e) {
                System.out.println("Lỗi tại CompanyController");
            } catch (IOException e) {
                System.out.println("Lỗi tại company controller");
            }
        }
    }

    private boolean checkExistData(String fileDir){
        File file = new File(fileDir);
        if(file.exists()){
            return new File(fileDir).listFiles()!=null;
        }else{
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Path path = Paths.get("Data", "Company\\Logo");
            if(!checkExistData(path.toAbsolutePath().toString())){
                syncLogo();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Path companyDataDir = Paths.get("Data", "Company\\Info");
        File companyFile = new File(companyDataDir.toAbsolutePath().toString() + "\\CompanyData.json");

        try {
            Company company = new ObjectMapper().readValue(companyFile, Company.class);
            Path companyLogo = Paths.get("Data", "Company\\Logo");
            Image image = new Image(new FileInputStream(companyLogo.toAbsolutePath().toString() + "\\" + company.getImgLogo()));
            logoImg.setImage(image);
            name.setText(company.getNameOfCompany());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
