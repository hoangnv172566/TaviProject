package View.Design.Client.Thanks;

import Models.Reward.Reward;
import Models.Thanks.Thanks;
import Service.impl.ThankService;
import View.Design.Client.Questions.CQuestionsController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CThanksController implements Initializable {
    @FXML private Label thankContent;
    @FXML private Button back;
    @FXML private Label nameReward;
    @FXML private Label codeReward;
    @FXML private Label expiredDate;
    @FXML private VBox rewardInfo;

    public static Reward getReward() {
        try{
            Path path = Paths.get("Data", "Reward");
            File rewardFile = new File(path.toAbsolutePath().toString() + "\\RewardData.json");
            return new ObjectMapper().readValue(rewardFile, Reward.class);
        }catch (IOException er){
            return null;
        }
    }

    public static Parent getParent(){
        try{
            return FXMLLoader.load(CThanksController.class.getResource("C-Thanks.fxml"));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void setScene(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private Thanks getThankData() throws IOException {
        Path path = Paths.get("Data","Thanks");
        File file = new File(path.toAbsolutePath().toString() + "\\ThankData.json");
        return new ObjectMapper().readValue(file, Thanks.class);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Thanks thanks = getThankData();
            thankContent.setText(thanks.getContent());
        } catch (IOException e) {
            thankContent.setText("Thank You");
        }

        Reward reward = getReward();
        if(reward!=null){
            nameReward.setText(reward.getContent());
            codeReward.setText(reward.getCode());
            expiredDate.setText(reward.getExpiredDate());
            rewardInfo.setVisible(true);
        }else{
            rewardInfo.setVisible(false);
        }

        back.setOnAction(e->{
            setScene(CQuestionsController.getParent(), e);
        });
    }


}
