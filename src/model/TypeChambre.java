package model;

// Enumération des catégories de chambres disponibles dans l'hôtel (Simple, Double, Suite)
public enum TypeChambre {
    SIMPLE("Simple"),
    DOUBLE("Double"),
    SUITE("Suite");

    private final String libelle;

    // Associe un libellé lisible en français à chaque type de chambre
    TypeChambre(String libelle) {
        this.libelle = libelle;
    }

    // Retourne le libellé français correspondant au type de chambre
    public String getLibelle() {
        return libelle;
    }

    // Retourne le libellé pour l'affichage dans la console
    @Override
    public String toString() {
        return libelle;
    }
}
