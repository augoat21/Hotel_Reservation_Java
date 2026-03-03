package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Représente une réservation liant un client à une chambre sur une période donnée
public class Reservation {
    private int id;
    private Client client;
    private Chambre chambre;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private StatutReservation statut;

    private static int compteur = 0;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Crée une nouvelle réservation avec un identifiant auto-incrémenté et le statut CONFIRMEE par défaut
    public Reservation(Client client, Chambre chambre, LocalDate dateArrivee, LocalDate dateDepart) {
        this.id = ++compteur;
        this.client = client;
        this.chambre = chambre;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.statut = StatutReservation.CONFIRMEE;
    }

    // Crée une réservation avec toutes ses données explicites, utilisé lors du chargement depuis un fichier CSV
    public Reservation(int id, Client client, Chambre chambre, LocalDate dateArrivee,
                       LocalDate dateDepart, StatutReservation statut) {
        this.id = id;
        this.client = client;
        this.chambre = chambre;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.statut = statut;
        if (id >= compteur) {
            compteur = id;
        }
    }

    // Calcule le nombre de nuits entre la date d'arrivée et la date de départ
    public int getNbNuits() {
        return (int) ChronoUnit.DAYS.between(dateArrivee, dateDepart);
    }

    // Calcule le montant total en appelant calculerPrix sur la chambre (polymorphisme)
    public double getMontantTotal() {
        return chambre.calculerPrix(getNbNuits());
    }

    // Annule la réservation et libère la chambre associée
    public void annuler() {
        this.statut = StatutReservation.ANNULEE;
        this.chambre.setDisponible(true);
    }

    // Retourne l'identifiant unique de la réservation
    public int getId() {
        return id;
    }

    // Retourne le client associé à la réservation
    public Client getClient() {
        return client;
    }

    // Modifie le client associé à la réservation
    public void setClient(Client client) {
        this.client = client;
    }

    // Retourne la chambre réservée
    public Chambre getChambre() {
        return chambre;
    }

    // Modifie la chambre associée à la réservation
    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    // Retourne la date d'arrivée du séjour
    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    // Modifie la date d'arrivée du séjour
    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    // Retourne la date de départ du séjour
    public LocalDate getDateDepart() {
        return dateDepart;
    }

    // Modifie la date de départ du séjour
    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    // Retourne le statut actuel de la réservation (CONFIRMEE, EN_ATTENTE ou ANNULEE)
    public StatutReservation getStatut() {
        return statut;
    }

    // Modifie le statut de la réservation
    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    // Réinitialise le compteur d'identifiants, utilisé après chargement depuis un fichier
    public static void setCompteur(int valeur) {
        compteur = valeur;
    }

    // Retourne un résumé complet de la réservation sur une seule ligne
    @Override
    public String toString() {
        return String.format("Réservation #%d | %s | Chambre n°%d (%s) | Du %s au %s | %d nuit(s) | %.2f€ | %s",
                id, client.getNomComplet(), chambre.getNumero(), chambre.getType(),
                dateArrivee.format(FORMATTER), dateDepart.format(FORMATTER),
                getNbNuits(), getMontantTotal(), statut);
    }

    // Sérialise la réservation en ligne CSV pour la sauvegarde dans un fichier
    public String toCSV() {
        return String.format("%d;%d;%d;%s;%s;%s",
                id, client.getId(), chambre.getNumero(),
                dateArrivee.format(FORMATTER), dateDepart.format(FORMATTER), statut.name());
    }
}
