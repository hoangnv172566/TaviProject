package View.Design.Common;

import Config.URLApi;
import Models.Company.Company;
import Models.User.User;
import Service.impl.CompanyService;
import Utils.FileMethod;
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
import java.util.ResourceBundle;

public class CompanyController implements Initializable {
    @FXML private ImageView logoImg;
    @FXML private Label name;

    private User getUserData(){
        User user = new User();
        try{
            File file = new File("src/main/java/Data/Answer/UserData.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String userData = bufferedReader.readLine();
            JSONObject userJson = (JSONObject) new JSONParser().parse(userData);
            String password = (String) userJson.get("password");
            String username = (String) userJson.get("username");
            user.setPassword(password);
            user.setUsername(username);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }
    private void syncData(){
        User user = getUserData();
        CompanyService companyService = new CompanyService();
        Company company = companyService.getCompanyInfor(user);
        String logoDirectory = "src/main/java/Data/Company/Logo";
        String urlLogo = URLApi.HOST + "/" + company.getImgLogo();
        try{
            FileMethod.createDirectory(logoDirectory);
            FileMethod.saveFileFromURL(logoDirectory + "/" + company.getImgLogo(), urlLogo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = getUserData();
        syncData();
        CompanyService companyService = new CompanyService();
        Company company = companyService.getCompanyInfor(user);
        //setting logo and name of company
        name.setText(company.getNameOfCompany());
        try {
            Image image = new Image(new FileInputStream("src/main/java/Data/Company/Logo/hignland.png"));
            logoImg.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
