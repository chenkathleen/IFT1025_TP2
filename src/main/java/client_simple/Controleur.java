package client_simple;

import server.models.Course;
import server.models.RegistrationForm;

import java.util.ArrayList;
import java.util.List;

public class Controleur {
        private Modele modele;
        private Vue vue;
        private List<EventHandler> handlers = new ArrayList();
        private List<Course> currentCourseList = new ArrayList<>();

        public Controleur(Modele m, Vue v)  {
                this.modele = m;
                this.vue = v;
                addEventHandler((screen, arg) -> {
                        if (screen.equals("SESSION")) {
                                if (arg.equals("1")) {
                                        currentCourseList = modele.retrieveSessionCourses("CHARGER Automne");
                                        vue.printMessage("Les cours offerts pendant la session d'automne sont:");
                                } else if (arg.equals("2")) {
                                        currentCourseList = modele.retrieveSessionCourses("CHARGER Hiver");
                                        vue.printMessage("Les cours offerts pendant la session d'hiver sont:");
                                } else if (arg.equals("3")) {
                                        currentCourseList = modele.retrieveSessionCourses("CHARGER Ete");
                                        vue.printMessage("Les cours offerts pendant la session d'été sont:");
                                } else {
                                        vue.printMessage("Erreur: votre choix n'est pas reconnu");
                                        vue.showSessionScreen();
                                }
                                if (arg.equals("1") || arg.equals("2") || arg.equals("3")) {
                                        vue.printMessage(formatCourseList(currentCourseList));
                                        vue.showNextChoices();
                                }
                        }
                });

                addEventHandler((screen, arg) -> {
                        if (screen.equals("NEXTCHOICES")) {
                                if (arg.equals("1")) {
                                        vue.showSessionScreen();
                                } else if (arg.equals("2")) {
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
                                        RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, selectedCourse);

                                        vue.printMessage(modele.registerStudent(registrationForm));
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
