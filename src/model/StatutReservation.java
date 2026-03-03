package model;

// Enumération des états possibles d'une réservation (en attente, confirmée ou annulée)
public enum StatutReservation {
    EN_ATTENTE("En attente"),
    CONFIRMEE("Confirmée"),
    ANNULEE("Annulée");

    private final String libelle;

    // Associe un libellé lisible en français à chaque valeur de l'énumération
    StatutReservation(String libelle) {
        this.libelle = libelle;
    }

    // Retourne le libellé français correspondant au statut
    public String getLibelle() {
        return libelle;
    }

    // Retourne le libellé pour l'affichage dans la console
    @Override
    public String toString() {
        return libelle;
    }
}
