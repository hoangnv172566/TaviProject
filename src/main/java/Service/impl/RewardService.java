package Service.impl;

import Models.Reward.Reward;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RewardService {
    public int getReward(long idAnswerTotal){
        try{
            byte[] responseByte = Request.Get("http://103.9.86.61:8080/admin_srs/api/v1/public/reward-code/find-by-answer-total?answer-total-id=" + idAnswerTotal).execute().returnContent().asBytes();
            String response = new String(responseByte, StandardCharsets.UTF_8);
            JSONObject responseJs = (JSONObject) new JSONParser().parse(response);
            JSONArray data1 = (JSONArray) responseJs.get("data");
            JSONObject data = (JSONObject) data1.get(0);

            String note = (String) data.get("note");
            String date = (String) data.get("endDate");
            String code = (String) data.get("code");

            Reward reward = new Reward();
            reward.setIdAnswerTotal(idAnswerTotal);
            reward.setCode(code);
            reward.setContent(note);
            reward.setExpiredDate(date);

            Path path = Paths.get("Data", "Reward");
            Files.createDirectories(path);
            File rewardFile = new File(path.toAbsolutePath().toString() + "\\RewardData.json");
            if(rewardFile.exists()){
                rewardFile.createNewFile();
            }

            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(rewardFile, reward);
            return 200;

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return 400;
        }

        return 500;
    }

}
