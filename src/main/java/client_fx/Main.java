package client_fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Démarre le client qui gère l'interface graphique pour le système d'inscription des cours de l'UDEM.
 */
public class Main extends Application {
    /**
     * Crée le lanceur du client interface graphique.
     */
    public Main() {
    }

    /**
     * Démarre l'interface graphique.
     * Relie modèle, vue et controlleur de l'application.
     *
     * @param stage la fenêtre de l'application
     */
    @Override
    public void start(Stage stage) {
        Modele leModele = new Modele();
        Vue laVue = new Vue();
        Controleur leControleur = new Controleur(leModele, laVue);

        Scene scene = new Scene(laVue, 600, 500);

        stage.setScene(scene);
        stage.setTitle("Inscription UdeM");
        stage.show();

    }

    /**
     * Démarre l'application.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}
