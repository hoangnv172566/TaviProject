import Config.URLApi;
import Models.Survey.Survey;
import Models.User.User;
import Service.impl.CollectionService;
import Service.impl.CompanyService;
import Service.impl.SurveyService;
import Service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadFile {
    private static boolean checkExistData(String fileDir){
        return new File(fileDir).listFiles()!=null;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SurveyService surveyService = new SurveyService();
        surveyService.getSurvey(1);
    }
}
