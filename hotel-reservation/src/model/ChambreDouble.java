package model;

// Chambre double avec un lit double et minibar ; réduction de 5% à partir de 5 nuits
public class ChambreDouble extends Chambre {

    // Crée une chambre double en appelant le constructeur parent avec le type DOUBLE
    public ChambreDouble(int numero, int etage, double prixParNuit) {
        super(numero, etage, prixParNuit, TypeChambre.DOUBLE);
    }

    // Retourne la description des équipements d'une chambre double
    @Override
    public String getDescription() {
        return "Chambre double : 1 lit double, salle de bain, TV, Wi-Fi, minibar.";
    }

    // Calcule le prix total avec une réduction de 5% automatiquement appliquée à partir de 5 nuits
    @Override
    public double calculerPrix(int nbNuits) {
        if (nbNuits >= 5) {
            return getPrixParNuit() * nbNuits * 0.95;
        }
        return getPrixParNuit() * nbNuits;
    }
}
