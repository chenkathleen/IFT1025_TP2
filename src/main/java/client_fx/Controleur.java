package client_fx;

import server.models.Course;

import java.util.ArrayList;

public class Controleur {
    private Modele modele;
    private Vue vue;

    public Controleur(Modele m, Vue v) {
        this.modele = m;
        this.vue = v;

        this.vue.getChargerBouton().setOnAction((action) -> {
            try {
                String selectedSession = vue.getSessionChoices().getValue();
                ArrayList<Course> courseList = modele.getCourseList("CHARGER " + selectedSession);
                //vue.updateText(courseList.toString());
                vue.updateClassTable(courseList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        this.vue.getEnvoyerBouton().setOnAction((action) -> {
            try {
                String prenom = vue.getPrenomField().getText();
                String nom = vue.getNomField().getText();
                String email = vue.getEmailField().getText();
                String matricule = vue.getMatriculeField().getText();
                Course selectedCourse = vue.getClassViewTable().getSelectionModel().getSelectedItem();

                String message = modele.registerStudent(prenom, nom, email, matricule, selectedCourse);
                if (message.equals("Success")) {
                    vue.clearRegistrationForm();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

}
