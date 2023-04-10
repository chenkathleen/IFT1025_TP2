package client_fx;


import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import server.models.Course;
import server.models.RegistrationForm;

import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

public class Vue extends HBox {

    private VBox inscriptionScreen = new VBox();
    private Text inscriptionScreenTitle = new Text("Formulaire d'inscription");

    private BorderPane classViewScreen = new BorderPane();
    private Text classViewScreenTitle = new Text("Liste des cours");
    private Text classViewText = new Text("Placeholder");
    private TableView<Course> classViewTable = new TableView();
    //private ObservableList<Course> courseList;
    private ChoiceBox<String> sessionChoices = new ChoiceBox();
    private Button chargerBouton = new Button("Charger");
    private Button envoyerBouton = new Button("Envoyer");
    private TextField prenomField = new TextField();
    private TextField nomField = new TextField();
    private TextField emailField = new TextField();
    private TextField matriculeField = new TextField();


    public Vue() {


        classViewScreen.setTop(classViewScreenTitle);

        TableColumn<Course,String> courseCodeCol = new TableColumn<Course,String>("Code");
        courseCodeCol.setCellValueFactory(new PropertyValueFactory("code"));
        TableColumn<Course,String> courseNameCol = new TableColumn<Course,String>("Cours");
        courseNameCol.setCellValueFactory(new PropertyValueFactory("name"));

        classViewTable.getColumns().setAll(courseCodeCol, courseNameCol);

        classViewScreen.setCenter(classViewTable);

        HBox classViewScreenBottom = new HBox();
        sessionChoices.getItems().addAll("Automne", "Ete", "Hiver");
        sessionChoices.setValue("Automne");

        classViewScreenBottom.getChildren().add(sessionChoices);
        classViewScreenBottom.getChildren().add(chargerBouton);

        classViewScreen.setBottom(classViewScreenBottom);

        inscriptionScreen.getChildren().add(inscriptionScreenTitle);

        GridPane RegistrationForm = new GridPane();
        Label prenomFieldLabel = new Label("Prenom");
        Label nomFieldLabel = new Label("Nom");
        Label emailFieldLabel = new Label("Email");
        Label matriculeFieldLabel = new Label("Matricule");

        RegistrationForm.addRow(0, prenomFieldLabel, prenomField);
        RegistrationForm.addRow(1, nomFieldLabel, nomField);
        RegistrationForm.addRow(2, emailFieldLabel, emailField);
        RegistrationForm.addRow(3, matriculeFieldLabel, matriculeField);
        inscriptionScreen.getChildren().add(RegistrationForm);
        inscriptionScreen.getChildren().add(envoyerBouton);


        this.getChildren().add(classViewScreen);
        this.getChildren().add(new Separator());
        this.getChildren().add(inscriptionScreen);


    }

    public void updateText(String nouvelleValeur) {
        classViewText.setText(nouvelleValeur);
    }
    public void updateClassTable(List<Course> courseList) {
        classViewTable.setItems(observableList(courseList));
    }

    public Button getChargerBouton(){
        return chargerBouton;
    }
    public Button getEnvoyerBouton(){
        return envoyerBouton;
    }
    public ChoiceBox<String> getSessionChoices(){
        return sessionChoices;
    }
    public void clearRegistrationForm() {
        prenomField.clear();
        nomField.clear();
        emailField.clear();
        matriculeField.clear();
    }

    public TableView<Course> getClassViewTable() {
        return classViewTable;
    }

    public TextField getPrenomField() {
        return prenomField;
    }
    public TextField getNomField() {
        return nomField;
    }
    public TextField getEmailField() {
        return emailField;
    }

    public TextField getMatriculeField() {
        return matriculeField;
    }
}
