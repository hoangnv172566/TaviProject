package View.Design.Client.Questions;

import Models.Answer.AnswerContact.AnswerContact;
import Models.Answer.AnswerLevelGroup.AnswerLevel;
import Models.Answer.AnswerMultipleChoice.AnswerChoice;
import Models.Answer.AnswerMultipleChoice.AnswerMultipleChoice;
import Models.Answer.AnswerOpen.AnswerOpen;
import Models.Answer.AnswerSingleChoice.AnswerSingleChoice;
import Models.Answer.AnswerTotal;
import Models.Answer.SubAnswer;
import Models.Setting.WaitingScene;
import Models.Survey.Choice.Choice;
import Models.Survey.Choice.MutipleChoice;
import Models.Survey.Choice.SingleChoice;
import Models.Survey.ContactField;
import Models.Survey.Question;
import Models.Survey.Survey;
import Models.Temp.CheckRequireQuestion;
import Models.User.User;
import Service.impl.AnswerService;
import Service.impl.RewardService;
import Utils.FileMethod;
import View.Design.Client.Questions.CES.CQuestionsCESController;
import View.Design.Client.Questions.CSAT.CQuestionCSATChoiceController;
import View.Design.Client.Questions.CSAT.CQuestionsCSATController;
import View.Design.Client.Questions.Contact.CQuestionContactController;
import View.Design.Client.Questions.FLX.CQuestionFLXController;
import View.Design.Client.Questions.Index.QIndexController;
import View.Design.Client.Questions.MutipleChoice.CQuestionMutipleChoiceChoiceController;
import View.Design.Client.Questions.NPS.CQuestionsNPSChoiceController;
import View.Design.Client.Questions.Open.CQuestionOpenController;
import View.Design.Client.Questions.SingleChoice.CQuestionSingleChoiceChoiceController;
import View.Design.Client.Questions.Star.CQuestionStarController;
import View.Design.Client.Thanks.CThanksController;
import View.Design.Common.VideoController;
import View.Design.Common.WaitingSceneSlideController;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CQuestionsController implements Initializable {

    //inject element
    @FXML private AnchorPane bigBackground;
    @FXML private VBox listQuestions;
    @FXML private Button back;
    @FXML private Label contentSurvey;
    @FXML private Button sendingSurvey;
    @FXML private AnchorPane parentListAnswer;
    @FXML private ScrollPane scrollParent;
    @FXML private JFXToggleButton languageSwitch;
    @FXML private Button undo;
    @FXML private HBox listFinishedAnswer;

    private WaitingScene waitingScene;

    //configure layout
    public static Parent getParent() {
        try{
           return FXMLLoader.load(CQuestionsController.class.getResource("C-Questions.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setScene(Parent parent, ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    //create layout for each type of question
    private void setFullQuestionLayout(){
        Screen screen = Screen.getPrimary();
        Rectangle2D rectangle2D = screen.getVisualBounds();
        parentListAnswer.setPrefWidth(rectangle2D.getWidth()-200.0);
    }

    private AnchorPane createMutipleChoiceLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();
        //setContentQuestion
        HBox parentContentQuestion = (HBox) questionLayout.getChildren().get(0);
        Label content = (Label) parentContentQuestion.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Bắt buộc!");
                require.setVisible(true);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Required!");
                require.setVisible(true);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);
        for(int i = 0; i<question.getChoice().size(); i++){
            AnchorPane questionChoice = (AnchorPane) CQuestionMutipleChoiceChoiceController.getParent();
            CheckBox choice = (CheckBox) questionChoice.getChildren().get(0);
            MutipleChoice multipleChoice = (MutipleChoice) question.getChoice().get(i);

            choice.setId(String.valueOf(multipleChoice.getOrd()));

            if(vi == true){
                choice.setText(multipleChoice.getViContentChoice());
            }else{
                choice.setText(multipleChoice.getEnContentChoice());
            }

            listChoice.getChildren().add(choice);

        }
        listChoice.setSpacing(30.0);





        return questionLayout;

    }

    private AnchorPane createSingleChoiceLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();

        //setContentQuestion
        HBox parentContentQuestion = (HBox) questionLayout.getChildren().get(0);
        Label content = (Label) parentContentQuestion.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Bắt buộc!");
                require.setVisible(true);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Required!");
                require.setVisible(true);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);
        ToggleGroup toggleGroup = new ToggleGroup();
        for(int i = 0; i<question.getChoice().size(); i++){
            AnchorPane questionChoice = (AnchorPane) CQuestionSingleChoiceChoiceController.getParent();
            RadioButton choice = (RadioButton) questionChoice.getChildren().get(0);

            SingleChoice singleChoice = (SingleChoice) question.getChoice().get(i);
            if(vi == true){
                choice.setText(singleChoice.getViContentChoice());
            }else{
                choice.setText(singleChoice.getEnContentChoice());
            }

            choice.setToggleGroup(toggleGroup);
            listChoice.getChildren().add(choice);
        }

        //writeData
        listChoice.setSpacing(30.0);
        return questionLayout;
    }

    private AnchorPane createCSATLayout(Question question, boolean vi){

        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();

        //setContentQuestion
        assert questionLayout != null;
        HBox parentContentQuestion = (HBox) questionLayout.getChildren().get(0);
        Label content = (Label) parentContentQuestion.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Bắt buộc!");
                require.setVisible(true);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                Label require = (Label) parentContentQuestion.getChildren().get(1);
                require.setText("(*) Required!");
                require.setVisible(true);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);

        for(int i = 0; i<5; i++){

            AnchorPane questionChoice = (AnchorPane) CQuestionCSATChoiceController.getParent();
            ImageView imageChoice = (ImageView) questionChoice.getChildren().get(0);

            try{
                String fileName = "src/main/resources/FixedSetting/Icon/CSAT/icon_"+(i+1)+ ".png";
                File file = new File(fileName);
                Image image = new Image(new FileInputStream(file));
                imageChoice.setImage(image);
            } catch (FileNotFoundException e) {
                Path path = Paths.get("FixedSetting", "Icon\\CSAT");
                String fileName = "\\icon_"+ (i+1) + ".png";
                try{
                    Image image = new Image(new FileInputStream(new File(path.toAbsolutePath().toString() + fileName)));
                    imageChoice.setImage(image);
                } catch (FileNotFoundException ex) {
                    Alert fileNotFoundAlert = new Alert(Alert.AlertType.ERROR);
                    fileNotFoundAlert.setContentText("Icon không tồn tại. Đồng bộ đúng tên icon_i");
                    fileNotFoundAlert.show();
                }
            }

            Label labelContent = (Label) questionChoice.getChildren().get(1);
            if(vi){
                if(i== 0){
                    labelContent.setText("Rất tệ");
                }else if(i==1){
                    labelContent.setText("Tệ");
                }else if(i==2){
                    labelContent.setText("Bình thường");
                }else if(i==3){
                    labelContent.setText("Hài lòng");
                }else if(i==4){
                    labelContent.setText("Rất Hài lòng");
                }
            }else{
                if(i== 0){
                    labelContent.setText("Extremely bad");
                }else if(i==1){
                    labelContent.setText("Not good");
                }else if(i==2){
                    labelContent.setText("Can be accepted");
                }else if(i==3){
                    labelContent.setText("Satisfy");
                }else if(i==4){
                    labelContent.setText("Very good");
                }
            }

            questionChoice.setId(String.valueOf((i+1)));
            questionChoice.setCursor(Cursor.HAND);
            listChoice.getChildren().add(questionChoice);
        }

        ObservableList<Node> listChildren = listChoice.getChildren();
        for(int i = 0; i<5; i++){

            int level = i+1;
            listChildren.get(i).setOnMouseClicked(e->{
                //set Effect
                ((AnchorPane)listChildren.get(level-1)).setStyle("-fx-background-color: #cecece");
                Label contentChoiceLb = (Label) ((AnchorPane)listChildren.get(level-1)).getChildren().get(1);
                String textStyle = "-fx-text-fill: green";
                contentChoiceLb.setStyle("-fx-text-fill: green");

                for(int sibling = 0; sibling < 5; sibling++){
                    if(sibling!=level-1){
                        ((AnchorPane)listChildren.get(sibling)).setStyle(null);
                        Label contentChoiceLbSiblings = (Label) ((AnchorPane)listChildren.get(sibling)).getChildren().get(1);
                        contentChoiceLbSiblings.setStyle("-fx-text-fill: black");
                    }
                }


                //write data
                String fileName = question.getType()+ "_" + question.getOrd();
                Path path = Paths.get("Data", "SubAnswer");
                try {
                    Files.createDirectories(path);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                File csatData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                if(!csatData.exists()){
                    try {
                        csatData.createNewFile();
                    } catch (IOException ex) {
                        System.out.println("can't open file");
                    }
                }
                AnswerLevel levelAnswer = new AnswerLevel();
                levelAnswer.setLevel(level);
                levelAnswer.setOrd(question.getOrd());
                levelAnswer.setSubAnswerID(question.getQuestionID());

                ObjectMapper mapper = new ObjectMapper();
                try
                {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, levelAnswer);
                } catch (JsonGenerationException ex) {
                    ex.printStackTrace();
                } catch (JsonMappingException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });

        }

        listChoice.setSpacing(15.0);

        return questionLayout;
    }

    private AnchorPane createNPSLayout(Question question, boolean vi) {
        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();
        //setContentQuestion
        assert questionLayout != null;
        HBox parentContentQuestion = (HBox) questionLayout.getChildren().get(0);
        Label content = (Label) parentContentQuestion.getChildren().get(0);
        Label require = (Label) parentContentQuestion.getChildren().get(1);

        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("(*) Bắt buộc!");
                require.setVisible(true);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("(*) Required!");
                require.setVisible(true);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);
        for(int i = 0; i<5; i++){
            AnchorPane questionChoice = (AnchorPane) CQuestionsNPSChoiceController.getParent();
            assert questionChoice != null;
            Label labelContent = (Label) questionChoice.getChildren().get(0);
            labelContent.setText(String.valueOf(i+1));
            listChoice.getChildren().add(questionChoice);
        }

        listChoice.setSpacing(15.0);
        return questionLayout;

    }

    private AnchorPane createFLXLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionFLXController.getParent();


        //setContentQuestion
        HBox contentHb = (HBox) questionLayout.getChildren().get(0);
        Label content = (Label) contentHb.getChildren().get(0);
        Label require = (Label) contentHb.getChildren().get(1);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("Bắt buộc");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("Required");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);
        String normalStyle = "-fx-background-color: white;\n"
                + "-fx-border-color: yellow;\n";
        for(int i = 0; i< question.getMaxLevel(); i++){
            Label level = new Label();
            level.setText(String.valueOf(i+1));
            level.setFont(new Font(30));
            level.setPrefWidth(60.0);
            level.setPrefHeight(60.0);
            level.setAlignment(Pos.CENTER);
            level.setCursor(Cursor.HAND);
            level.setStyle(normalStyle);
            listChoice.getChildren().add(level);
        }


        listChoice.setSpacing(15.0);

        return questionLayout;
    }

    private AnchorPane createCESLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCESController.getParent();

        HBox contentHb = (HBox) questionLayout.getChildren().get(0);
        //setContentQuestion
        Label content = (Label) contentHb.getChildren().get(0);
        Label require = (Label) contentHb.getChildren().get(1);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("Bắt buộc");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("(*)Required");
                require.setVisible(true);
            }else{
                require.setVisible(true);
            }
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);

        String normalStyle = "-fx-background-color: white;\n"
                + "-fx-border-color: yellow;\n";

        for(int i = 0; i<7; i++){
            Label level = new Label();
            level.setPrefHeight(50.0);
            level.setPrefWidth(50.0);
            level.setAlignment(Pos.CENTER);
            level.setFont(new Font(18.0));
            level.setCursor(Cursor.HAND);
            level.setText(String.valueOf(i+1));
            level.setId(String.valueOf(i));
            level.setCursor(Cursor.HAND);
            level.setStyle(normalStyle);
            listChoice.getChildren().add(level);
        }





        return questionLayout;
    }

    private ImageView configureStar(String pathOfImageIcon) throws FileNotFoundException {
        Image emptyStarImage = new Image(new FileInputStream(new File(pathOfImageIcon)));
        ImageView starLevel = new ImageView(emptyStarImage);
        starLevel.setFitWidth(100.0);
        starLevel.setFitHeight(100.0);
        starLevel.setCursor(Cursor.HAND);
        return starLevel;
    }

    private AnchorPane createStarLayout(Question question, boolean vi){
        AnchorPane starAnchorPane = (AnchorPane) CQuestionStarController.getParent();
        HBox contentHb = (HBox) starAnchorPane.getChildren().get(0);
        HBox listChoice = (HBox) starAnchorPane.getChildren().get(1);

        Label content = (Label) contentHb.getChildren().get(0);
        Label require = (Label) contentHb.getChildren().get(1);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("Bắt buộc");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("Required");
                require.setVisible(true);
            }else{
                require.setVisible(true);
            }
        }

        //set Choice
        String emptyStarRootPath = "src/main/resources/FixedSetting/Icon/Star/whiteStar.png";
        String filledStarRootPath = "src/main/resources/FixedSetting/Icon/Star/filledStar.png";

        for(int i = 0; i<5; i++){
           ImageView imageView;
           try{
               imageView = configureStar(emptyStarRootPath);
               listChoice.getChildren().add(imageView);
           } catch (FileNotFoundException e) {
               Path emptyStarPath = Paths.get("FixedSetting", "Icon\\Star");
               try{
                   imageView = configureStar(emptyStarPath.toAbsolutePath().toString() + "\\whiteStar.png");
                   listChoice.getChildren().add(imageView);
               } catch (FileNotFoundException ex) {
                   Alert.AlertType alertAlertType = AlertType.ERROR;
                   Alert alert = new Alert(alertAlertType);
                   alert.setContentText("File Star không tồn tại");
                   alert.showAndWait();
               }
           }

       }

        return starAnchorPane;
    }

    private AnchorPane createOpenLayout(Question question, boolean vi){
        AnchorPane openLayout = (AnchorPane) CQuestionOpenController.getParent();

        HBox contentHb = (HBox) openLayout.getChildren().get(0);

        Label content = (Label) contentHb.getChildren().get(0);
        Label require = (Label) contentHb.getChildren().get(1);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("Bắt buộc");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("Required");
                require.setVisible(true);
            }else{
                require.setVisible(true);
            }
        }
        return openLayout;
    }

    private AnchorPane createContactLayout(Question question, boolean vi){
        AnchorPane contactLayout = (AnchorPane) CQuestionContactController.getParent();

        HBox contentHb = (HBox) contactLayout.getChildren().get(0);
        Label content = (Label) contentHb.getChildren().get(0);
        Label require = (Label) contentHb.getChildren().get(1);
        if (vi){
            content.setText(question.getViContent());
            if(question.isRequire()){
                require.setText("Bắt buộc");
                require.setVisible(true);
            }else{
                require.setVisible(false);
            }
        }else{
            content.setText(question.getEnContent());
            if(question.isRequire()){
                require.setText("Required");
                require.setVisible(true);
            }else{
                require.setVisible(true);
            }
        }



        return contactLayout;
    }

    //check data
    private boolean checkValidateEmail(String text) {
        String format = "^[a-z][a-z0-9_.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    private boolean checkValidatePhone(String text){
        String format = "\\d{4}\\d{3}\\d{3}";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    private boolean checkAmountAnswerData(String fileDirectory, AnswerTotal data){
        return false;
    }

    private WaitingScene getWaitingScene(){
        Alert alert;
        Path path = Paths.get("Setting", "WaitingScene");
        File videoFile = new File(path.toAbsolutePath().toString() + "\\WaitingScene.json");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(videoFile, WaitingScene.class);
        } catch (JsonParseException | JsonMappingException e) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Không mapping được dữ liệu waitingScene");
            alert.showAndWait();
        } catch (IOException e) {
            Path pathDefault = Paths.get("FixedSetting","DefaultWaitingScene");
            File defaultVideoFile = new File(pathDefault.toAbsolutePath().toString() + "\\WaitingScene.json");
            try{
                return new ObjectMapper().readValue(defaultVideoFile, WaitingScene.class);
            } catch (JsonParseException | JsonMappingException er) {
                System.out.println("Không thể mapping dữ liệu tại videoController");
            } catch (IOException er) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Không có cài đặt mặc định cho màn hình chờ!");
                alert.showAndWait();
            }
            return null;
        }
        return null;
    }

    //userInfo
    private User getUserData(){
        try{
            Path userPath = Paths.get("Data",  "User");
            File userFile = new File(userPath.toAbsolutePath().toString() + "\\UserData.json");
            ObjectMapper userObM = new ObjectMapper();
            User userData = userObM.readValue(userFile, User.class);
            return userData;
        } catch (JsonParseException | JsonMappingException e) {
            System.out.println("cann't mapping Data");
        } catch (IOException e) {
            System.out.println("cannot read userFile");
        }
        return null;
    }

    private Survey getSurveyData(){
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

    //generating finished Question
    private String normalStyle = "    -fx-background-color: white;\n" +
            "    -fx-border-radius: 4px;\n" +
            "    -fx-border-color: #12e200;\n" +
            "    -fx-text-fill: #4b4b4b;";

    private String chosenStyle = "-fx-background-color: #12e200;\n" +
            "    -fx-background-radius: 4px;\n" +
            "    -fx-border-radius: 4px;";

    private void generate(long numOfQuestions){
        for(int i = 0; i < numOfQuestions; i++){
            Label label = new Label();
            label.setText(String.valueOf(i+1));
            label.setFont(new Font(18.0));
            label.setPrefHeight(30.0);
            label.setPrefWidth(30.0);
            label.setAlignment(Pos.CENTER);
            label.setStyle(normalStyle);
            listFinishedAnswer.getChildren().add(label);

        }
    }

    private void displayData(boolean vi){
        Survey survey = getSurveyData();
        assert survey != null;
        contentSurvey.setText(survey.getContentSurvey());
        ArrayList<Question> listQuestionOfSurvey = survey.getListQuestion();
        for(int index = 0; index<listQuestionOfSurvey.size(); index++){
            AnchorPane questionAnchorPane = new AnchorPane();
            Question question = listQuestionOfSurvey.get(index);

            if(question.isRequire()){
                Path tempDataPath = Paths.get("Temp", "CheckData");
                try{
                    Files.createDirectories(tempDataPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File tempDataFile = new File(tempDataPath.toAbsolutePath().toString() + "\\" + question.getType() + "_" + question.getOrd() + ".json");
                if(!tempDataFile.exists()){
                    try{
                        tempDataFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                CheckRequireQuestion checkRequireQuestion = new CheckRequireQuestion();
                checkRequireQuestion.setOrderInList(index);
                checkRequireQuestion.setRequired(question.isRequire());
                checkRequireQuestion.setTypeQuestion(question.getType());
                try{
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(tempDataFile, checkRequireQuestion);
                } catch (IOException e) {

                }
            }

            if (question.getType().equals("CSAT")) {
                questionAnchorPane = createCSATLayout(question, vi);
                HBox parentContent = (HBox) questionAnchorPane.getChildren().get(0);
                Label announcement = (Label) parentContent.getChildren().get(1);
                HBox listChoicePr = (HBox) questionAnchorPane.getChildren().get(1);
                HBox surveyNumPr = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumPr.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số "+ (index+1));
                }else{
                    surveyNum.setText("Survey No." + (index+1));
                }

                //set eff for each choice node
                ObservableList<Node> listChoice = listChoicePr.getChildren();
                for(int choiceIndex = 0; choiceIndex < listChoice.size(); choiceIndex++){
                    Node choiceAnchorPane = listChoice.get(choiceIndex);
                    int finalIndex = choiceIndex;
                    int finalBigIndex = index;
                    choiceAnchorPane.setOnMouseClicked(event -> {
                        //set Effect
                        listChoice.get(finalIndex).setStyle("-fx-background-color: #cecece");
                        Label contentChoiceLb = (Label) ((AnchorPane)listChoice.get(finalIndex)).getChildren().get(1);
                        String textStyle = "-fx-text-fill: green; \n"
                                +"-fx-font-weight: bold;\n";
                        contentChoiceLb.setStyle(textStyle);

                        for(int sibling = 0; sibling < listChoice.size(); sibling++){
                            if(sibling!=finalIndex){
                                ((AnchorPane)listChoice.get(sibling)).setStyle(null);
                                Label contentChoiceLbSiblings = (Label) ((AnchorPane)listChoice.get(sibling)).getChildren().get(1);
                                contentChoiceLbSiblings.setStyle("-fx-text-fill: black");
                            }
                        }

                        ImageView imageView = (ImageView) surveyNumPr.getChildren().get(1);
                        imageView.setImage(null);
                        listFinishedAnswer.getChildren().get(finalBigIndex).setStyle(chosenStyle);

                        //write data
                        String fileName = question.getType()+ "_" + question.getOrd();
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        File csatData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                        if(!csatData.exists()){
                            try {
                                csatData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }
                        AnswerLevel levelAnswer = new AnswerLevel();
                        levelAnswer.setLevel(finalIndex+1);
                        levelAnswer.setOrd(question.getOrd());
                        levelAnswer.setSubAnswerID(question.getQuestionID());
                        levelAnswer.setFilled(true);//to check
                        levelAnswer.setRequired(question.isRequire());
                        levelAnswer.setOrdInList(finalBigIndex);//to annoucement to these question in list

                        ObjectMapper mapper = new ObjectMapper();
                        try
                        {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, levelAnswer);
                        } catch (JsonGenerationException ex) {
                            ex.printStackTrace();
                        } catch (JsonMappingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    });
                }

            } else if(question.getType().equals("NPS")){
                questionAnchorPane = createNPSLayout(question, vi);
                HBox parentContent = (HBox) questionAnchorPane.getChildren().get(0);
                Label announcement = (Label) parentContent.getChildren().get(1);
                HBox listChoicePr = (HBox) questionAnchorPane.getChildren().get(1);
                HBox surveyNumPr = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumPr.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số "+ (index+1));
                }else{
                    surveyNum.setText("Survey No." + (index+1));
                }

                //set eff for each choice node
                ObservableList<Node> listChoice = listChoicePr.getChildren();
                for(int choiceIndex = 0; choiceIndex < listChoice.size(); choiceIndex++){
                    AnchorPane choiceAnchorPane = (AnchorPane) listChoice.get(choiceIndex);
                    Label numberLb = (Label) choiceAnchorPane.getChildren().get(0);
                    int finalIndex = choiceIndex;
                    int finalBigIndex = index;

                    choiceAnchorPane.setOnMouseClicked(event -> {
                        //set Effect
                        String normalStyle = "-fx-background-color: white;\n"
                                + "-fx-border-color: yellow;\n";
                        String clickedStyle = "-fx-background-color: yellow;\n"
                                +"-fx-font-weight: bold;\n";
                        numberLb.setStyle(clickedStyle);
                        for(int sibling = 0; sibling < listChoice.size(); sibling++){
                          if(sibling!=finalIndex){
                             Label numE = (Label) ((AnchorPane)listChoice.get(sibling)).getChildren().get(0);
                             numE.setStyle(normalStyle);
                          }
                        }

                        ImageView imageView = (ImageView) surveyNumPr.getChildren().get(1);
                        imageView.setImage(null);

                        listFinishedAnswer.getChildren().get(finalBigIndex).setStyle(chosenStyle);

                        //write data
                        String fileName = question.getType()+ "_" + question.getOrd();
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        File csatData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                        if(!csatData.exists()){
                            try {
                                csatData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }
                        AnswerLevel levelAnswer = new AnswerLevel();
                        levelAnswer.setLevel(finalIndex+1);
                        levelAnswer.setOrd(question.getOrd());
                        levelAnswer.setSubAnswerID(question.getQuestionID());
                        levelAnswer.setOrdInList(finalBigIndex);
                        levelAnswer.setFilled(true);
                        levelAnswer.setRequired(question.isRequire());

                        ObjectMapper mapper = new ObjectMapper();
                        try
                        {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, levelAnswer);
                        } catch (JsonGenerationException ex) {
                            ex.printStackTrace();
                        } catch (JsonMappingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }



                    });
                }

            } else if(question.getType().equals("MULTIPLE_CHOICE")){
                questionAnchorPane = createMutipleChoiceLayout(question, vi);
                HBox parentContent = (HBox) questionAnchorPane.getChildren().get(0);
                Label announcement = (Label) parentContent.getChildren().get(1);
                HBox listChoicePr = (HBox) questionAnchorPane.getChildren().get(1);
                HBox surveyNumPr = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumPr.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số "+ (index+1));
                }else{
                    surveyNum.setText("Survey No." + (index+1));
                }


                HBox listChoice = (HBox) questionAnchorPane.getChildren().get(1);
                ArrayList<Choice> listMultiplechoice = question.getChoice();
                ArrayList<AnswerChoice> listAnswerMultipleChoice = new ArrayList<>();

                ObservableList<Node> listChildrenChoice = listChoice.getChildren();

                for(int i = 0; i<question.getChoice().size(); i++){
                    int finalI = i;
                    int bigIndex = index;
                    ((CheckBox) listChildrenChoice.get(i)).selectedProperty().addListener((observable, oldValue, newValue) -> {
                        //set effect
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                        ImageView imageView = (ImageView) surveyNumPr.getChildren().get(1);
                        imageView.setImage(null);

                        //open file
                        //create Path
                        String fileName = question.getType()+ "_" + question.getOrd();
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        File multipleAnswerData = new File( path.toAbsolutePath().toString() + "\\" + fileName +".json");

                        if(!multipleAnswerData.exists()){
                            try {
                                multipleAnswerData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }

                        //getting data if customers changes any choice
                        AnswerMultipleChoice answerMultipleChoice = new AnswerMultipleChoice();
                        answerMultipleChoice.setOrd(question.getOrd());
                        answerMultipleChoice.setSubAnswerID(question.getQuestionID());
                        AnswerChoice answerChoice = new AnswerChoice();

                        if(newValue){

                            answerChoice.setOrd(((MutipleChoice) listMultiplechoice.get(finalI)).getOrd());
                            answerChoice.setSampleAnswerID(((MutipleChoice) listMultiplechoice.get(finalI)).getSampleAnswerID());

                            listAnswerMultipleChoice.add(answerChoice);
                            answerMultipleChoice.setListAnswerMultiChoice(listAnswerMultipleChoice);

                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                objectMapper.writerWithDefaultPrettyPrinter().writeValue(multipleAnswerData, answerMultipleChoice);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Iterator itr = listAnswerMultipleChoice.iterator();

                            try{
                                while(itr.hasNext()){
                                    AnswerChoice choiceElement = (AnswerChoice) itr.next();
                                    if(choiceElement.getOrd() == Integer.parseInt(listChildrenChoice.get(finalI).getId())){
                                        itr.remove();
                                    }
                                    if(!listAnswerMultipleChoice.isEmpty()){
                                        answerMultipleChoice.setFilled(true);
                                    }else{
                                        answerMultipleChoice.setFilled(false);
                                    }

                                    answerMultipleChoice.setListAnswerMultiChoice(listAnswerMultipleChoice);
                                    answerMultipleChoice.setOrdInList(bigIndex);
                                    answerMultipleChoice.setRequired(question.isRequire());

                                    ObjectMapper objectMapper = new ObjectMapper();
                                    try {
                                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(multipleAnswerData, answerMultipleChoice);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }catch(ConcurrentModificationException ignored){

                            }


                        }

                    });
                }


            }else if(question.getType().equals("SINGLE_CHOICE")){
                questionAnchorPane = createSingleChoiceLayout(question, vi);
                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số " + (index+1));
                }else{
                    surveyNum.setText("Survey No." + (index+1));
                }

                HBox listChoice = (HBox) questionAnchorPane.getChildren().get(1);

                for(int i = 0; i<question.getChoice().size(); i++) {
                    SingleChoice singleChoice = (SingleChoice) question.getChoice().get(i);
                    int bigIndex = index;
                    listChoice.getChildren().get(i).focusedProperty().addListener((observable, oldValue, newValue) -> {
                        //set effect
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);

                        ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                        imageView.setImage(null);
                        //write Data
                        String fileName = question.getType()+ "_" + question.getOrd();

                        //create Path
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // create file to save data

                        File singleData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                        if(!singleData.exists()){
                            try {
                                singleData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }
                        if(newValue){
                            AnswerSingleChoice singleAnswer = new AnswerSingleChoice();
                            singleAnswer.setOrd(question.getOrd());
                            singleAnswer.setSubAnswerID(question.getQuestionID());
                            singleAnswer.setSampleAnswerID(singleChoice.getSampleAnswerID());
                            singleAnswer.setOrdInList(bigIndex);
                            singleAnswer.setFilled(true);
                            singleAnswer.setRequired(question.isRequire());
                            ObjectMapper mapper = new ObjectMapper();
                            try
                            {
                                mapper.writerWithDefaultPrettyPrinter().writeValue(singleData, singleAnswer);
                            } catch (JsonGenerationException ex) {
                                ex.printStackTrace();
                            } catch (JsonMappingException ex) {
                                ex.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                    });

                }
            }else if(question.getType().equals("FLX")){
                questionAnchorPane = createFLXLayout(question, vi);
                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số " + (index + 1));
                }else{
                    surveyNum.setText("Survey No." + (index + 1));
                }
                HBox listChoice = (HBox) questionAnchorPane.getChildren().get(1);
                for(int i = 0; i<5; i++){
                    ObservableList<Node> listChildren = listChoice.getChildren();
                    int level = i+1;
                    int bigIndex = index;
                    listChildren.get(i).setOnMouseClicked(e->{
                        //set eff
                        String normalStyle = "-fx-background-color: white;\n"
                                + "-fx-border-color: yellow;\n";
                        String clickedStyle = "-fx-background-color: yellow;\n"
                                +"-fx-font-weight: bold;\n";

                        listChildren.get(level-1).setStyle(clickedStyle);
                        for(int sibling = 0; sibling < listChildren.size(); sibling++){
                            if(sibling!=level-1){
                                Label numE = (Label) listChildren.get(sibling);
                                numE.setStyle(normalStyle);
                            }
                        }

                        ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                        imageView.setImage(null);

                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                        //write data
                        String fileName = question.getType()+ "_" + question.getOrd();
                        Path path = Paths.get("Data","SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        File csatData = new File(path.toAbsolutePath().toString()+"\\" + fileName +".json");
                        if(!csatData.exists()){
                            try {
                                csatData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }
                        AnswerLevel levelAnswer = new AnswerLevel();
                        levelAnswer.setLevel(level);
                        levelAnswer.setOrd(question.getOrd());
                        levelAnswer.setSubAnswerID(question.getQuestionID());
                        levelAnswer.setFilled(true);
                        levelAnswer.setRequired(question.isRequire());
                        levelAnswer.setOrdInList(bigIndex);

                        ObjectMapper mapper = new ObjectMapper();
                        try
                        {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, levelAnswer);
                        } catch (JsonGenerationException ex) {
                            ex.printStackTrace();
                        } catch (JsonMappingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }


                    });
                }

            }else if(question.getType().equals("CES")){
                questionAnchorPane = createCESLayout(question, vi);
                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số " + (index + 1));
                }else{
                    surveyNum.setText("Survey No." + (index + 1));
                }


                HBox listChoice = (HBox) questionAnchorPane.getChildren().get(1);

                ObservableList<Node> listChildren = listChoice.getChildren();
                for(int i =0; i<7; i++){
                    int level = i+1;
                    int bigIndex = index;
                    listChildren.get(i).setOnMouseClicked(e-> {
                        //set effect
                        String normalStyle = "-fx-background-color: white;\n"
                                + "-fx-border-color: yellow;\n";
                        String clickedStyle = "-fx-background-color: yellow;\n"
                                +"-fx-font-weight: bold;\n";

                        listChildren.get(level-1).setStyle(clickedStyle);
                        for(int sibling = 0; sibling < listChildren.size(); sibling++){
                            if(sibling!=level-1){
                                Label numE = (Label) listChildren.get(sibling);
                                numE.setStyle(normalStyle);
                            }
                        }
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                        ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                        imageView.setImage(null);
                        //write Data
                        String fileName = question.getType() + "_" + question.getOrd();
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        File csatData = new File(path.toAbsolutePath().toString()+"\\" + fileName + ".json");
                        if (!csatData.exists()) {
                            try {
                                csatData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }
                        AnswerLevel levelAnswer = new AnswerLevel();
                        levelAnswer.setLevel(level);
                        levelAnswer.setOrd(question.getOrd());
                        levelAnswer.setSubAnswerID(question.getQuestionID());
                        levelAnswer.setOrdInList(bigIndex);
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, levelAnswer);
                        } catch (JsonGenerationException ex) {
                            ex.printStackTrace();
                        } catch (JsonMappingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    });
                }
            }else if(question.getType().equals("CONTACT")){
                questionAnchorPane = createContactLayout(question, vi);
                int bigIndex = index;
                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);

                if(vi){
                    surveyNum.setText("Khảo sát số "+(index+1));
                }else{
                    surveyNum.setText("Survey No."+(index+1));
                }

                AnswerContact answerContact = new AnswerContact();

                answerContact.setSubAnswerID(question.getQuestionID());
                //init data
                answerContact.setName("");
                answerContact.setAddress("");
                answerContact.setEmail("");
                answerContact.setPhone("");

                answerContact.setOrd(question.getOrd());
                answerContact.setOrdInList(bigIndex);
                answerContact.setRequired(question.isRequire());
                answerContact.setFilled(false);


                HBox infoHb = (HBox) questionAnchorPane.getChildren().get(1);
                AnchorPane parentListInfor = (AnchorPane) infoHb.getChildren().get(0);
                ObservableList<Node> listInfor = parentListInfor.getChildren();

                HBox parentOfName = (HBox) listInfor.get(0);
                HBox parentOfPhone = (HBox) listInfor.get(1);
                HBox parentOfEmail = (HBox) listInfor.get(2);
                HBox parentOfAddress = (HBox) listInfor.get(6);

                Label emailAnnoucement = (Label) listInfor.get(3);
                Label nameAnnoucement = (Label) listInfor.get(4);
                Label phoneAnnoucement = (Label) listInfor.get(5);
                Label addressAnnoucement = (Label) listInfor.get(7);

                TextField name = (TextField) parentOfName.getChildren().get(1);
                TextField phone = (TextField) parentOfPhone.getChildren().get(1);
                TextField email = (TextField) parentOfEmail.getChildren().get(1);
                TextField address = (TextField) parentOfAddress.getChildren().get(1);

                if(vi){
                   name.setPromptText("Họ và tên");
                   phone.setPromptText("Số điện thoại");
                   email.setPromptText("Địa chỉ Email");
                   address.setPromptText("Địa chỉ của bạn");
                }else{
                    name.setPromptText("Your Name");
                    phone.setPromptText("Telephone Number");
                    email.setPromptText("Email");
                    address.setPromptText("Address");
                }

                name.focusedProperty().addListener(((observable, oldValue, newValue) -> {
                    ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                    imageView.setImage(null);

                    String fileName = question.getType()+ "_" + question.getOrd();
                    Path path = Paths.get("Data", "SubAnswer");
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File contactData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                    if(!contactData.exists()){
                        try {
                            contactData.createNewFile();
                        } catch (IOException ex) {
                            System.out.println("can't open file");
                        }
                    }



                    if(!newValue){

                        if(question.isRequire()){
                            ContactField contactField = question.getContactField();
                            if(name.getText().isEmpty()||name.getText()==null){
                                if(contactField.isNameRequire()){
                                    if(contactData.exists()){
                                        contactData.delete();
                                    }
                                    nameAnnoucement.setText("Tên không được để trống");
                                    nameAnnoucement.setVisible(true);
                                }else{
                                    answerContact.setName("");
                                }
                            }else{
                                answerContact.setName(name.getText());

                                if(contactField.isEmailRequire()){
                                    if(answerContact.getEmail().isEmpty()||answerContact.getEmail()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                if(contactField.isPhoneRequire()){
                                    if(answerContact.getPhone().equals("")||answerContact.getPhone()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                if(contactField.isAddressRequire()){
                                    if(answerContact.getAddress().equals("")||answerContact.getAddress()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                if(contactData.exists()){
                                    try{
                                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                    }catch(IOException er){
                                        System.out.println("Lỗi k ghi đc file tại name TextFile");
                                    }
                                }else{
                                    System.out.println("Không ghi được file tại name tf");
                                }

                            }

                        }else{//nếu không require, chỉ lưu khi điền một thông tin nào đó
                            boolean existData = answerContact.getEmail()!=null||answerContact.getPhone()!=null||answerContact.getName()!=null||answerContact.getAddress()!=null;
                            if(existData){
                                if(contactData.exists()){
                                    try{
                                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                    } catch (IOException er) {
                                        er.printStackTrace();
                                    }
                                }
                            }else{
                                if(contactData.exists()){
                                    contactData.delete();
                                }
                            }
                        }
                    }else{
                        nameAnnoucement.setVisible(false);
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                    }




                }));

                address.focusedProperty().addListener(((observable, oldValue, newValue) -> {
                    //dont care
                    ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                    imageView.setImage(null);

                    //create File whenever focusedProperty is changed
                    String fileName = question.getType()+ "_" + question.getOrd();
                    Path path = Paths.get("Data", "SubAnswer");
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File contactData = new File(path.toAbsolutePath().toString() + "\\" + fileName +".json");
                    if(!contactData.exists()){
                        try {
                            contactData.createNewFile();
                        } catch (IOException ex) {
                            System.out.println("can't open file");
                        }
                    }

                    if(!newValue){
                        if(question.isRequire()){
                            ContactField contactField = question.getContactField();
                            if(address.getText().isEmpty()||address.getText()==null){
                                if(contactField.isAddressRequire()){
                                    if(contactData.exists()){
                                        contactData.delete();
                                    }
                                    addressAnnoucement.setText("Địa chỉ không được để trống");
                                    addressAnnoucement.setVisible(true);
                                }else{
                                    answerContact.setAddress("");
                                }
                            }else{
                                answerContact.setAddress(address.getText());

                                if(contactField.isEmailRequire()){
                                    if(answerContact.getEmail().equals("")||answerContact.getEmail()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                if(contactField.isPhoneRequire()){
                                    if(answerContact.getPhone().equals("")||answerContact.getPhone()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                if(contactField.isNameRequire()){
                                    if(answerContact.getName().equals("")||answerContact.getName()==null){
                                        if(contactData.exists()){
                                            contactData.delete();
                                        }
                                    }
                                }
                                try{
                                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);

                                }catch(IOException er){
                                    System.out.println("Lỗi k ghi đc file tại address TextFile");
                                }
                            }


                        }else{//nếu không require, chỉ lưu khi điền một thông tin nào đó
                            boolean existData = answerContact.getEmail()!=null||answerContact.getPhone()!=null||answerContact.getName()!=null||answerContact.getAddress()!=null;
                            if(existData){
                                if(contactData.exists()){
                                    try{
                                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                    } catch (IOException er) {
                                        er.printStackTrace();
                                    }
                                }
                            }else{
                                if(contactData.exists()){
                                    contactData.delete();
                                }
                            }

                        }
                    }else{
                        addressAnnoucement.setVisible(false);
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                    }

                }));

                email.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    answerContact.setOrdInList(bigIndex);
                    answerContact.setRequired(question.isRequire());
                    answerContact.setFilled(false);
                    ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                    imageView.setImage(null);

                    //create file
                    String fileName = question.getType()+ "_" + question.getOrd();
                    Path path = Paths.get("Data", "SubAnswer");
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File contactData = new File(path.toAbsolutePath().toString() +"\\"+ fileName +".json");
                    if(!contactData.exists()){
                        try {
                            contactData.createNewFile();
                        } catch (IOException ex) {
                            System.out.println("can't open file");
                        }
                    }

                    if(!newValue){
                        // check require. if not, save as normal
                        if(question.isRequire()){
                            ContactField contactField = question.getContactField();

                            if(email.getText().isEmpty()||email.getText()==null){
                                contactData.delete();
                                emailAnnoucement.setText("(*) Email không được để trống");
                                emailAnnoucement.setVisible(true);
                                listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                            }else{
                                if(checkValidateEmail(email.getText())){
                                    answerContact.setEmail(email.getText());

                                    if(contactField.isAddressRequire()){
                                        if(answerContact.getEmail().isEmpty()||answerContact.getEmail()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }

                                    if(contactField.isPhoneRequire()){
                                        if(answerContact.getPhone().isEmpty()||answerContact.getPhone()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }

                                    if(contactField.isNameRequire()){
                                        if(answerContact.getName().isEmpty()||answerContact.getName()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }
                                    if(contactData.exists()){
                                        try {
                                            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        System.out.println("Không tồn tại file khi lưu email. thiếu thông tin");
                                    }


                                }else{
                                    contactData.delete();
                                    emailAnnoucement.setText("Email không đúng định dạng");
                                    emailAnnoucement.setVisible(true);
                                }
                            }

                        }else{
                            boolean checkExist = answerContact.getEmail()!=null||answerContact.getPhone()!=null||answerContact.getName()!=null||answerContact.getAddress()!=null;
                            if(checkExist){
                                if(contactData.exists()){
                                    try{
                                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                    }catch(IOException er){
                                        System.out.println("không ghi được file khi question require == false tại email");
                                    }
                                }
                            }else{
                                System.out.println("file k ton tai tai email khi question require == false");
                            }
                        }

                    }else{
                        emailAnnoucement.setVisible(false);
                    }

                });

                phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                    imageView.setImage(null);

                    String fileName = question.getType()+ "_" + question.getOrd();
                    Path path = Paths.get("Data", "SubAnswer");
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File contactData = new File(path.toAbsolutePath().toString() +"\\"+ fileName +".json");
                    if(!contactData.exists()){
                        try {
                            contactData.createNewFile();
                        } catch (IOException ex) {
                            System.out.println("can't open file");
                        }
                    }

                    if(!newValue){

                        if(question.isRequire()){
                            ContactField contactField = question.getContactField();

                            if(phone.getText().isEmpty()||phone.getText()==null){
                                if(contactField.isPhoneRequire()){
                                    contactData.delete();
                                    phoneAnnoucement.setText("(*) Số điện thoại không được để trống");
                                    phoneAnnoucement.setVisible(true);
                                }
                                listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                            }else{
                                if(checkValidatePhone(phone.getText())){
                                    answerContact.setPhone(phone.getText());

                                    if(contactField.isAddressRequire()){
                                        if(answerContact.getAddress().equals("")||answerContact.getAddress()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }
                                    if(contactField.isEmailRequire()){
                                        if(answerContact.getEmail().equals("")||answerContact.getEmail()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }
                                    if(contactField.isNameRequire()){
                                        if(answerContact.getName().equals("")||answerContact.getName()==null){
                                            if(contactData.exists()){
                                                contactData.delete();
                                            }
                                        }
                                    }

                                    if(contactData.exists()){
                                        try {
                                            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        System.out.println("Không tồn tại file khi lưu phone. thiếu thông tin");
                                    }


                                }else{
                                    contactData.delete();
                                    phoneAnnoucement.setText("Số điện thoại không đúng định dạng");
                                    phoneAnnoucement.setVisible(true);
                                }
                            }

                        }else{
                            boolean checkExist = answerContact.getEmail()!=null||answerContact.getPhone()!=null||answerContact.getName()!=null||answerContact.getAddress()!=null;
                            if(checkExist){
                                if(contactData.exists()){
                                    try{
                                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                                    }catch(IOException er){
                                        System.out.println("không ghi được file khi question require == false tại email");
                                    }
                                }
                            }else{
                                System.out.println("file k ton tai tai email khi question require == false");
                            }
                        }
                    }else{
                        phoneAnnoucement.setVisible(false);
                    }

                });

            }else if(question.getType().equals("OPEN")){
                questionAnchorPane = createOpenLayout(question, vi);
                HBox contentHb = (HBox) questionAnchorPane.getChildren().get(0);
                HBox textHb = (HBox) questionAnchorPane.getChildren().get(1);
                HBox contentNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) contentNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số "+(index+1));
                }else{
                    surveyNum.setText("Survey No."+(index+1));
                }

                TextArea contentAnswer = (TextArea) textHb.getChildren().get(0);
                AnswerOpen answerOpen = new AnswerOpen();
                answerOpen.setSubAnswerID(question.getQuestionID());
                answerOpen.setOrd(question.getOrd());
                int bigIndex = index;
                contentAnswer.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    ImageView imageView = (ImageView) contentNumHb.getChildren().get(1);
                    imageView.setImage(null);

                    //set eff
                    listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                    if(!newValue){
                        //open and write data to file
                        if(!contentAnswer.getText().equals("")){
                            String fileName = question.getType()+ "_" + question.getOrd();
                            Path path = Paths.get("Data", "SubAnswer");
                            try {
                                Files.createDirectories(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File openData = new File(path.toAbsolutePath().toString()+"\\" + fileName +".json");

                            if(!openData.exists()){
                                try {
                                    openData.createNewFile();
                                } catch (IOException ex) {
                                    System.out.println("can't open file");
                                }
                            }

                            if(question.isRequire()){
                                if(contentAnswer.getText().isEmpty()){
                                    answerOpen.setFilled(false);
                                }else{
                                    answerOpen.setFilled(true);
                                }
                            }else{
                                answerOpen.setFilled(true);
                            }

                            answerOpen.setOrdInList(bigIndex);
                            answerOpen.setRequired(question.isRequire());
                            answerOpen.setContentAnswer(contentAnswer.getText());
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                objectMapper.writerWithDefaultPrettyPrinter().writeValue(openData, answerOpen);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }

                    }
                });
            }else if(question.getType().equals("STAR")){
                questionAnchorPane = createStarLayout(question, vi);
                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                HBox listChoice = (HBox) questionAnchorPane.getChildren().get(1);

                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số " +(index+1));
                }else{
                    surveyNum.setText("Survey No." + (index+1));
                }

                String emptyStarRootPath = "src/main/resources/FixedSetting/Icon/Star/whiteStar.png";
                String filledStarRootPath = "src/main/resources/FixedSetting/Icon/Star/filledStar.png";
                ObservableList<Node> listImageStar = listChoice.getChildren();

                int bigIndex = index;
                for(int i = 0; i<5; i++){
                    int level = i +1;
                    listImageStar.get(i).setOnMouseClicked(e->{
                        //set eff
                        ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                        imageView.setImage(null);

                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);

                        //set event
                        for(int j = 0; j<level; j++){
                            try {
                                ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(filledStarRootPath)));
                            } catch (FileNotFoundException ex) {
                                Path filledStarPath = Paths.get("FixedSetting", "Icon\\Star");
                                try{
                                    ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(filledStarPath.toAbsolutePath().toString() + "\\filledStar.png")));
                                } catch (FileNotFoundException exception) {
                                    Alert filledAnnoucement = new Alert(AlertType.INFORMATION);
                                    filledAnnoucement.setContentText("Filled star k ton tai");
                                    filledAnnoucement.show();
                                }
                            }
                        }
                        for(int j = level; j<5; j++){
                            try {
                                ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(emptyStarRootPath)));
                            } catch (FileNotFoundException ex) {
                                Path filledStarPath = Paths.get("FixedSetting", "Icon\\Star");
                                try{
                                    ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(filledStarPath.toAbsolutePath().toString() + "\\whiteStar.png")));
                                } catch (FileNotFoundException exception) {
                                    Alert filledAnnoucement = new Alert(AlertType.INFORMATION);
                                    filledAnnoucement.setContentText("empty star k ton tai khi click");
                                    filledAnnoucement.show();
                                }
                            }
                        }



                        //write Data to file
                        String fileName = question.getType()+ "_" + question.getOrd();
                        Path path = Paths.get("Data", "SubAnswer");
                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        File starData = new File(path.toAbsolutePath().toString()+"\\" + fileName +".json");
                        if(!starData.exists()){
                            try {
                                starData.createNewFile();
                            } catch (IOException ex) {
                                System.out.println("can't open file");
                            }
                        }

                        AnswerLevel levelAnswer = new AnswerLevel();
                        levelAnswer.setLevel(level);
                        levelAnswer.setOrd(question.getOrd());
                        levelAnswer.setSubAnswerID(question.getQuestionID());
                        levelAnswer.setOrdInList(bigIndex);
                        levelAnswer.setFilled(true);
                        levelAnswer.setRequired(question.isRequire());

                        ObjectMapper mapper = new ObjectMapper();
                        try
                        {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(starData, levelAnswer);
                        } catch (JsonGenerationException ex) {
                            ex.printStackTrace();
                        } catch (JsonMappingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }


                    });
                }

            }
            listQuestions.getChildren().add(questionAnchorPane);
        }
    }

    //check Data before sending
    private ArrayList<Integer> listDataNotFiled(){
        ArrayList<Integer> listIndexQuestionNotFilled = new ArrayList<>();

        Path listTempFilePath = Paths.get("Temp","CheckData");
        Path listSubAnswerPath = Paths.get("Data", "SubAnswer");

        if(listSubAnswerPath.toFile().isDirectory()){
            File[] listSubAnswer = listSubAnswerPath.toFile().listFiles();
            if(listTempFilePath.toFile().isDirectory()){
                    File[] listTempFile = listTempFilePath.toFile().listFiles();
                    if(listTempFile== null){
                        listIndexQuestionNotFilled.add(-1);
                        return listIndexQuestionNotFilled;
                    }else{
                        for (File file : listTempFile) {
                            String tempFileName = file.getName();
                            File ifExistFile = new File(listSubAnswerPath.toAbsolutePath().toString() + "\\" + tempFileName);
                            if (!ifExistFile.exists()) {
                                try {
                                    CheckRequireQuestion checkRequireQuestion = new ObjectMapper().readValue(file, CheckRequireQuestion.class);
                                    listIndexQuestionNotFilled.add(checkRequireQuestion.getOrderInList());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }
        }

        return listIndexQuestionNotFilled;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //set full scene for question layout
        setFullQuestionLayout();

        AnswerService answerService = new AnswerService();

        //create timeline for waiting video
        waitingScene = getWaitingScene();

        assert waitingScene != null;
        AtomicLong second = new AtomicLong(waitingScene.getTime());

        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
            second.getAndDecrement();
            System.out.println(second.get());
            if(second.get() == 0){
                time.stop();
                if(!waitingScene.isType()){
                    Stage stage = (Stage) back.getScene().getWindow();
                    stage.setScene(new Scene(VideoController.getParent()));
                    stage.show();
                }else{
                    Stage stage = (Stage) back.getScene().getWindow();
                    stage.setScene(new Scene(WaitingSceneSlideController.getParent()));
                    stage.show();
                }
            }
        });

        time.getKeyFrames().add(frame);
        time.playFromStart();

        //mapping Data
        Survey survey = getSurveyData();

        displayData(true);
        AtomicBoolean languageFlag = new AtomicBoolean(true);
        //Tieng viet by default
        languageSwitch.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
                if (newValue) {
                    languageSwitch.setText("English");
                    listQuestions.getChildren().clear();
                    displayData(false);
                    languageFlag.set(false);
                    for(int i = 0; i<listFinishedAnswer.getChildren().size(); i++){
                        listFinishedAnswer.getChildren().get(i).setStyle(normalStyle);
                    }
                }else{
                    languageSwitch.setText("Tiếng Việt");
                    listQuestions.getChildren().clear();
                    displayData(true);
                    languageFlag.set(true);
                    for(int i = 0; i<listFinishedAnswer.getChildren().size(); i++){
                        listFinishedAnswer.getChildren().get(i).setStyle(normalStyle);
                    }
                }
            });

        assert survey != null;
        generate(survey.getListQuestion().size());

        //set Event for buttons
        sendingSurvey.setOnAction(e->{
            AnswerTotal answerTotal = new AnswerTotal();
            try{
                answerTotal.setAnswerTotalID(answerService.getAnswerTotalID());
            }catch (IOException er){
                System.out.println("Lỗi set ID cho answer total");
            }

            ArrayList<SubAnswer> listAnswer = new ArrayList<>();
            Path path = Paths.get("Data", "SubAnswer");
            File parentListData = new File(path.toAbsolutePath().toString());

            //check require data
            ArrayList<Integer> listNotFilledData = listDataNotFiled();
            if(listNotFilledData.isEmpty()){
                if(parentListData.isDirectory()){
                    File[] listDataFile = parentListData.listFiles();
                    assert listDataFile != null;
                    for(File child : listDataFile){
                        String fileName = child.getName();
                        String[] splitFileName = fileName.split("[.]");
                        String[] typeRaw = splitFileName[0].split("_");
                        String type = typeRaw[0];

                        if(type.equals("CSAT")||type.equals("NPS")||type.equals("CES")||type.equals("FLX")||type.equals("STAR")){
                            try {
                                SubAnswer answerLevel = new ObjectMapper().readValue(child, AnswerLevel.class);
                                listAnswer.add(answerLevel);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }else if(type.equals("MULTIPLE")){
                            try {
                                SubAnswer answerLevel = new ObjectMapper().readValue(child, AnswerMultipleChoice.class);
                                listAnswer.add(answerLevel);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }else if(type.equals("SINGLE")){
                            try {
                                SubAnswer answerSingle = new ObjectMapper().readValue(child, AnswerSingleChoice.class);
                                listAnswer.add(answerSingle);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }else if(type.equals("OPEN")){
                            try {
                                SubAnswer answerOpen = new ObjectMapper().readValue(child, AnswerOpen.class);
                                listAnswer.add(answerOpen);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }else if(type.equals("CONTACT")){
                            try {
                                SubAnswer answerContact = new ObjectMapper().readValue(child, AnswerContact.class);
                                listAnswer.add(answerContact);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                    }
                    answerTotal.setListAnswer(listAnswer);
                }
                long stt = answerService.uploadAnswerTotal(answerTotal);

                if(stt != 400 && stt !=500){
                    Path answerPathData = Paths.get("Data","Answer", "UpdatedAnswer");
                    try {
                        Files.createDirectories(answerPathData);
                    } catch (IOException ex) {
                        System.out.println("không thể tạo đường dẫn UpdatedAnswer");
                    }
                    String nameOfUpdatedAnswer = "answerTotal_" + LocalDateTime.now().getHour() + "h"+ LocalDateTime.now().getMinute() + "m"+LocalDateTime.now().getSecond() + ".json";
                    File updatedAnswerData = new File(answerPathData.toAbsolutePath().toString()+"\\" + nameOfUpdatedAnswer);
                    if(!updatedAnswerData.exists()){
                        try {
                            updatedAnswerData.createNewFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    try{
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(updatedAnswerData, answerTotal);
                        RewardService rewardService = new RewardService();
                        rewardService.getReward(answerTotal.getAnswerTotalID());
                    } catch (JsonGenerationException err) {
                        System.out.println("không thể tạo json Generator");
                    } catch (JsonMappingException err) {
                        System.out.println("Không thể mapping dữ liệu!");
                    } catch (IOException err) {
                        System.out.println("Lỗi ghi dữ liệu online vào file");
                    }

                    File[] listDatafile = parentListData.listFiles();
                    for(int i = 0; i <listAnswer.size(); i++){
                        assert listDatafile != null;
                        listDatafile[i].delete();
                    }
                    time.stop();
                    setScene(CThanksController.getParent(), e);
                }else{
                    Path answerPathData = Paths.get("Data","Answer", "NotBeUpdated");
                    try {
                        Files.createDirectories(answerPathData);
                    } catch (IOException ex) {
                        System.out.println("không thể tạo đường dẫn answer offline");
                    }
                    String nameOfNotBeUpdatedAnswer = "answerTotal_" + LocalDateTime.now().getHour() + "h"+ LocalDateTime.now().getMinute() + "m"+LocalDateTime.now().getSecond() + ".json";
                    File notBeUpdatedAnswer = new File(answerPathData.toAbsolutePath().toString() + "\\" + nameOfNotBeUpdatedAnswer);
                    if(!notBeUpdatedAnswer.exists()){
                        try {
                            notBeUpdatedAnswer.createNewFile();
                        } catch (IOException ex) {
                            System.out.println("Không thể ghi thông tin answer vào file offline");
                        }
                    }
                    try{
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(notBeUpdatedAnswer, answerTotal);
                    } catch (JsonGenerationException err) {
                        System.out.println("không thể tạo json Generator");
                    } catch (JsonMappingException err) {
                        System.out.println("Không thể mapping dữ liệu!");
                    } catch (IOException err) {
                        System.out.println("Lỗi ghi dữ liệu offline vào file");
                    }
                }
            }else{
                second.set(waitingScene.getTime());
                Alert warningDataNotFilled = new Alert(AlertType.INFORMATION);
                warningDataNotFilled.setContentText("Bạn cần điền đầy đủ các thông tin bắt buộc");
                warningDataNotFilled.show();
                for(int i = 0; i<listNotFilledData.size(); i++){
                    AnchorPane questionAnchorPane = (AnchorPane) listQuestions.getChildren().get(listNotFilledData.get(i));
                    HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                    ImageView imageView = (ImageView) surveyNumHb.getChildren().get(1);
                    try{
                        String rootWarningIconPath = "src/main/resources/FixedSetting/Icon/Question/warning.png";
                        File warningIconFile = new File(rootWarningIconPath);
                        Image image = new Image(new FileInputStream(warningIconFile));
                        imageView.setImage(image);
                    } catch (FileNotFoundException ex) {
                        Path warningIconPath = Paths.get("FixedSetting", "Icon\\Question");
                        try{
                            File warningIconFile = new File(warningIconPath.toAbsolutePath().toString() + "\\warning.png");
                            Image image = new Image(new FileInputStream(warningIconFile));
                            imageView.setImage(image);
                        } catch (FileNotFoundException exception) {
                            System.out.println("Không tìm thấy warningIcon");
                        }
                    }
                }
            }

        });

        undo.setOnAction(event -> {
            listQuestions.getChildren().clear();
            displayData(languageFlag.get());
            for(int i = 0; i<listFinishedAnswer.getChildren().size(); i++){
                listFinishedAnswer.getChildren().get(i).setStyle(normalStyle);
            }
            Path path = Paths.get("Data", "SubAnswer");
            File[] listSubAnswer = path.toFile().listFiles();
            if(listSubAnswer!=null){
                for(int i = 0; i<listSubAnswer.length; i++){
                    listSubAnswer[i].delete();
                }
            }

        });

        back.setOnAction(e->{
            Stage QStage = (Stage) back.getScene().getWindow();
            QStage.close();
            time.stop();
        });

        //when customer touch on the scene, second set to 10 and recount
        bigBackground.setOnMouseClicked(e->{
            second.set(waitingScene.getTime());
        });

    }


}
