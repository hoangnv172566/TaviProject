package Models.Survey;

import java.io.Serializable;
import java.util.ArrayList;

public class Survey implements Serializable {
    //infor for mapping other object
    private long idSurvey;
    private long companyID;

    //dislaying these infor in scene
    private String contentSurvey;
    private String kindOfSurvey;
    private ArrayList<Question> listQuestion;

    public long getIdSurvey() {
        return idSurvey;
    }

    public void setIdSurvey(long idSurvey) {
        this.idSurvey = idSurvey;
    }

    public long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(long companyID) {
        this.companyID = companyID;
    }

    public String getContentSurvey() {
        return contentSurvey;
    }

    public void setContentSurvey(String contentSurvey) {
        this.contentSurvey = contentSurvey;
    }

    public String getKindOfSurvey() {
        return kindOfSurvey;
    }

    public void setKindOfSurvey(String kindOfSurvey) {
        this.kindOfSurvey = kindOfSurvey;
    }

    public ArrayList<Question> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(ArrayList<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

//    public  Survey(){
//        contentSurvey = null;
//        kindOfSurvey = null;
//        listQuestion = null;
//        status = new SimpleStringProperty();
//        status.set("active");
//    }
//
//    public LongProperty idSurveyProperty(){
//        if(idSurvey == null){
//            idSurvey = new SimpleLongProperty(0);
//        }
//        return idSurvey;
//    }
//
//    public void setIdSurvey(long value){
//        idSurveyProperty().set(value);
//    }
//    public long getIdSurvey(){
//        return idSurveyProperty().get();
//    }
//
//    public LongProperty companyIDProperty(){
//        if(companyID == null){
//            companyID = new SimpleLongProperty(0);
//        }
//        return companyID;
//    }
//
//    public void setCompanyID(long value){
//        companyIDProperty().set(value);
//    }
//
//    public long getCompanyID(){
//        return companyIDProperty().get();
//    }
//
//    public void setContentSurvey(String data){
//        contentSurveyProperty().set(data);
//    }
//    public String getContentSurvey(){
//        return contentSurveyProperty().get();
//    }
//    public StringProperty contentSurveyProperty(){
//        if(contentSurvey == null){
//            contentSurvey = new SimpleStringProperty();
//        }
//        return contentSurvey;
//    }
//
//    public ArrayList<Question> getListQuestion(){
//        return this.listQuestion;
//    }
//    public void setListQuestion(ArrayList<Question> data){
//        if(listQuestion == null){
//            listQuestion = new ArrayList<>();
//            listQuestion.addAll(data);
//        }
//    }
//
//    public String getKindOfSurvey(){
//        return kindOfSurveyProperty().get();
//    }
//    public void setKindOfSurvey(String data){
//        kindOfSurveyProperty().set(data);
//    }
//    public StringProperty kindOfSurveyProperty() {
//        if(kindOfSurvey == null){
//            kindOfSurvey = new SimpleStringProperty();
//        }
//        return kindOfSurvey;
//
//    }
//    public String getstatus(){
//        return statusProperty().get();
//    }
//    public StringProperty statusProperty(){
//        return status;
//    }


    @Override
    public String toString() {
        return "Survey{" +
                "idSurvey=" + idSurvey +
                ", companyID=" + companyID +
                ", contentSurvey='" + contentSurvey + '\'' +
                ", kindOfSurvey='" + kindOfSurvey + '\'' +
                ", listQuestion=" + listQuestion +
                '}';
    }
}
