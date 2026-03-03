package service;

import model.*;
import storage.FichierStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Service principal de l'hôtel : gère les chambres, clients, réservations, factures et statistiques
public class HotelService {
    private List<Chambre> chambres;
    private List<Client> clients;
    private List<Reservation> reservations;
    private List<Facture> factures;
    private FichierStorage storage;

    // Initialise le service en créant le gestionnaire de stockage et en chargeant les données persistées
    public HotelService() {
        this.storage = new FichierStorage();
        chargerDonnees();
    }

    // ===================== CHARGEMENT / SAUVEGARDE =====================

    // Charge toutes les données depuis les fichiers CSV et initialise les chambres si nécessaire
    private void chargerDonnees() {
        chambres = storage.chargerChambres();
        clients = storage.chargerClients();
        reservations = storage.chargerReservations(clients, chambres);
        factures = storage.chargerFactures(reservations);

        if (chambres.isEmpty()) {
            initialiserChambres();
        }
    }

    // Sauvegarde toutes les données dans les fichiers CSV correspondants
    public void sauvegarderDonnees() {
        storage.sauvegarderChambres(chambres);
        storage.sauvegarderClients(clients);
        storage.sauvegarderReservations(reservations);
        storage.sauvegarderFactures(factures);
    }

    // Crée les 8 chambres par défaut au premier démarrage et les sauvegarde
    private void initialiserChambres() {
        chambres.add(new ChambreSimple(101, 1, 59.99));
        chambres.add(new ChambreSimple(102, 1, 59.99));
        chambres.add(new ChambreSimple(103, 1, 64.99));

        chambres.add(new ChambreDouble(201, 2, 89.99));
        chambres.add(new ChambreDouble(202, 2, 89.99));
        chambres.add(new ChambreDouble(203, 2, 99.99));

        chambres.add(new Suite(301, 3, 189.99));
        chambres.add(new Suite(302, 3, 219.99));

        sauvegarderDonnees();
        System.out.println("Hôtel initialisé avec 8 chambres par défaut.");
    }

    // ===================== GESTION DES CHAMBRES =====================

    // Retourne la liste complète de toutes les chambres de l'hôtel
    public List<Chambre> getChambres() {
        return chambres;
    }

    // Retourne uniquement les chambres dont le statut est disponible
    public List<Chambre> getChambresDisponibles() {
        List<Chambre> disponibles = new ArrayList<>();
        for (Chambre c : chambres) {
            if (c.isDisponible()) {
                disponibles.add(c);
            }
        }
        return disponibles;
    }

    // Retourne les chambres disponibles filtrées par type (SIMPLE, DOUBLE ou SUITE)
    public List<Chambre> getChambresParType(TypeChambre type) {
        List<Chambre> result = new ArrayList<>();
        for (Chambre c : chambres) {
            if (c.getType() == type && c.isDisponible()) {
                result.add(c);
            }
        }
        return result;
    }

    // Recherche et retourne une chambre par son numéro, ou null si introuvable
    public Chambre trouverChambre(int numero) {
        for (Chambre c : chambres) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        return null;
    }

    // ===================== GESTION DES CLIENTS =====================

    // Retourne la liste de tous les clients enregistrés
    public List<Client> getClients() {
        return clients;
    }

    // Crée un nouveau client, l'ajoute à la liste et sauvegarde immédiatement
    public Client ajouterClient(String nom, String prenom, String telephone, String email) {
        Client client = new Client(nom, prenom, telephone, email);
        clients.add(client);
        sauvegarderDonnees();
        return client;
    }

    // Recherche et retourne un client par son identifiant, ou null si introuvable
    public Client trouverClient(int id) {
        for (Client c : clients) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    // Recherche un client par son nom ou nom complet, sans tenir compte de la casse
    public Client rechercherClientParNom(String nom) {
        for (Client c : clients) {
            if (c.getNom().equalsIgnoreCase(nom) || c.getNomComplet().equalsIgnoreCase(nom)) {
                return c;
            }
        }
        return null;
    }

    // ===================== GESTION DES RESERVATIONS =====================

    // Retourne la liste de toutes les réservations
    public List<Reservation> getReservations() {
        return reservations;
    }

    // Crée une réservation si la chambre est disponible et les dates valides, puis sauvegarde
    public Reservation creerReservation(Client client, Chambre chambre,
                                         LocalDate dateArrivee, LocalDate dateDepart) {
        if (!chambre.isDisponible()) {
            System.out.println("Erreur : La chambre n°" + chambre.getNumero() + " n'est pas disponible.");
            return null;
        }
        if (dateDepart.isBefore(dateArrivee) || dateDepart.isEqual(dateArrivee)) {
            System.out.println("Erreur : La date de départ doit être après la date d'arrivée.");
            return null;
        }

        Reservation reservation = new Reservation(client, chambre, dateArrivee, dateDepart);
        chambre.setDisponible(false);
        reservations.add(reservation);
        sauvegarderDonnees();
        return reservation;
    }

    // Annule une réservation et libère la chambre associée ; retourne false si déjà annulée ou introuvable
    public boolean annulerReservation(int idReservation) {
        Reservation reservation = trouverReservation(idReservation);
        if (reservation == null) {
            System.out.println("Erreur : Réservation introuvable.");
            return false;
        }
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            System.out.println("Erreur : Cette réservation est déjà annulée.");
            return false;
        }
        reservation.annuler();
        sauvegarderDonnees();
        return true;
    }

    // Modifie les dates d'une réservation existante après vérification de validité
    public boolean modifierReservation(int idReservation, LocalDate nouvDateArrivee, LocalDate nouvDateDepart) {
        Reservation reservation = trouverReservation(idReservation);
        if (reservation == null) {
            System.out.println("Erreur : Réservation introuvable.");
            return false;
        }
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            System.out.println("Erreur : Impossible de modifier une réservation annulée.");
            return false;
        }
        if (nouvDateDepart.isBefore(nouvDateArrivee) || nouvDateDepart.isEqual(nouvDateArrivee)) {
            System.out.println("Erreur : La date de départ doit être après la date d'arrivée.");
            return false;
        }
        reservation.setDateArrivee(nouvDateArrivee);
        reservation.setDateDepart(nouvDateDepart);
        sauvegarderDonnees();
        return true;
    }

    // Recherche et retourne une réservation par son identifiant, ou null si introuvable
    public Reservation trouverReservation(int id) {
        for (Reservation r : reservations) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    // ===================== GESTION DES FACTURES =====================

    // Retourne la liste de toutes les factures générées
    public List<Facture> getFactures() {
        return factures;
    }

    // Génère une facture pour une réservation, ou retourne celle existante si déjà créée
    public Facture genererFacture(Reservation reservation) {
        for (Facture f : factures) {
            if (f.getReservation().getId() == reservation.getId()) {
                System.out.println("Une facture existe déjà pour cette réservation.");
                return f;
            }
        }

        Facture facture = new Facture(reservation);
        factures.add(facture);
        sauvegarderDonnees();
        return facture;
    }

    // Marque une facture comme payée ; retourne false si introuvable ou déjà payée
    public boolean payerFacture(int idFacture) {
        for (Facture f : factures) {
            if (f.getId() == idFacture) {
                if (f.isPayee()) {
                    System.out.println("Cette facture est déjà payée.");
                    return false;
                }
                f.payer();
                sauvegarderDonnees();
                return true;
            }
        }
        System.out.println("Facture introuvable.");
        return false;
    }

    // Recherche et retourne une facture par son identifiant, ou null si introuvable
    public Facture trouverFacture(int id) {
        for (Facture f : factures) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    // ===================== STATISTIQUES =====================

    // Calcule le chiffre d'affaires total en additionnant les montants des factures payées
    public double getChiffreAffaires() {
        double total = 0;
        for (Facture f : factures) {
            if (f.isPayee()) {
                total += f.getMontant();
            }
        }
        return total;
    }

    // Retourne le nombre de chambres actuellement occupées
    public int getNbChambresOccupees() {
        int count = 0;
        for (Chambre c : chambres) {
            if (!c.isDisponible()) {
                count++;
            }
        }
        return count;
    }

    // Calcule le taux d'occupation en pourcentage (chambres occupées / total) ; retourne 0 si aucune chambre
    public double getTauxOccupation() {
        if (chambres.isEmpty()) return 0;
        return (double) getNbChambresOccupees() / chambres.size() * 100;
    }
}
