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
        String selectedSession = arg;
        System.out.println("En train de charger les cours pour la session " + selectedSession + "...");
        List <Course> courseList = new ArrayList<>();
        List <Exception> errorList = new ArrayList<>();

        // try (BufferedReader reader = new BufferedReader(new FileReader("cours.txt"))){
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/server/data/cours.txt"))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String code = parts[0];
                String name = parts[1];
                String session = parts[2];
                if (session.equals(selectedSession)) {
                    courseList.add(new Course(name, code, session));
                }
            }
        } catch (IOException ex) {
            String fileReadErrorMessage = "Erreur lors de la lecture du fichier cours.txt.";
            System.out.println(fileReadErrorMessage);
            errorList.add(new IOException(fileReadErrorMessage));
        }

        try {
            if (errorList.size() == 0) {
                objectOutputStream.writeObject(courseList);
                objectOutputStream.flush();
            } else {
                objectOutputStream.writeObject(null);
                objectOutputStream.flush();
            }
        } catch (IOException ex) {
            String streamWriteErrorMessage = "Erreur lors de l'écriture de la liste des cours dans le flux.";
            System.out.println(streamWriteErrorMessage);
            errorList.add(new IOException(streamWriteErrorMessage));
        }

        try {
            objectOutputStream.writeObject(errorList);
            objectOutputStream.flush();
        } catch (IOException ex) {
            System.out.println("Erreur lors de l'écriture de la liste d'erreurs dans le flux.");
        }

        if (errorList.size() == 0) {
            System.out.println("Réussite!");
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        System.out.println("En train d'effectuer une nouvelle inscription...");
        RegistrationForm registrationForm = null;
        String nouveau_inscription = null;
        String message_inscription = null;
        List <Exception> errorList = new ArrayList<>();
        try {
            registrationForm = (RegistrationForm) objectInputStream.readObject();
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur lors de la lecture de l'objet RegistrationForm.");
            errorList.add(new ClassNotFoundException("Erreur lors de la lecture de l'objet RegistrationForm."));
        } catch (IOException ex) {
            System.out.println("Erreur lors de la lecture de l'objet RegistrationForm.");
            errorList.add(new IOException("Erreur lors de la lecture de l'objet RegistrationForm."));
        }

        try {
            if (
                    registrationForm != null && registrationForm.getCourse() != null
                            && registrationForm.getMatricule().length() != 0 && registrationForm.getPrenom().length() != 0
                            && registrationForm.getNom().length() != 0 && registrationForm.getEmail().length() != 0
            ) {
                Course course = registrationForm.getCourse();
                String session = course.getSession();
                String code = course.getCode();
                String matricule = registrationForm.getMatricule();
                String prenom = registrationForm.getPrenom();
                String nom = registrationForm.getNom();
                String email = registrationForm.getEmail();

                nouveau_inscription = session + "\t" + code + "\t" + matricule + "\t" + prenom + "\t" + nom + "\t" + email;
                message_inscription = "Félicitations! Inscription réussie de " + prenom + " " + nom + " au cours " + code + ".";
            } else {
                throw new IllegalArgumentException("Erreur: le formulaire d'inscription n'est pas complet.");
            }

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            errorList.add(ex);
        }

        System.out.println(nouveau_inscription);
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter("inscription.txt", true))){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/server/data/inscription.txt", true))){
            if (nouveau_inscription != null) {
                writer.newLine();
                writer.write(nouveau_inscription);
            }
        }
        catch (IOException ex) {
            String recordRegistrationErrorMessage = "Erreur lors de l'écriture dans le fichier inscription.txt.";
            System.out.println(recordRegistrationErrorMessage);
            errorList.add(new IOException(recordRegistrationErrorMessage));
        }

        try {
            if (errorList.size() == 0) {
                objectOutputStream.writeObject(message_inscription);
                objectOutputStream.flush();
            } else {
                objectOutputStream.writeObject(null);
                objectOutputStream.flush();
            }
        } catch (IOException ex) {
            String streamWriteErrorMessage = "Erreur lors de l'écriture dans le flux de sortie.";
            System.out.println(streamWriteErrorMessage);
            errorList.add(new IOException(streamWriteErrorMessage));
        }

        try {
            objectOutputStream.writeObject(errorList);
            objectOutputStream.flush();
        } catch (IOException ex) {
            System.out.println("Erreur lors de l'écriture de la liste d'erreurs dans le flux.");
        }

        if (errorList.size() == 0) {
            System.out.println("Réussite!");
        }

    }
}

