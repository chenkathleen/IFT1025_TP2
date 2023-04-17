package client_fx;

import client_simple.Modele;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
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

    public static void main(String[] args) {
        launch(args);
    }
}
