package client_fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import server.models.Course;
import java.util.List;
import static javafx.collections.FXCollections.observableList;

/**
 * L'affichage graphique de l'application.
 * Inclut un écran qui permet d'afficher les cours disponibles pour une session donnée, ainsi qu'un écran qui envoie
 * un formulaire d'inscription et effectue l'inscription.
 */
public class Vue extends HBox {

    private TableView<Course> classViewTable = new TableView();
    private ChoiceBox<String> sessionChoices = new ChoiceBox();
    private Button chargerBouton = new Button("Charger");
    private Button envoyerBouton = new Button("Envoyer");
    private TextField prenomField = new TextField();
    private TextField nomField = new TextField();
    private TextField emailField = new TextField();
    private TextField matriculeField = new TextField();
    private Dialog<String> errorScreen = new Dialog();
    private Dialog<String> confirmationScreen = new Dialog();

    /**
     * Crée l'affichage graphique.
     * Instantie la structure des deux écrans, incluant la liste de sessions possibles, et les champs du formulaire
     * d'inscription.
     */
    public Vue() {
        this.setBackground(new Background (new BackgroundFill(Color.LINEN, CornerRadii.EMPTY, Insets.EMPTY)));

        // ********* Écran des cours *********
        BorderPane classViewScreen = new BorderPane();

        // Titre
        Text classViewScreenTitle = new Text("Liste des cours");
        classViewScreenTitle.setFont(new Font(16));
        classViewScreen.setTop(classViewScreenTitle);
        classViewScreen.setAlignment(classViewScreenTitle, Pos.CENTER);
        classViewScreen.setMargin(classViewScreenTitle, new Insets(5, 0,5,0));

        // Tableau avec information des cours
        TableColumn<Course,String> courseCodeCol = new TableColumn<Course,String>("Code");
        courseCodeCol.setCellValueFactory(new PropertyValueFactory("code"));
        TableColumn<Course,String> courseNameCol = new TableColumn<Course,String>("Cours");
        courseNameCol.setCellValueFactory(new PropertyValueFactory("name"));

        classViewTable.getColumns().setAll(courseCodeCol, courseNameCol);
        classViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        classViewScreen.setCenter(classViewTable);
        classViewScreen.setMargin(classViewTable, new Insets(0, 10,0,10));

        // Boutons pour choisir la session et charger
        HBox classViewScreenBottom = new HBox();
        sessionChoices.getItems().addAll("Automne", "Ete", "Hiver");
        sessionChoices.setValue("Automne");

        classViewScreenBottom.getChildren().addAll(sessionChoices, chargerBouton);
        classViewScreenBottom.setAlignment(Pos.CENTER);
        classViewScreenBottom.setSpacing(40);
        classViewScreenBottom.setMargin(sessionChoices, new Insets(5, 0,5,0));
        classViewScreenBottom.setMargin(chargerBouton, new Insets(5, 0,5,0));

        classViewScreen.setBottom(classViewScreenBottom);

        // ********* Écran d'inscription *********
        VBox inscriptionScreen = new VBox();
        inscriptionScreen.setAlignment(Pos.TOP_CENTER);
        inscriptionScreen.setSpacing(10);

        // Titre
        Text inscriptionScreenTitle = new Text("Formulaire d'inscription");
        inscriptionScreenTitle.setFont(new Font(16));
        inscriptionScreen.getChildren().add(inscriptionScreenTitle);
        inscriptionScreen.setMargin(inscriptionScreenTitle, new Insets(15, 0,15,0));

        // Formulaire
        GridPane RegistrationForm = new GridPane();
        Label prenomFieldLabel = new Label("Prenom");
        Label nomFieldLabel = new Label("Nom");
        Label emailFieldLabel = new Label("Email");
        Label matriculeFieldLabel = new Label("Matricule");

        RegistrationForm.addRow(0, prenomFieldLabel, prenomField);
        RegistrationForm.addRow(1, nomFieldLabel, nomField);
        RegistrationForm.addRow(2, emailFieldLabel, emailField);
        RegistrationForm.addRow(3, matriculeFieldLabel, matriculeField);

        RegistrationForm.setHgap(10);
        RegistrationForm.setVgap(5);

        inscriptionScreen.getChildren().add(RegistrationForm);
        inscriptionScreen.getChildren().add(envoyerBouton);
        inscriptionScreen.setMargin(envoyerBouton, new Insets(15, 0,15,0));

        // ******** Ajouter les deux écrans **********
        this.getChildren().add(classViewScreen);
        this.getChildren().add(new Separator());
        this.getChildren().add(inscriptionScreen);
        HBox.setHgrow(classViewScreen, Priority.ALWAYS);
        HBox.setHgrow(inscriptionScreen, Priority.ALWAYS);

        // ********* Boîtes de dialogue *************
        ButtonType errorButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        errorScreen.setTitle("Erreur");
        errorScreen.getDialogPane().getButtonTypes().add(errorButton);

        ButtonType successButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        confirmationScreen.setTitle("Confirmation");
        confirmationScreen.getDialogPane().getButtonTypes().add(successButton);
    }

    /**
     * Récupère l'écran qui affiche les messages erreur.
     *
     * @return l'écran qui affiche les messages erreur.
     */
    public Dialog<String> getErrorScreen() {
        return errorScreen;
    }

    /**
     * Récupère l'écran qui affiche le message de confirmation si une inscription a réussi.
     *
     * @return l'écran qui affiche le message de confirmation de l'inscription.
     */
    public Dialog<String> getConfirmationScreen() {
        return confirmationScreen;
    }

    /**
     * Récupère le bouton qui permet d'accéder au liste de cours pour une session donnée.
     *
     * @return le bouton qui charge la liste des cours.
     */
    public Button getChargerBouton(){
        return chargerBouton;
    }

    /**
     * Récupère le bouton qui envoie le formulaire d'inscription au serveur.
     *
     * @return le bouton qui envoie le formulaire d'inscription au serveur.
     */
    public Button getEnvoyerBouton(){
        return envoyerBouton;
    }

    /**
     * Récupère la liste déroulante qui contient les sessions reconnues.
     *
     * @return la liste déroulante des sessions reconnues.
     */
    public ChoiceBox<String> getSessionChoices(){
        return sessionChoices;
    }

    /**
     * Récupère le tableau qui affiche les cours pour la session choisie.
     *
     * @return le tableau qui affiche les cours pour la session choisie.
     */
    public TableView<Course> getClassViewTable() {
        return classViewTable;
    }

    /**
     * Récupère le champ pour le prénom dans le formulaire d'inscription.
     *
     * @return le champ pour le prénom dans le formulaire d'inscription.
     */
    public TextField getPrenomField() {
        return prenomField;
    }

    /**
     * Récupère le champ pour le nom dans le formulaire d'inscription.
     *
     * @return le champ pour le nom dans le formulaire d'inscription.
     */
    public TextField getNomField() {
        return nomField;
    }

    /**
     * Récupère le champ pour l'email dans le formulaire d'inscription.
     *
     * @return le champ pour l'email dans le formulaire d'inscription.
     */
    public TextField getEmailField() {
        return emailField;
    }

    /**
     * Récupère le champ pour la matricule dans le formulaire d'inscription.
     *
     * @return le champ pour la matricule dans le formulaire d'inscription.
     */
    public TextField getMatriculeField() {
        return matriculeField;
    }

    /**
     * Mettre à jour le tableau de la liste de cours pour une session donneé.
     *
     * @param courseList liste de cours à afficher
     */

    public void updateClassTable(List<Course> courseList) {
        classViewTable.setItems(observableList(courseList));
    }

    /**
     * Effacer le contenu des champs du formulaire d'inscription.
     */
    public void clearRegistrationForm() {
        prenomField.clear();
        nomField.clear();
        emailField.clear();
        matriculeField.clear();
    }
}
