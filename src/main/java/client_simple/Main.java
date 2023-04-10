package client_simple;

public class Main {
    public static void main(String[] args) {
        Modele leModele = new Modele();
        Vue laVue = new Vue();
        Controleur leControleur = new Controleur(leModele, laVue);
        laVue.getSc().close();
    }
}
