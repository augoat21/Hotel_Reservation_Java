package model;

public abstract class Chambre {
    private int numero;
    private int etage;
    private double prixParNuit;
    private boolean disponible;
    private TypeChambre type;

    // Constructeur 
    public Chambre(int numero, int etage, double prixParNuit, TypeChambre type) {
        this.numero = numero;
        this.etage = etage;
        this.prixParNuit = prixParNuit;
        this.disponible = true;
        this.type = type;
    }

    // Méthode abstraite : chaque type de chambre décrit ses équipements
    public abstract String getDescription();

    // Méthode abstraite : calcul du prix peut varier selon le type
    public abstract double calculerPrix(int nbNuits);

    //Les methodes Get & Set
    
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public TypeChambre getType() {
        return type;
    }

    public void setType(TypeChambre type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Chambre n°%d | %s | Étage %d | %.2f€/nuit | %s",
                numero, type, etage, prixParNuit,
                disponible ? "Disponible" : "Occupée");
    }

    // Format CSV pour la sauvegarde
    public String toCSV() {
        return String.format("%d;%d;%.2f;%b;%s", numero, etage, prixParNuit, disponible, type.name());
    }
}
