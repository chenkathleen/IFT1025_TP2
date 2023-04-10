package client_fx;

import client_simple.Modele;
import server.models.Course;
import server.models.RegistrationForm;

import java.util.List;

public class Controleur {
    private Modele modele;
    private Vue vue;

    public Controleur(Modele m, Vue v) {
        this.modele = m;
        this.vue = v;

        this.vue.getChargerBouton().setOnAction((action) -> {
            try {
                String selectedSession = vue.getSessionChoices().getValue();
                List<Course> courseList = modele.retrieveSessionCourses("CHARGER " + selectedSession);
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
                RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, selectedCourse);

                String message = modele.registerStudent(registrationForm);
                if (message.equals("Success")) {
                    vue.clearRegistrationForm();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

}
