package client_simple;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Modele {
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 1337;
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
