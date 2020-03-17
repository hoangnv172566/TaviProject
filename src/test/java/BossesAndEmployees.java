import Models.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class BossesAndEmployees extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("bosses and Employees: working with Tables ");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 630, 250, Color.WHITE);

        // crete a grid pane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        root.setCenter(gridPane);

        //candidates lable
        Label candidatesLbl = new Label("Boss");
        GridPane.setHalignment(candidatesLbl, HPos.CENTER);
        gridPane.add(candidatesLbl, 0, 0);

        //List Of bosses

        ObservableList<Person> bosses = getPeople();
        final ListView<Person> leaderListView = new ListView<>(bosses);
        leaderListView.setPrefWidth(150);
        leaderListView.setMinWidth(200);
        leaderListView.setMaxWidth(200);
        leaderListView.setPrefHeight(Integer.MAX_VALUE);

        //display first and last name with tooltip using alias
        leaderListView.setCellFactory(listView -> {
            Tooltip tooltip = new Tooltip();
            ListCell<Person> cell = new ListCell<Person>(){
                @Override
                public void updateItem(Person item, boolean empty){
                    super.updateItem(item, empty);
                    if(item!= null){
                        setText(item.getFirstName() + " " + item.getLastName());
                        tooltip.setText(item.getAliasName());
                        setTooltip(tooltip);
                    }
                }
            };//ListCell
            return cell;
        });//setCellFactory

        gridPane.add(leaderListView, 0, 1);

        Label emplLbl  = new Label("Employees");
        gridPane.add(emplLbl, 2, 0);
        gridPane.setHalignment(emplLbl,HPos.CENTER);

        TableView<Person> employeeTableView = new TableView<>();
        employeeTableView.setEditable(true);
        employeeTableView.setPrefWidth(Integer.MAX_VALUE);

        ObservableList<Person> teamMember = FXCollections.observableArrayList();
        employeeTableView.setItems(teamMember);

        TableColumn<Person, String> aliasNameCol = new TableColumn<>("Alias");
        aliasNameCol.setCellValueFactory(new PropertyValueFactory<>("aliasName"));
        aliasNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Person, Person.MOOD_TYPES> moodCol = new TableColumn<>("Mood");
        moodCol.setCellValueFactory(new PropertyValueFactory<>("mood"));
        ObservableList<Person.MOOD_TYPES> moods = FXCollections.observableArrayList(Person.MOOD_TYPES.values());
        moodCol.setCellFactory(ComboBoxTableCell.forTableColumn(moods));
        moodCol.setPrefWidth(100);
        employeeTableView.getColumns().add(aliasNameCol);
        employeeTableView.getColumns().add(firstNameCol);
        employeeTableView.getColumns().add(lastNameCol);
        employeeTableView.getColumns().add(moodCol);
        gridPane.add(employeeTableView, 2, 1);

        //selection listening
        leaderListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(observable != null && observable.getValue() != null){
                teamMember.clear();
                teamMember.addAll(observable.getValue().employeesProperty());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private ObservableList<Person> getPeople(){
        ObservableList<Person> people = FXCollections.<Person>observableArrayList();

        Person docX = new Person("Professor X", "Charles", "Xavier", Person.MOOD_TYPES.Positive);
        docX.employeesProperty().add(new Person("Wolverine", "James", "Howlett", Person.MOOD_TYPES.Angry));
        docX.employeesProperty().add(new Person("Cyclops", "Scott", "Summers", Person.MOOD_TYPES.Happy));
        docX.employeesProperty().add(new Person("Storm", "Ororo", "Munroe", Person.MOOD_TYPES.Positive));

        Person magneto = new Person("Magneto", "Max", "Eisenhardt", Person.MOOD_TYPES.Sad);
        magneto.employeesProperty().add(new Person("Juggernault", "Cain", "Marko", Person.MOOD_TYPES.Angry));
        magneto.employeesProperty().add(new Person("Mystique", "Raven", "Darkholme", Person.MOOD_TYPES.Sad));
        magneto.employeesProperty().add(new Person("Sabretooth", "Victor", "Creed", Person.MOOD_TYPES.Angry));

        Person biker = new Person("Mountain Biker", "Jonatha", "Gennick", Person.MOOD_TYPES.Positive);
        biker.employeesProperty().add(new Person("MkHeck", "Mark", "Heckler", Person.MOOD_TYPES.Happy));
        biker.employeesProperty().add(new Person("Hansolo", "Mark", "Heckler", Person.MOOD_TYPES.Angry));
        biker.employeesProperty().add(new Person("Doc", "Noan", "Heckler", Person.MOOD_TYPES.Sad));
        biker.employeesProperty().add(new Person("Cosmonaul", "Sean", "Heckler", Person.MOOD_TYPES.Happy));
        biker.employeesProperty().add(new Person("CarlFX", "Mark", "Dea", Person.MOOD_TYPES.Positive));

        people.add(docX);
        people.add(magneto);
        people.add(biker);

        return people;
    }

}
