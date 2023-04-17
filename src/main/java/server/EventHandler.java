package server;

/**
 * Exécuteur d'actions en réaction à des évènements.
 * Cette interface permet d'effectuer des commandes adaptées aux évènements qui se produisent.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Effectue une commande à partir des arguments fournis.
     * La commande <code>cmd</code> est conçue pour être appelée lorsqu'un évènement spécifique se produit.
     *
     * @param cmd le nom de la commande
     * @param arg les arguments pour la commande
     */
    void handle(String cmd, String arg);
}
