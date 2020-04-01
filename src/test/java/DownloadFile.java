import Config.URLApi;
import Models.Survey.Survey;
import Models.User.User;
import Service.impl.*;
import Utils.FileMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadFile {
    private static boolean checkExistData(String fileDir){
        return new File(fileDir).listFiles()!=null;
    }
    private static Survey getSurveyData(){
        Path path = Paths.get("Data", "Survey");
        try{
            Files.createDirectories(path);
            File file = new File(path.toAbsolutePath().toString() + "\\SurveyData.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Survey survey = (Survey) ois.readObject();

            ois.close();
            return survey;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        RewardService rewardService = new RewardService();
        rewardService.getReward(1582);


    }
}
