package client_simple;
@FunctionalInterface
public interface EventHandler {
    public void handle(String screen, String arg);
}
