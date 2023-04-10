package client_simple;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Modele {
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 1337;
    public List<Course> retrieveSessionCourses(String args) {
        List<Course> courseList = null;
        try (
                Socket cS = new Socket(HOST,PORT);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
        ) {
            os.writeObject(args);
            os.flush();
            courseList = (ArrayList <Course>) is.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public String registerStudent (RegistrationForm form) {
        String message = null;
        try (
                Socket cS = new Socket(HOST,PORT);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
        ) {
            os.writeObject("INSCRIRE");
            os.writeObject(form);
            os.flush();
            message = (String) is.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
