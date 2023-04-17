package client_fx;

import client_simple.Modele;
import server.models.Course;
import server.models.RegistrationForm;
import java.util.ArrayList;
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
                vue.updateClassTable(courseList);
            } catch (Exception ex) {
                vue.getErrorScreen().setContentText(ex.getMessage());
                vue.getErrorScreen().showAndWait();
            }

        });

        this.vue.getEnvoyerBouton().setOnAction((action) -> {
            List<String> inputErrorList = new ArrayList<>();
            String prenom = vue.getPrenomField().getText();
            String nom = vue.getNomField().getText();
            String email = vue.getEmailField().getText();
            String matricule = vue.getMatriculeField().getText();
            Course selectedCourse = vue.getClassViewTable().getSelectionModel().getSelectedItem();
            RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, selectedCourse);

            if (vue.getClassViewTable().getSelectionModel().getSelectedItem() == null) {
                inputErrorList.add("SVP veuillez sélectionner un cours.");
            }
            if (prenom.length() == 0 || nom.length() == 0 || email.length() == 0 || matricule.length() == 0) {
                inputErrorList.add("Le formulaire est incomplet.");
            }
            if (email.length() != 0 && !email.matches("\\S+@\\S+\\.\\S+")) {
                inputErrorList.add("Le format du email saisi n'est pas reconnu.");
            }
            if (matricule.length() !=0 && !matricule.matches("^\\d{8}$")) {
                inputErrorList.add("Le format du matricule saisi n'est pas reconnu.");
            }

            if (inputErrorList.size() > 0) {
                String errorOutput = "";
                for (String err: inputErrorList) {
                    errorOutput = errorOutput + err + "\n";
                }
                vue.getErrorScreen().setContentText(errorOutput);
                vue.getErrorScreen().showAndWait();
            } else {
                try {
                    String message = modele.registerStudent(registrationForm);
                    if (message != null) {
                        vue.getConfirmationScreen().setContentText(message);
                        vue.getConfirmationScreen().showAndWait();
                        vue.clearRegistrationForm();
                    } else {
                        vue.getConfirmationScreen().setContentText("Inscription échouée.");
                        vue.getConfirmationScreen().showAndWait();
                    }
                } catch (Exception e) {
                    vue.getErrorScreen().setContentText(e.getMessage());
                    vue.getErrorScreen().showAndWait();
                }
            }

        });

    }

}
