package model;

// Suite haut de gamme avec jacuzzi et room service ; réduction de 10% dès 3 nuits et 15% dès 7 nuits
public class Suite extends Chambre {

    // Crée une suite en appelant le constructeur parent avec le type SUITE
    public Suite(int numero, int etage, double prixParNuit) {
        super(numero, etage, prixParNuit, TypeChambre.SUITE);
    }

    // Retourne la description des équipements de la suite
    @Override
    public String getDescription() {
        return "Suite : salon séparé, lit king size, jacuzzi, TV, Wi-Fi, minibar, room service inclus.";
    }

    // Calcule le prix avec réductions progressives : 10% dès 3 nuits, 15% dès 7 nuits
    @Override
    public double calculerPrix(int nbNuits) {
        if (nbNuits >= 7) {
            return getPrixParNuit() * nbNuits * 0.85;
        } else if (nbNuits >= 3) {
            return getPrixParNuit() * nbNuits * 0.90;
        }
        return getPrixParNuit() * nbNuits;
    }
}
