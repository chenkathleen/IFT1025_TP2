package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static String currentScreen = "chooseSession";
    //private static Map<String, Course> currentCourseDict;
    private static ArrayList <Course> currentCourseDict;
    
    public static void main(String[] args) {

        try (
                Scanner sc = new Scanner(System.in);
        ){

            String fromServer;
            String fromUser = null;
            String session;
            // ArrayList<Course>; // available courses

            String welcomeMessage = "Welcome." + "\n" + "Choose session: 1 = Automne, 2= Ete, 3= Hiver";
            System.out.println(welcomeMessage);
//            String chooseSessionOutput = "Choose session: 1 = Automne, 2= Ete, 3= Hiver";
//            System.out.println(chooseSessionOutput);
            
            while (sc.hasNext()) {
                Socket cS = new Socket("127.0.0.1",1337);
                ObjectOutputStream os = new ObjectOutputStream(cS.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(cS.getInputStream());
                
                // show instruction
//                if (currentScreen.equals("chooseSession")) {
//                    fromUser = chooseSessionScreen(sc);
//                }

                fromUser = sc.nextLine();
                System.out.println("Client: " + fromUser);
                if (fromUser.equals("exit")) {
                    System.out.println("Exiting.");
                    break;
                } else {
                    processInput(fromUser, sc, os);
//                if (processInput(fromUser, sc) != null) {
//                    os.writeObject(processInput(fromUser, sc));
//                    os.flush();
//                }
                    if ((fromServer = processOutput(is)) != null) {
                        System.out.println("Server: " + fromServer);
                    }
                }


                os.close();
                is.close();
            }

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch  (Exception ex) {

        }

    }

    // processes user inputs and converts to objects to be passed to server
    public static void processInput(String input, Scanner sc, ObjectOutputStream os) throws Exception {
        // case for each command, return error and break if command is not supported
        // sends String (command and args) that is recognized by server

        String processedInput = null;

        if (currentScreen.equals("chooseSession")) {
            if (input.equals("1")) {
                processedInput = "CHARGER Automne";
            } else if (input.equals("2")) {
                processedInput = "CHARGER Ete";
            } else if (input.equals("3")) {
                processedInput = "CHARGER Hiver";
            } else {
                System.out.println("Command is not recognized.");
                System.out.println("Choose session: 1 = Automne, 2= Ete, 3= Hiver");
                currentScreen = "chooseSession";
                return;
            }
            os.writeObject(processedInput);
            os.flush();
            currentScreen = "viewCourses";
        } else if (currentScreen.equals("viewCourses") || currentScreen.equals("registration")) {
            if (input.equals("1")) {
                System.out.println("Choose session: 1 = Automne, 2= Ete, 3= Hiver");
                currentScreen = "chooseSession";
                os.writeObject(""); //Server won't do anything with this
                os.flush();
            } else if (input.equals("2")) {
                String[] registrationInfo = registrationInfoScreen(sc);

                Course testCourse = new Course("IFT1025",	"Programmation2", "Hiver");
                RegistrationForm testInscription = new RegistrationForm(
                        registrationInfo[0], registrationInfo[1], registrationInfo[2], registrationInfo[3], testCourse);
//                RegistrationForm testInscription = new RegistrationForm(
//                        registrationInfo[0], registrationInfo[1], registrationInfo[2], registrationInfo[3], currentCourseDict.get(registrationInfo[4]));
                os.writeObject("INSCRIRE");
                os.writeObject(testInscription);
                os.flush();
                currentScreen = "registration";
            } else {
                System.out.println("Command is not recognized.");
                System.out.println("Choose session: 1 = Automne, 2= Ete, 3= Hiver");
                currentScreen = "chooseSession";

            }

        }
        //return new String[] {sendToServer, processedInput};
        // return processedInput;

        // send objects to Server

    }
    public static String processOutput(ObjectInputStream is) throws Exception {
        // can perform operations on objects, clean up output for display, including allowed next set of options
        String processedOutput = null;
        if (currentScreen.equals("viewCourses")) {
            //currentCourseDict = (Map<String, Course>) is.readObject();
            currentCourseDict = (ArrayList <Course>) is.readObject();
            processedOutput = "Courses offered: " + "\n" + currentCourseDict.toString() + "\n" + "1 = another session; 2 = register";
        } else if (currentScreen.equals("registration")) {
            processedOutput = is.readObject().toString() + "\n" + "1 = another session; 2 = register";
        }
        return processedOutput;
    }

    // prints instructions and returns selections from screen if current command needs additional user inputs
    public static String[] registrationInfoScreen(Scanner sc) {
        System.out.print("First name: ");
        String firstname = sc.nextLine();
        System.out.print("Last name: ");
        String lastname = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Matricule: ");
        String matricule = sc.nextLine();
        System.out.print("Code du cours: ");
        String codeDuCours = sc.nextLine();
        return new String[] {firstname, lastname, email, matricule, codeDuCours};
    }
    
}

