package client_fx;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Objet qui communique avec le serveur.
 * Envoie les requêtes de chargement des cours et d'inscription au serveur, et traite les résultats reçus du serveur.
 */
public class Modele {
    /**
     * Adresse IP auquel le client va se connecter.
     */
    public final static String HOST = "127.0.0.1";
    /**
     * Numéro de port auquel le client va se connecter.
     */
    public final static int PORT = 1337;

    /**
     * Crée un objet qui communique avec le serveur.
     */
    public Modele() {
    }

    /**
     * Récupère les cours pour une session spécifiée.
     * Envoie une requête au serveur pour récupérer les cours pour une session spécifiée.
     *
     * @param args arguments à fournir au serveur
     * @return liste de cours disponibles pour la session spécifiée
     * @throws Exception erreurs de communication avec le serveur, ou exceptions produites lors du traitement de la
     * requête par le serveur. Ces derniers incluent les erreurs de lecture du fichier cours.txt et les erreurs de
     * transmission des résultats de la requête du serveur au client.
     */
    public List<Course> retrieveSessionCourses(String args) throws Exception {
        List<Course> courseList = null;
        List<Exception> errorList = null;
        try (
                Socket cS = new Socket(HOST,PORT);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
        ) {
            os.writeObject(args);
            os.flush();

            courseList = (ArrayList <Course>) is.readObject();
            // courseList est null si et seulement si il y a une exception dans handleLoadCourses
            if (courseList == null) {
                errorList = (ArrayList <Exception>) is.readObject();
            }
        }

        catch (IOException ex) {
            throw new IOException("Erreur de communication avec le serveur.");
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Erreur de communication avec le serveur.");
        }

        if (errorList != null) {
            for (Exception ex : errorList) {
                throw ex;
            }
        }

        return courseList;
    }

    /**
     * Enregistre un étudiant pour un cours spécifié.
     * Envoie une requête au serveur pour enregistrer un étudiant pour un cours spécifié.
     *
     * @param form formulaire d'inscription qui contient les informations de l'étudiant et le cours
     * @return message de confirmation de l'inscription avec les détails de l'étudiant et le cours
     * @throws Exception erreurs de communication avec le serveur, ou exceptions produites lors du traitement de la
     * requête par le serveur. Ces derniers incluent les erreurs de lecture du formulaire d'inscription, les erreurs
     * d'écriture dans le fichier inscription.txt et les erreurs de transmission des résultats de la requête du
     * serveur au client.
     */
    public String registerStudent (RegistrationForm form) throws Exception {
        String message = null;
        List<Exception> errorList = null;
        try (
                Socket cS = new Socket(HOST,PORT);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
        ) {
            os.writeObject("INSCRIRE");
            os.writeObject(form);
            os.flush();
            message = (String) is.readObject();
            if (message == null) {
                errorList = (ArrayList <Exception>) is.readObject();
            }
        }

        catch (IOException ex) {
            throw new IOException("Erreur de communication avec le serveur.");
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Erreur de communication avec le serveur.");
        }

        if (errorList != null) {
            for (Exception ex : errorList) {
                throw ex;
            }
        }

        return message;
    }
}
