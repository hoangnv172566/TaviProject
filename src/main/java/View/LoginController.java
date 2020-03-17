package View;

public class LoginController{
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    private Basic basic;
//    private Stage stage = new Stage();
//    private Scene loginScene;
//    private Scene resisterScene;
//    private AnchorPane dynamicalAnchorPane;
//
//    @FXML
//    private AnchorPane mainAnchorPane;
//
//    @FXML
//    private AnchorPane loginPane;
//
//    @FXML
//    private Button login;
//    @FXML
//    private Button resister;
//    @FXML
//    private Hyperlink fogettingPass;
//
//    @FXML
//    private TextField userName;
//    @FXML
//    private PasswordField password;
//    @FXML
//    private RadioButton userRole;
//    @FXML
//    private RadioButton enterpriseRole;
//    @FXML
//    private RadioButton anonymous;
//
//    public void initialize(URL location, ResourceBundle resources) {
//        //initiation
//        ToggleGroup toggleGroup = new ToggleGroup();
//        userRole.setToggleGroup(toggleGroup);
//        enterpriseRole.setToggleGroup(toggleGroup);
//        anonymous.setToggleGroup(toggleGroup);
//
//        RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
//        System.out.println(radioButton);
//
//        //event
//
//        login.setOnAction(this::loadIndex);
//        resister.setOnAction(this::loadResister);
//
//    }
//
//    public void loadIndex(ActionEvent e){
//
//        setScenne(CIndexController.display(), e);
//
//    }
//
//
//
//    public void loadResister(ActionEvent event) {
//
//        try{
//            Parent root = FXMLLoader.load(ResisterController.class.getResource("Resister.fxml"));
//            Scene scene = new Scene(root);
//            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
//            window.setScene(scene);
//            window.show();
//
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void setScenne(Parent root, ActionEvent event){
//        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.centerOnScreen();
//        stage.show();
//    }
//    public static Parent display(){
//        try{
//            return FXMLLoader.load(LoginController.class.getResource("Login.fxml"));
//        } catch(IOException e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene(display()));
//        primaryStage.setResizable(false);
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.show();
//
//    }
}
