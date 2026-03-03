package model;

// Représente un client de l'hôtel avec ses coordonnées et un identifiant auto-incrémenté
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    private static int compteur = 0;

    // Crée un nouveau client avec un identifiant généré automatiquement
    public Client(String nom, String prenom, String telephone, String email) {
        this.id = ++compteur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    // Crée un client avec un identifiant explicite, utilisé lors du chargement depuis un fichier CSV
    public Client(int id, String nom, String prenom, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        if (id >= compteur) {
            compteur = id;
        }
    }

    // Retourne l'identifiant unique du client
    public int getId() {
        return id;
    }

    // Retourne le nom de famille du client
    public String getNom() {
        return nom;
    }

    // Modifie le nom de famille du client
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Retourne le prénom du client
    public String getPrenom() {
        return prenom;
    }

    // Modifie le prénom du client
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Retourne le numéro de téléphone du client
    public String getTelephone() {
        return telephone;
    }

    // Modifie le numéro de téléphone du client
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Retourne l'adresse email du client
    public String getEmail() {
        return email;
    }

    // Modifie l'adresse email du client
    public void setEmail(String email) {
        this.email = email;
    }

    // Retourne le prénom et le nom du client concaténés
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Réinitialise le compteur d'identifiants, utilisé après chargement depuis un fichier
    public static void setCompteur(int valeur) {
        compteur = valeur;
    }

    // Retourne une représentation lisible du client avec ses coordonnées
    @Override
    public String toString() {
        return String.format("Client #%d | %s %s | Tél: %s | Email: %s",
                id, prenom, nom, telephone, email);
    }

    // Sérialise le client en ligne CSV pour la sauvegarde dans un fichier
    public String toCSV() {
        return String.format("%d;%s;%s;%s;%s", id, nom, prenom, telephone, email);
    }
}
