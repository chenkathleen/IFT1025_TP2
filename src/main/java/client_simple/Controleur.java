package client_simple;

import server.models.Course;
import server.models.RegistrationForm;

import java.util.ArrayList;
import java.util.List;

public class Controleur {
        private Modele modele;
        private Vue vue;
        private List<EventHandler> handlers = new ArrayList<>();
        private List<Course> currentCourseList = new ArrayList<>();

        public Controleur(Modele m, Vue v)  {
                this.modele = m;
                this.vue = v;
                addEventHandler((screen, arg) -> {
                        if (screen.equals("SESSION")) {
                                if (arg.equals("1") || arg.equals("2") || arg.equals("3")) {
                                        try {
                                                String session = null;
                                                if (arg.equals("1")) {
                                                        session = "Automne";
                                                } else if (arg.equals("2")) {
                                                        session = "Hiver";
                                                } else if (arg.equals("3")) {
                                                        session = "Ete";
                                                }
                                                currentCourseList = modele.retrieveSessionCourses("CHARGER " + session);
                                                vue.printMessage("Les cours offerts pendant la session " + session + " sont:");
                                                vue.printMessage(formatCourseList(currentCourseList));
                                        } catch (Exception ex){
                                                System.out.println(ex.getMessage());
                                                // ex.printStackTrace();
                                        }
                                        vue.showNextChoices();
                                } else {
                                        vue.printMessage("Erreur: votre choix n'est pas reconnu.");
                                        vue.showSessionScreen();
                                }
                        }
                });

                addEventHandler((screen, arg) -> {
                        if (screen.equals("NEXTCHOICES")) {
                                if (arg.equals("1")) {
                                        vue.showSessionScreen();
                                } else if (arg.equals("2")) {
                                        List<String> inputErrorList = new ArrayList<>();
                                        vue.showRegistrationScreen();
                                        String prenom = vue.getRegistrationUserInput().get("prenom");
                                        String nom = vue.getRegistrationUserInput().get("nom");
                                        String email = vue.getRegistrationUserInput().get("email");
                                        String matricule = vue.getRegistrationUserInput().get("matricule");
                                        String codeDuCours = vue.getRegistrationUserInput().get("codeDuCours");

                                        Course selectedCourse = null;
                                        for (Course course : currentCourseList) {
                                                if (course.getCode().equals(codeDuCours)) {
                                                        selectedCourse = course;
                                                }
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
                                        if (selectedCourse == null) {
                                                inputErrorList.add("Le cours saisi n'est pas parmi les cours disponibles pour cette session.");
                                        }
                                        if (inputErrorList.size() != 0) {
                                                for (String error: inputErrorList) {
                                                        System.out.println(error);
                                                }

                                        } else {
                                                try {
                                                        RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, selectedCourse);
                                                        String message = modele.registerStudent(registrationForm);
                                                        if (message != null) {
                                                                vue.printMessage(modele.registerStudent(registrationForm));
                                                        } else {
                                                                vue.printMessage("Inscription échouée.");
                                                        }


                                                } catch (Exception ex){
                                                        System.out.println(ex.getMessage());
                                                }

                                        }
                                        vue.showNextChoices();
                                }
                                else {
                                        vue.printMessage("Erreur: votre choix n'est pas reconnu.");
                                        vue.showNextChoices();
                                }
                        }
                });
                listen();
        }

        public void addEventHandler(EventHandler h) {
                this.handlers.add(h);
        }
        public void listen() {
                String arg;
                while ((arg = vue.getSc().nextLine()) != null) {
                        System.out.println("Votre choix: " + arg);
                        if (arg.equals("exit"))
                                break;
                        this.alertHandlers(vue.getCurrentScreen(), arg);
                }
        }
        private void alertHandlers(String screen, String arg) {
                for (EventHandler h : this.handlers) {
                        h.handle(screen, arg);
                }
        }
        private String formatCourseList(List<Course> courseList) {
                String formattedCourseList = "";
                int i = 0;
                for (Course course: courseList) {
                      i++;
                      formattedCourseList = formattedCourseList + i +
                              ". " + course.getCode() + "\t" + course.getName() + "\n";
                }
                return formattedCourseList;
        }
}
