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
import Models.Survey.Question;
import Models.Survey.Survey;
import Models.User.User;
import Service.impl.AnswerService;
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
        if (vi == true){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
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
        }else{
            content.setText(question.getEnContent());
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

        for(int i = 0; i<question.getChoice().size(); i++) {
            SingleChoice singleChoice = (SingleChoice) question.getChoice().get(i);
            listChoice.getChildren().get(i).focusedProperty().addListener((observable, oldValue, newValue) -> {
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
                String fileName = "\\icon_"+ (i+1) + ".gif";
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
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
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
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
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
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
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
        HBox contentQuestionHb = (HBox) starAnchorPane.getChildren().get(0);
        HBox listChoice = (HBox) starAnchorPane.getChildren().get(1);

        Label contentQuestion = (Label) contentQuestionHb.getChildren().get(0);

        //set content
        if(vi){
            contentQuestion.setText(question.getViContent());

        }else{
            contentQuestion.setText(question.getEnContent());
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
                   imageView = configureStar(emptyStarPath.toAbsolutePath().toString() + "\\emptyStar.png");
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
        Label contentQuestion = (Label) contentHb.getChildren().get(0);
        if(vi){
            contentQuestion.setText(question.getViContent());

        }else{
            contentQuestion.setText(question.getEnContent());
        }

        return openLayout;
    }

    private AnchorPane createContactLayout(Question question, boolean vi){
        AnchorPane contactLayout = (AnchorPane) CQuestionContactController.getParent();
        HBox contentQuestionHb = (HBox) contactLayout.getChildren().get(0);

        Label contentQuestion = (Label) contentQuestionHb.getChildren().get(0);

        if(vi){
            contentQuestion.setText(question.getViContent());
        }else{
            contentQuestion.setText(question.getEnContent());
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


    private void setFinishAnswerLight(){

    }


    private void displayData(boolean vi){
        Survey survey = getSurveyData();
        assert survey != null;
        contentSurvey.setText(survey.getContentSurvey());
        ArrayList<Question> listQuestionOfSurvey = survey.getListQuestion();
        for(int index = 0; index<listQuestionOfSurvey.size(); index++){
            AnchorPane questionAnchorPane = new AnchorPane();
            Question question = listQuestionOfSurvey.get(index);

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

                                    answerMultipleChoice.setListAnswerMultiChoice(listAnswerMultipleChoice);
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

                HBox surveyNumHb = (HBox) questionAnchorPane.getChildren().get(2);
                Label surveyNum = (Label) surveyNumHb.getChildren().get(0);
                if(vi){
                    surveyNum.setText("Khảo sát số "+(index+1));
                }else{
                    surveyNum.setText("Survey No."+(index+1));
                }

                AnswerContact answerContact = new AnswerContact();

                answerContact.setSubAnswerID(question.getQuestionID());
                answerContact.setOrd(question.getOrd());

                HBox infoHb = (HBox) questionAnchorPane.getChildren().get(1);
                AnchorPane parentListInfor = (AnchorPane) infoHb.getChildren().get(0);
                ObservableList<Node> listInfor = parentListInfor.getChildren();

                HBox parentOfName = (HBox) listInfor.get(0);
                HBox parentOfPhone = (HBox) listInfor.get(1);
                HBox parentOfEmail = (HBox) listInfor.get(2);

                Label emailAnnoucement = (Label) listInfor.get(3);
                Label nameAnnoucement = (Label) listInfor.get(4);
                Label phoneAnnoucement = (Label) listInfor.get(5);

                TextField name = (TextField) parentOfName.getChildren().get(1);
                TextField phone = (TextField) parentOfPhone.getChildren().get(1);
                TextField email = (TextField) parentOfEmail.getChildren().get(1);

                int bigIndex = index;
                name.focusedProperty().addListener(((observable, oldValue, newValue) -> {
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
                        if(name.getText().equals("")){
                            nameAnnoucement.setText("(*) Tên không được để trống");
                            nameAnnoucement.setVisible(true);
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                        }else{
                            answerContact.setName(name.getText());
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                        }
                    }else{
                        nameAnnoucement.setVisible(false);
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));

                email.focusedProperty().addListener((observable, oldValue, newValue) -> {
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

                        if(checkValidateEmail(email.getText())){
                            answerContact.setEmail(email.getText());
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);

                        }else{
                            if(email.getText().equals("")){
                                emailAnnoucement.setText("(*) Email không được để trống");
                            } else{
                                emailAnnoucement.setText("(*) Email không hợp lệ");
                            }
                            emailAnnoucement.setVisible(true);
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                        }
                    }else{
                        emailAnnoucement.setVisible(false);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
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

                        if(checkValidatePhone(phone.getText())){
                            answerContact.setPhone(phone.getText());
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);

                        }else{
                            if(phone.getText().equals("")){
                                phoneAnnoucement.setText("(*) số điện thoại không được để trống");
                            } else{
                                phoneAnnoucement.setText("(*) số điện thoại không hợp lệ");
                            }
                            phoneAnnoucement.setVisible(true);
                            listFinishedAnswer.getChildren().get(bigIndex).setStyle(normalStyle);
                        }
                    }else{
                        phoneAnnoucement.setVisible(false);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(contactData, answerContact);
                    } catch (IOException e) {
                        e.printStackTrace();
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
                    //set eff
                    listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);
                    if(!newValue){

                        //open and write data to file
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
                        answerOpen.setContentAnswer(contentAnswer.getText());
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            objectMapper.writerWithDefaultPrettyPrinter().writeValue(openData, answerOpen);
                        } catch (IOException e) {
                            e.printStackTrace();
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
                        listFinishedAnswer.getChildren().get(bigIndex).setStyle(chosenStyle);

                        //set event
                        for(int j = 0; j<level; j++){
                            try {
                                ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(filledStarRootPath)));
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                        for(int j = level; j<5; j++){
                            try {
                                ((ImageView)listImageStar.get(j)).setImage(new Image(new FileInputStream(emptyStarRootPath)));
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
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

                        //set effect

                    });
                }

            }
            listQuestions.getChildren().add(questionAnchorPane);
        }

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
            if(second.get() == 0){
                time.stop();
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(VideoController.getParent()));
                stage.show();
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
            if(parentListData.isDirectory()){
                File[] listDataFile = parentListData.listFiles();
                for(File child : listDataFile){
                    String fileName = child.getName();
                    String[] splitFileName = fileName.split("[.]");
                    String[] typeRaw = splitFileName[0].split("_");
                    String type = typeRaw[0];

                    if(type.equals("CSAT")||type.equals("NPS")||type.equals("CES")||type.equals("FLX")||type.equals("STAR")){
                        SubAnswer answerLevel;
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            answerLevel = objectMapper.readValue(child, AnswerLevel.class);
                            listAnswer.add(answerLevel);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else if(type.equals("MULTIPLE")){
                        SubAnswer answerLevel;
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            answerLevel = objectMapper.readValue(child, AnswerMultipleChoice.class);
                            listAnswer.add(answerLevel);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else if(type.equals("SINGLE")){
                        SubAnswer answerSingle;
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            answerSingle = objectMapper.readValue(child, AnswerSingleChoice.class);
                            listAnswer.add(answerSingle);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else if(type.equals("OPEN")){
                        SubAnswer answerOpen;
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            answerOpen = objectMapper.readValue(child, AnswerOpen.class);

                            listAnswer.add(answerOpen);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else if(type.equals("CONTACT")){
                        SubAnswer answerContact;
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            answerContact = objectMapper.readValue(child, AnswerContact.class);
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
        });

        undo.setOnAction(event -> {
            listQuestions.getChildren().clear();
            displayData(languageFlag.get());
            for(int i = 0; i<listFinishedAnswer.getChildren().size(); i++){
                listFinishedAnswer.getChildren().get(i).setStyle(normalStyle);
            }
        });

        back.setOnAction(e->{
            setScene(QIndexController.getParent(), e);
            time.stop();
        });

        //when customer touch on the scene, second set to 10 and recount
        bigBackground.setOnMouseClicked(e->{
            second.set(waitingScene.getTime());
        });

    }


}
