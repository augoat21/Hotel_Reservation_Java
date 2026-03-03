package model;

// Chambre simple avec un lit simple ; prix fixe sans aucune réduction
public class ChambreSimple extends Chambre {

    // Crée une chambre simple en appelant le constructeur parent avec le type SIMPLE
    public ChambreSimple(int numero, int etage, double prixParNuit) {
        super(numero, etage, prixParNuit, TypeChambre.SIMPLE);
    }

    // Retourne la description des équipements d'une chambre simple
    @Override
    public String getDescription() {
        return "Chambre simple : 1 lit simple, salle de bain, TV, Wi-Fi.";
    }

    // Calcule le prix total sans réduction : prix par nuit multiplié par le nombre de nuits
    @Override
    public double calculerPrix(int nbNuits) {
        return getPrixParNuit() * nbNuits;
    }
}
