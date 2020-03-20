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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CompanyController implements Initializable {
    @FXML private ImageView logoImg;
    @FXML private Label name;

    private User getUserData(){
        User user = new User();
        try{
            Path path = Paths.get("Data", "User");
            File file = new File(path.toAbsolutePath().toString() + "\\UserData.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String userData = bufferedReader.readLine();
            JSONObject userJson = (JSONObject) new JSONParser().parse(userData);
            String password = (String) userJson.get("password");
            String username = (String) userJson.get("username");
            user.setPassword(password);
            user.setUsername(username);
        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy thông tin tài khoản");
        } catch (IOException e) {
            System.out.println("không tìm thấy thông tin tài khoản");
        } catch (ParseException e) {
            System.out.println("không tìm thấy thông tin tài khoản");
        }
        return user;
    }
    private Company syncDataAndReturnCompanyData(){
        User user = getUserData();
        CompanyService companyService = new CompanyService();
        Company company = companyService.getCompanyInfor(user);
        Path companyDataPath = Paths.get("Data", "Company\\Logo");
        String urlLogo = URLApi.HOST + "/" + company.getImgLogo();
        try{
            Files.createDirectories(companyDataPath);
            FileMethod.saveFileFromURL(companyDataPath.toAbsolutePath().toString() + "\\" + company.getImgLogo(), urlLogo);
        } catch (MalformedURLException e) {
            System.out.println("Lỗi tại CompanyController");
        } catch (IOException e) {
            System.out.println("Lỗi tại company controller");
        }
        return company;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Company company = syncDataAndReturnCompanyData();
        //setting logo and name of company
        name.setText(company.getNameOfCompany());
        try {
            Path companyLogo = Paths.get("Data", "Company\\Logo");
            Image image = new Image(new FileInputStream(companyLogo.toAbsolutePath().toString() + "\\" + company.getImgLogo()));
            logoImg.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
