package View.Design.Client.Questions;

import Models.Answer.AnswerContact.AnswerContact;
import Models.Answer.AnswerLevelGroup.AnswerLevel;
import Models.Answer.AnswerMultipleChoice.AnswerChoice;
import Models.Answer.AnswerMultipleChoice.AnswerMultipleChoice;
import Models.Answer.AnswerOpen.AnswerOpen;
import Models.Answer.AnswerSingleChoice.AnswerSingleChoice;
import Models.Answer.AnswerTotal;
import Models.Answer.SubAnswer;
import Models.Survey.Choice.Choice;
import Models.Survey.Choice.MutipleChoice;
import Models.Survey.Choice.SingleChoice;
import Models.Survey.Question;
import Models.Survey.Survey;
import Service.impl.AnswerService;
import Service.impl.SurveyService;
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
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CQuestionsController extends Application implements Initializable {

    @FXML private VBox listQuestions;
    @FXML private Button back;
    @FXML private Label contentSurvey;
    @FXML private Button sendingSurvey;

    private int second = 10;
    private SurveyService surveyService;
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
    private AnchorPane createMutipleChoiceLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();
        //setContentQuestion
        Label content = (Label) questionLayout.getChildren().get(0);
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




        ArrayList<Choice> listMultiplechoice = question.getChoice();
        ArrayList<AnswerChoice> listAnswerMultipleChoice = new ArrayList<>();

        ObservableList<Node> listChildrenChoice = listChoice.getChildren();

        for(int i = 0; i<question.getChoice().size(); i++){
            int finalI = i;
            ((CheckBox) listChildrenChoice.get(i)).selectedProperty().addListener((observable, oldValue, newValue) -> {
                //open file
                String fileName = question.getType()+ "_" + question.getOrd();
                File multipleAnswerData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");

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

                    }catch(ConcurrentModificationException e){
                        System.out.println("hihi");
                    }

//                    listAnswerMultipleChoice.remove(answerChoice);
//                    answerMultipleChoice.setListAnswerMultiChoice(listAnswerMultipleChoice);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    try {
//                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(multipleAnswerData, answerMultipleChoice);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                if(listAnswerMultipleChoice != null){
                    System.out.println(listAnswerMultipleChoice);
                }


            });
//            System.out.println(listAnswerMultipleChoice.get(i));
        }
        return questionLayout;

    }

    private AnchorPane createSingleChoiceLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCSATController.getParent();

        //setContentQuestion
        Label content = (Label) questionLayout.getChildren().get(0);
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
            int indexChoice = i;
            SingleChoice singleChoice = (SingleChoice) question.getChoice().get(i);
            listChoice.getChildren().get(i).focusedProperty().addListener((observable, oldValue, newValue) -> {
                String fileName = question.getType()+ "_" + question.getOrd();
                File csatData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
                if(!csatData.exists()){
                    try {
                        csatData.createNewFile();
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
                        mapper.writerWithDefaultPrettyPrinter().writeValue(csatData, singleAnswer);
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
        Label content = (Label) questionLayout.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);

        for(int i = 0; i<5; i++){
            AnchorPane questionChoice = (AnchorPane) CQuestionCSATChoiceController.getParent();
            Label labelContent = (Label) questionChoice.getChildren().get(1);
            questionChoice.setId(String.valueOf((i+1)));
            questionChoice.setCursor(Cursor.HAND);
            listChoice.getChildren().add(questionChoice);
        }

        for(int i = 0; i<5; i++){
            ObservableList<Node> listChildrent = listChoice.getChildren();
            int level = i+1;
            listChildrent.get(i).setOnMouseClicked(e->{
                String fileName = question.getType()+ "_" + question.getOrd();
                File csatData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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
        Label content = (Label) questionLayout.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);
        ToggleGroup toggleGroup = new ToggleGroup();
        for(int i = 0; i<5; i++){
            AnchorPane questionChoice = (AnchorPane) CQuestionsNPSChoiceController.getParent();
            Label labelContent = (Label) questionChoice.getChildren().get(0);
            questionChoice.setId(String.valueOf((i+1)));
            labelContent.setText(String.valueOf(i+1));
            listChoice.getChildren().add(questionChoice);
        }

        ObservableList<Node> listChildren = listChoice.getChildren();

        for(int i =0; i<5; i++){
            int level = i+1;
            listChildren.get(i).setOnMouseClicked(e-> {
                String fileName = question.getType() + "_" + question.getOrd();
                File csatData = new File("src/main/java/Data/SubAnswer/" + fileName + ".json");
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

        listChoice.setSpacing(15.0);


        return questionLayout;

    }

    private AnchorPane createFLXLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionFLXController.getParent();

        //setContentQuestion
        Label content = (Label) questionLayout.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);


        for(int i = 0; i< question.getMaxLevel(); i++){
            Label level = new Label();
            level.setText(String.valueOf(i+1));
            level.setFont(new Font(30));
            level.setPrefWidth(60.0);
            level.setPrefHeight(60.0);
            level.setAlignment(Pos.CENTER);
            level.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            listChoice.getChildren().add(level);
        }

        for(int i = 0; i<5; i++){
            ObservableList<Node> listChildrent = listChoice.getChildren();
            int level = i+1;
            listChildrent.get(i).setOnMouseClicked(e->{
                String fileName = question.getType()+ "_" + question.getOrd();
                File csatData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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

    private AnchorPane createCESLayout(Question question, boolean vi){
        AnchorPane questionLayout = (AnchorPane) CQuestionsCESController.getParent();

        //setContentQuestion
        Label content = (Label) questionLayout.getChildren().get(0);
        if (vi){
            content.setText(question.getViContent());
        }else{
            content.setText(question.getEnContent());
        }

        //set Choices
        HBox listChoice = (HBox) questionLayout.getChildren().get(1);

        for(int i = 0; i<7; i++){
            Label level = new Label();
            level.setPrefHeight(50.0);
            level.setPrefWidth(50.0);
            level.setAlignment(Pos.CENTER);
            level.setFont(new Font(18.0));
            level.setCursor(Cursor.HAND);
            level.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            level.setText(String.valueOf(i+1));
            level.setId(String.valueOf(i));
            listChoice.getChildren().add(level);
        }

        ObservableList<Node> listChildren = listChoice.getChildren();
        for(int i =0; i<7; i++){
            ObservableList<Node> listChildrent = listChoice.getChildren();
            int level = i+1;
            listChildren.get(i).setOnMouseClicked(e-> {
                String fileName = question.getType() + "_" + question.getOrd();
                File csatData = new File("src/main/java/Data/SubAnswer/" + fileName + ".json");
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

        listChoice.setSpacing(15.0);

        return questionLayout;
    }

    private AnchorPane createStarLayout(Question question, boolean vi){
        AnchorPane starAnchorPane = (AnchorPane) CQuestionStarController.getParent();
        Label contentQuestion = (Label) starAnchorPane.getChildren().get(0);
        HBox listChoice = (HBox) starAnchorPane.getChildren().get(1);

        //set content
        if(vi){
            contentQuestion.setText(question.getViContent());

        }else{
            contentQuestion.setText(question.getEnContent());
        }

        //set Choice
        String emptyStarPath = "src/main/resources/View/Image/emptyStar.png";
        String fullStarPath = "src/main/resources/View/Image/fullStar.png";
        File fileEmptyStar = new File(emptyStarPath);
        File fileFullStar = new File(fullStarPath);
        if(fileEmptyStar.exists()&&fileFullStar.exists()){;
            System.out.println("file Tồn tại");
        }

        Image emptyStarImage = new Image(fileEmptyStar.toURI().toString());
        Image fullStarImage = new Image(fileFullStar.toURI().toString());

        for(int i = 0; i<5; i++){
            ImageView starLevel = new ImageView(emptyStarImage);
            starLevel.setFitWidth(100.0);
            starLevel.setFitHeight(100.0);
            starLevel.setCursor(Cursor.HAND);
            starLevel.setId(String.valueOf(i+1));
            listChoice.getChildren().add(starLevel);
        }
        ObservableList<Node> listImageStar = listChoice.getChildren();

        for(int i = 0; i<5; i++){
            int level = i +1;
            listImageStar.get(i).setOnMouseClicked(e->{
                String fileName = question.getType()+ "_" + question.getOrd();
                File starData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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

            });
        }



        return starAnchorPane;
    }

    private AnchorPane createOpenLayout(Question question, boolean vi){
        AnchorPane openLayout = (AnchorPane) CQuestionOpenController.getParent();
        Label contentQuestion = (Label) openLayout.getChildren().get(0);
        if(vi){
            contentQuestion.setText(question.getViContent());
        }else{
            contentQuestion.setText(question.getEnContent());
        }
        TextArea contentAnswer = (TextArea) openLayout.getChildren().get(1);

        AnswerOpen answerOpen = new AnswerOpen();
        answerOpen.setSubAnswerID(question.getQuestionID());
        answerOpen.setOrd(question.getOrd());
        contentAnswer.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                //open and write data to file
                String fileName = question.getType()+ "_" + question.getOrd();
                File openData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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
        return openLayout;
    }

    private AnchorPane createContactLayout(Question question, boolean vi){
        AnchorPane contactLayout = (AnchorPane) CQuestionContactController.getParent();
        Label contentQuestion = (Label) contactLayout.getChildren().get(0);

        AnchorPane parentListInfor = (AnchorPane) contactLayout.getChildren().get(1);
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

        if(vi){
            contentQuestion.setText(question.getViContent());
        }else{
            contentQuestion.setText(question.getEnContent());
        }

        AnswerContact answerContact = new AnswerContact();

        answerContact.setSubAnswerID(question.getQuestionID());
        answerContact.setOrd(question.getOrd());



        name.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            String fileName = question.getType()+ "_" + question.getOrd();
            File contactData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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
                }else{

                    answerContact.setName(name.getText());

                }
            }else{
                nameAnnoucement.setVisible(false);
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
            File contactData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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

                }else{
                    if(email.getText().equals("")){
                        emailAnnoucement.setText("(*) Email không được để trống");
                    } else{
                        emailAnnoucement.setText("(*) Email không hợp lệ");
                    }
                    emailAnnoucement.setVisible(true);
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
            File contactData = new File("src/main/java/Data/SubAnswer/" + fileName +".json");
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

                }else{
                    if(phone.getText().equals("")){
                        phoneAnnoucement.setText("(*) số điện thoại không được để trống");
                    } else{
                        phoneAnnoucement.setText("(*) số điện thoại không hợp lệ");
                    }
                    phoneAnnoucement.setVisible(true);
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

        return contactLayout;
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnswerService answerService = new AnswerService();

        //create timeline for waiting video
        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(100), event -> {
            second--;
            System.out.println(second);
            if(second == 0){
                time.stop();
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(VideoController.getParent()));
                stage.show();
            }
        });

        time.getKeyFrames().add(frame);
        time.playFromStart();

        //mapping Data
        surveyService = new SurveyService();
        Survey survey = surveyService.getSurvey();

        contentSurvey.setText(survey.getContentSurvey());
        for(Question question:survey.getListQuestion()){
            if (question.getType().equals("CSAT")) {
                listQuestions.getChildren().add(createCSATLayout(question, true));
            } else if(question.getType().equals("NPS")){
                listQuestions.getChildren().add(createNPSLayout(question, true));
            } else if(question.getType().equals("MULTIPLE_CHOICE")){
                listQuestions.getChildren().add(createMutipleChoiceLayout(question, true));
            }else if(question.getType().equals("SINGLE_CHOICE")){
                listQuestions.getChildren().add(createSingleChoiceLayout(question, true));
            }else if(question.getType().equals("FLX")){
                listQuestions.getChildren().add(createFLXLayout(question, true));
            }else if(question.getType().equals("CES")){
                listQuestions.getChildren().add(createCESLayout(question, true));
            }else if(question.getType().equals("CONTACT")){
                listQuestions.getChildren().add(createContactLayout(question, true));
            }else if(question.getType().equals("OPEN")){
                listQuestions.getChildren().add(createOpenLayout(question, true));
            }else if(question.getType().equals("STAR")){
                listQuestions.getChildren().add(createStarLayout(question, true));
            }
        }

        //set Event for buttons
        sendingSurvey.setOnAction(e->{
            AnswerTotal answerTotal = new AnswerTotal();

            try{
                answerTotal.setAnswerTotalID(answerService.getAnswerTotalID());
            }catch (IOException er){
                System.out.println("avas");
            }

            ArrayList<SubAnswer> listAnswer = new ArrayList<>();
            String path = "src/main/java/Data/SubAnswer";
            File parentListData = new File(path);
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
                setScene(CThanksController.getParent(), e);
                File[] listDatafile = parentListData.listFiles();
                for(int i = 0; i <listAnswer.size(); i++){
                    listDatafile[i].delete();
                }
            }else{
                FileMethod.saveFile("src/main/java/Data/Answer/", "Answer.json", answerTotal);
            }
        });

        back.setOnAction(e->{
            setScene(QIndexController.getParent(), e);
            time.stop();
        });

        //when customer touch on the scene, second set to 10 and recount
        back.getParent().getParent().setOnMouseClicked(e->{
            second = 10;
        });

        //testing
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getParent()));
        primaryStage.show();
    }
}
