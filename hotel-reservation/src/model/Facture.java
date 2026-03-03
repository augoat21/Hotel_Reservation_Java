package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Représente une facture générée à partir d'une réservation, avec suivi du statut de paiement
public class Facture {
    private int id;
    private Reservation reservation;
    private double montant;
    private LocalDate dateEmission;
    private boolean payee;

    private static int compteur = 0;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Crée une nouvelle facture à partir d'une réservation, avec la date du jour et statut non payée
    public Facture(Reservation reservation) {
        this.id = ++compteur;
        this.reservation = reservation;
        this.montant = reservation.getMontantTotal();
        this.dateEmission = LocalDate.now();
        this.payee = false;
    }

    // Crée une facture avec toutes ses données explicites, utilisé lors du chargement depuis un fichier CSV
    public Facture(int id, Reservation reservation, double montant, LocalDate dateEmission, boolean payee) {
        this.id = id;
        this.reservation = reservation;
        this.montant = montant;
        this.dateEmission = dateEmission;
        this.payee = payee;
        if (id >= compteur) {
            compteur = id;
        }
    }

    // Marque la facture comme payée
    public void payer() {
        this.payee = true;
    }

    // Retourne l'identifiant unique de la facture
    public int getId() {
        return id;
    }

    // Retourne la réservation associée à cette facture
    public Reservation getReservation() {
        return reservation;
    }

    // Retourne le montant total de la facture en euros
    public double getMontant() {
        return montant;
    }

    // Retourne la date à laquelle la facture a été émise
    public LocalDate getDateEmission() {
        return dateEmission;
    }

    // Indique si la facture a été réglée
    public boolean isPayee() {
        return payee;
    }

    // Modifie le statut de paiement de la facture
    public void setPayee(boolean payee) {
        this.payee = payee;
    }

    // Réinitialise le compteur d'identifiants, utilisé après chargement depuis un fichier
    public static void setCompteur(int valeur) {
        compteur = valeur;
    }

    // Génère un affichage formaté en tableau avec tous les détails de la facture
    public String afficherDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║            FACTURE HOTEL                 ║\n");
        sb.append("╠══════════════════════════════════════════╣\n");
        sb.append(String.format("║ Facture n°: %-29d║\n", id));
        sb.append(String.format("║ Date: %-35s║\n", dateEmission.format(FORMATTER)));
        sb.append("╠══════════════════════════════════════════╣\n");
        sb.append(String.format("║ Client: %-33s║\n", reservation.getClient().getNomComplet()));
        sb.append(String.format("║ Chambre: n°%-30d║\n", reservation.getChambre().getNumero()));
        sb.append(String.format("║ Type: %-35s║\n", reservation.getChambre().getType()));
        sb.append(String.format("║ Arrivée: %-32s║\n", reservation.getDateArrivee().format(FORMATTER)));
        sb.append(String.format("║ Départ: %-33s║\n", reservation.getDateDepart().format(FORMATTER)));
        sb.append(String.format("║ Nuits: %-34d║\n", reservation.getNbNuits()));
        sb.append(String.format("║ Prix/nuit: %-29.2f€ ║\n", reservation.getChambre().getPrixParNuit()));
        sb.append("╠══════════════════════════════════════════╣\n");
        sb.append(String.format("║ TOTAL: %-33.2f€ ║\n", montant));
        sb.append(String.format("║ Statut: %-33s║\n", payee ? "PAYÉE" : "EN ATTENTE"));
        sb.append("╚══════════════════════════════════════════╝\n");
        return sb.toString();
    }

    // Retourne un résumé compact de la facture sur une seule ligne
    @Override
    public String toString() {
        return String.format("Facture #%d | Résa #%d | %s | %.2f€ | %s",
                id, reservation.getId(), reservation.getClient().getNomComplet(),
                montant, payee ? "Payée" : "En attente");
    }

    // Sérialise la facture en ligne CSV pour la sauvegarde dans un fichier
    public String toCSV() {
        return String.format("%d;%d;%.2f;%s;%b",
                id, reservation.getId(), montant, dateEmission.format(FORMATTER), payee);
    }
}
