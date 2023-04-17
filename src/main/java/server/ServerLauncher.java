package server;

/**
 * Lanceur du serveur.
 */
public class ServerLauncher {
    /**
     * Numéro de port auquel le serveur va se connecter.
     */
    public final static int PORT = 1337;

    /**
     * Lance le serveur.
     * Connecte le serveur au port spécifié et, si la connexion est réussie, démarre le serveur.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}