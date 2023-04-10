package client_simple;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Vue {
    private String currentScreen = "SESSION";
    private final Scanner sc = new Scanner(System.in);
    private Map<String, String> registrationUserInput = new HashMap<>();
    public Vue() {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        showSessionScreen();
    }

    public Scanner getSc() {
        return sc;
    }
    public String getCurrentScreen() {
        return currentScreen;
    }
    public Map<String, String> getRegistrationUserInput(){
        return registrationUserInput;
    }
    public void showSessionScreen() {
        currentScreen = "SESSION";
        System.out.println(
                "Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:" +
                        "\n" + "1. Automne" + "\n" + "2. Hiver" + "\n" + "3. Ete"
        );
    }
    public void showNextChoices() {
        currentScreen = "NEXTCHOICES";
        System.out.println(
                "Choix" + "\n" + "1. Consulter les cours offerts pour une autre session" +
                        "\n" + "2. Inscription à un cours"
        );
    }
    public void showRegistrationScreen() {
        currentScreen = "REGISTRATION";
        registrationUserInput.clear();

        System.out.print("Veuillez saisir votre prénom: ");
        String prenom = sc.nextLine();
        System.out.print("Veuillez saisir votre nom: ");
        String nom = sc.nextLine();
        System.out.print("Veuillez saisir votre email: ");
        String email = sc.nextLine();
        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = sc.nextLine();
        System.out.print("Veuillez saisir le code du cours: ");
        String codeDuCours = sc.nextLine();

        registrationUserInput.put("prenom", prenom);
        registrationUserInput.put("nom", nom);
        registrationUserInput.put("email", email);
        registrationUserInput.put("matricule", matricule);
        registrationUserInput.put("codeDuCours", codeDuCours);
    }
    public void printMessage(String message) {
        System.out.println(message);
    }
}
