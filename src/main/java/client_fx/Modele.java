package client_fx;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Modele {
    public ArrayList<Course> getCourseList(String args) {
        ArrayList<Course> courseList = null;
        try (
                Socket cS = new Socket("127.0.0.1",1337);
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

    public String registerStudent (String prenom, String nom, String email, String matricule, Course course) {
        String message = null;
        try (
                Socket cS = new Socket("127.0.0.1",1337);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
        ) {
            // Course testCourse = new Course("IFT1025",	"Programmation2", "Hiver");
            RegistrationForm form = new RegistrationForm(
                    prenom, nom, email, matricule, course);

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
