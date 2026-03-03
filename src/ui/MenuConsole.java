package ui;

import model.*;
import service.HotelService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// Interface console de l'application : affiche les menus et interagit avec l'utilisateur via le terminal
public class MenuConsole {
    private HotelService service;
    private Scanner scanner;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Initialise le menu en créant le service hôtel et le scanner de saisie console
    public MenuConsole() {
        this.service = new HotelService();
        this.scanner = new Scanner(System.in);
    }

    // ===================== MENU PRINCIPAL =====================

    // Lance la boucle principale du menu et redirige vers les sous-menus selon le choix de l'utilisateur
    public void demarrer() {
        boolean running = true;
        System.out.println("\n========================================");
        System.out.println("   BIENVENUE À L'HÔTEL JAVA ★★★★");
        System.out.println("========================================");

        while (running) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1:
                    menuChambres();
                    break;
                case 2:
                    menuClients();
                    break;
                case 3:
                    menuReservations();
                    break;
                case 4:
                    menuFactures();
                    break;
                case 5:
                    afficherStatistiques();
                    break;
                case 0:
                    service.sauvegarderDonnees();
                    System.out.println("\nDonnées sauvegardées. Au revoir !");
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    // Affiche les options du menu principal dans la console
    private void afficherMenuPrincipal() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│         MENU PRINCIPAL           │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Gestion des chambres         │");
        System.out.println("│  2. Gestion des clients          │");
        System.out.println("│  3. Gestion des réservations     │");
        System.out.println("│  4. Facturation                  │");
        System.out.println("│  5. Statistiques                 │");
        System.out.println("│  0. Quitter                      │");
        System.out.println("└──────────────────────────────────┘");
    }

    // ===================== MENU CHAMBRES =====================

    // Affiche le sous-menu de gestion des chambres et traite les choix de l'utilisateur
    private void menuChambres() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- GESTION DES CHAMBRES ---");
            System.out.println("1. Voir toutes les chambres");
            System.out.println("2. Voir les chambres disponibles");
            System.out.println("3. Rechercher par type");
            System.out.println("4. Détails d'une chambre");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1:
                    afficherListe("Toutes les chambres", service.getChambres());
                    break;
                case 2:
                    afficherListe("Chambres disponibles", service.getChambresDisponibles());
                    break;
                case 3:
                    rechercherChambreParType();
                    break;
                case 4:
                    detailsChambre();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Demande un type de chambre à l'utilisateur et affiche les chambres disponibles correspondantes
    private void rechercherChambreParType() {
        System.out.println("Types disponibles : 1-Simple | 2-Double | 3-Suite");
        int choix = lireEntier("Votre choix : ");
        TypeChambre type;
        switch (choix) {
            case 1: type = TypeChambre.SIMPLE; break;
            case 2: type = TypeChambre.DOUBLE; break;
            case 3: type = TypeChambre.SUITE; break;
            default:
                System.out.println("Type invalide.");
                return;
        }
        List<Chambre> result = service.getChambresParType(type);
        afficherListe("Chambres " + type.getLibelle() + " disponibles", result);
    }

    // Affiche la description et les simulations de prix d'une chambre saisie par son numéro
    private void detailsChambre() {
        int numero = lireEntier("Numéro de chambre : ");
        Chambre chambre = service.trouverChambre(numero);
        if (chambre == null) {
            System.out.println("Chambre introuvable.");
            return;
        }
        System.out.println("\n" + chambre);
        System.out.println("Description : " + chambre.getDescription());
        System.out.println("Prix pour 1 nuit  : " + String.format("%.2f€", chambre.calculerPrix(1)));
        System.out.println("Prix pour 5 nuits : " + String.format("%.2f€", chambre.calculerPrix(5)));
        System.out.println("Prix pour 7 nuits : " + String.format("%.2f€", chambre.calculerPrix(7)));
    }

    // ===================== MENU CLIENTS =====================

    // Affiche le sous-menu de gestion des clients et traite les choix de l'utilisateur
    private void menuClients() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- GESTION DES CLIENTS ---");
            System.out.println("1. Voir tous les clients");
            System.out.println("2. Ajouter un client");
            System.out.println("3. Rechercher un client");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1:
                    afficherListe("Tous les clients", service.getClients());
                    break;
                case 2:
                    ajouterClient();
                    break;
                case 3:
                    rechercherClient();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Saisit les informations d'un nouveau client et l'enregistre via le service
    private void ajouterClient() {
        System.out.println("\n-- Nouveau client --");
        String nom = lireTexte("Nom : ");
        String prenom = lireTexte("Prénom : ");
        String tel = lireTexte("Téléphone : ");
        String email = lireTexte("Email : ");
        Client client = service.ajouterClient(nom, prenom, tel, email);
        System.out.println("Client ajouté : " + client);
    }

    // Recherche un client par son nom et affiche ses informations si trouvé
    private void rechercherClient() {
        String nom = lireTexte("Nom du client : ");
        Client client = service.rechercherClientParNom(nom);
        if (client != null) {
            System.out.println(client);
        } else {
            System.out.println("Aucun client trouvé avec ce nom.");
        }
    }

    // ===================== MENU RESERVATIONS =====================

    // Affiche le sous-menu de gestion des réservations et traite les choix de l'utilisateur
    private void menuReservations() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- GESTION DES RÉSERVATIONS ---");
            System.out.println("1. Voir toutes les réservations");
            System.out.println("2. Nouvelle réservation");
            System.out.println("3. Modifier une réservation");
            System.out.println("4. Annuler une réservation");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1:
                    afficherListe("Toutes les réservations", service.getReservations());
                    break;
                case 2:
                    nouvelleReservation();
                    break;
                case 3:
                    modifierReservation();
                    break;
                case 4:
                    annulerReservation();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Guide l'utilisateur pour sélectionner un client, une chambre et des dates, puis crée la réservation
    private void nouvelleReservation() {
        System.out.println("\n-- Nouvelle réservation --");

        if (service.getClients().isEmpty()) {
            System.out.println("Aucun client enregistré. Veuillez d'abord ajouter un client.");
            return;
        }
        afficherListe("Clients", service.getClients());
        int clientId = lireEntier("ID du client : ");
        Client client = service.trouverClient(clientId);
        if (client == null) {
            System.out.println("Client introuvable.");
            return;
        }

        List<Chambre> disponibles = service.getChambresDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("Aucune chambre disponible.");
            return;
        }
        afficherListe("Chambres disponibles", disponibles);
        int numChambre = lireEntier("Numéro de chambre : ");
        Chambre chambre = service.trouverChambre(numChambre);
        if (chambre == null || !chambre.isDisponible()) {
            System.out.println("Chambre invalide ou non disponible.");
            return;
        }

        LocalDate dateArrivee = lireDate("Date d'arrivée (dd/MM/yyyy) : ");
        if (dateArrivee == null) return;
        LocalDate dateDepart = lireDate("Date de départ (dd/MM/yyyy) : ");
        if (dateDepart == null) return;

        int nbNuits = (int) java.time.temporal.ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        double prix = chambre.calculerPrix(nbNuits);
        System.out.println(String.format("\nRécapitulatif : %s | Chambre n°%d (%s) | %d nuit(s) | %.2f€",
                client.getNomComplet(), chambre.getNumero(), chambre.getType(), nbNuits, prix));

        String confirm = lireTexte("Confirmer ? (oui/non) : ");
        if (confirm.equalsIgnoreCase("oui") || confirm.equalsIgnoreCase("o")) {
            Reservation resa = service.creerReservation(client, chambre, dateArrivee, dateDepart);
            if (resa != null) {
                System.out.println("Réservation créée : " + resa);
            }
        } else {
            System.out.println("Réservation annulée.");
        }
    }

    // Permet de changer les dates d'une réservation existante saisie par son identifiant
    private void modifierReservation() {
        afficherListe("Réservations", service.getReservations());
        int id = lireEntier("ID de la réservation à modifier : ");
        Reservation resa = service.trouverReservation(id);
        if (resa == null) {
            System.out.println("Réservation introuvable.");
            return;
        }
        System.out.println("Réservation actuelle : " + resa);

        LocalDate nouvArrivee = lireDate("Nouvelle date d'arrivée (dd/MM/yyyy) : ");
        if (nouvArrivee == null) return;
        LocalDate nouvDepart = lireDate("Nouvelle date de départ (dd/MM/yyyy) : ");
        if (nouvDepart == null) return;

        if (service.modifierReservation(id, nouvArrivee, nouvDepart)) {
            System.out.println("Réservation modifiée avec succès.");
            System.out.println(service.trouverReservation(id));
        }
    }

    // Demande confirmation à l'utilisateur puis annule la réservation saisie par son identifiant
    private void annulerReservation() {
        afficherListe("Réservations", service.getReservations());
        int id = lireEntier("ID de la réservation à annuler : ");
        String confirm = lireTexte("Confirmer l'annulation ? (oui/non) : ");
        if (confirm.equalsIgnoreCase("oui") || confirm.equalsIgnoreCase("o")) {
            if (service.annulerReservation(id)) {
                System.out.println("Réservation annulée avec succès.");
            }
        }
    }

    // ===================== MENU FACTURES =====================

    // Affiche le sous-menu de facturation et traite les choix de l'utilisateur
    private void menuFactures() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- FACTURATION ---");
            System.out.println("1. Voir toutes les factures");
            System.out.println("2. Générer une facture");
            System.out.println("3. Payer une facture");
            System.out.println("4. Détail d'une facture");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1:
                    afficherListe("Factures", service.getFactures());
                    break;
                case 2:
                    genererFacture();
                    break;
                case 3:
                    payerFacture();
                    break;
                case 4:
                    detailFacture();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Génère et affiche une facture pour une réservation saisie par son identifiant
    private void genererFacture() {
        afficherListe("Réservations", service.getReservations());
        int id = lireEntier("ID de la réservation : ");
        Reservation resa = service.trouverReservation(id);
        if (resa == null) {
            System.out.println("Réservation introuvable.");
            return;
        }
        if (resa.getStatut() == StatutReservation.ANNULEE) {
            System.out.println("Impossible de facturer une réservation annulée.");
            return;
        }
        Facture facture = service.genererFacture(resa);
        System.out.println("\n" + facture.afficherDetail());
    }

    // Marque comme payée la facture dont l'identifiant est saisi par l'utilisateur
    private void payerFacture() {
        afficherListe("Factures", service.getFactures());
        int id = lireEntier("ID de la facture à payer : ");
        if (service.payerFacture(id)) {
            System.out.println("Facture payée avec succès !");
        }
    }

    // Affiche le détail complet d'une facture saisie par son identifiant
    private void detailFacture() {
        int id = lireEntier("ID de la facture : ");
        Facture facture = service.trouverFacture(id);
        if (facture == null) {
            System.out.println("Facture introuvable.");
            return;
        }
        System.out.println(facture.afficherDetail());
    }

    // ===================== STATISTIQUES =====================

    // Affiche un tableau récapitulatif des statistiques de l'hôtel (occupation, chiffre d'affaires...)
    private void afficherStatistiques() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│          STATISTIQUES HÔTEL          │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.printf("│ Chambres totales    : %-15d│\n", service.getChambres().size());
        System.out.printf("│ Chambres occupées   : %-15d│\n", service.getNbChambresOccupees());
        System.out.printf("│ Taux d'occupation   : %-14.1f%%│\n", service.getTauxOccupation());
        System.out.printf("│ Réservations        : %-15d│\n", service.getReservations().size());
        System.out.printf("│ Factures émises     : %-15d│\n", service.getFactures().size());
        System.out.printf("│ Chiffre d'affaires  : %-13.2f€ │\n", service.getChiffreAffaires());
        System.out.println("└──────────────────────────────────────┘");
    }

    // ===================== UTILITAIRES =====================

    // Affiche une liste générique avec un titre et le nombre d'éléments en bas
    private <T> void afficherListe(String titre, List<T> liste) {
        System.out.println("\n--- " + titre + " ---");
        if (liste.isEmpty()) {
            System.out.println("Aucun élément à afficher.");
            return;
        }
        for (T item : liste) {
            System.out.println(item);
        }
        System.out.println("--- " + liste.size() + " élément(s) ---");
    }

    // Lit et retourne un entier saisi par l'utilisateur, redemande si la saisie est invalide
    private int lireEntier(String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir un nombre valide.");
            }
        }
    }

    // Affiche un message et retourne la saisie texte de l'utilisateur sans espaces superflus
    private String lireTexte(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    // Lit une date au format dd/MM/yyyy, redemande en cas de format invalide et retourne null si abandon
    private LocalDate lireDate(String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Format invalide. Utilisez dd/MM/yyyy (ex: 25/12/2025).");
                String retry = lireTexte("Réessayer ? (oui/non) : ");
                if (!retry.equalsIgnoreCase("oui") && !retry.equalsIgnoreCase("o")) {
                    return null;
                }
            }
        }
    }
}
