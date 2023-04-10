package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            System.out.println(line);
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        String sessionFiltre = arg;
        System.out.println("loading courses");

        //try (BufferedReader reader = new BufferedReader(new FileReader("cours.txt"))){
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/server/data/cours.txt"))){
            ArrayList <Course> listeDeCours = new ArrayList<>();
            Map<String, Course> courseDict = new HashMap<>();

            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String code = parts[0];
                String name = parts[1];
                String session = parts[2];
                if (sessionFiltre.equals(session)) {
                    listeDeCours.add(new Course(name, code, session));
                    courseDict.put(code, new Course(name, code, session));
                }
            }

            objectOutputStream.writeObject(listeDeCours);
            // objectOutputStream.writeObject(courseDict); //TEMP: simple client needs dict


        } catch (IOException ex) {
            System.out.println("issue with reading cours.txt");
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    //"./src/main/java/server/data/inscription.txt"
    public void handleRegistration() {

        //try (BufferedWriter writer = new BufferedWriter(new FileWriter("inscription.txt", true))){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/server/data/inscription.txt", true))){
            RegistrationForm registrationForm = (RegistrationForm) objectInputStream.readObject();
            Course course = registrationForm.getCourse();
            String session = course.getSession();
            String code = course.getCode();
            String matricule = registrationForm.getMatricule();
            String prenom = registrationForm.getPrenom();
            String nom = registrationForm.getNom();
            String email = registrationForm.getEmail();

            String nouveau_inscription = session + "\t" + code + "\t" + matricule + "\t" + prenom + "\t" + nom + "\t" + email;
            writer.newLine();
            writer.write(nouveau_inscription);
            objectOutputStream.writeObject("Success");

        } catch (Exception ex) {
        }

    }
}

