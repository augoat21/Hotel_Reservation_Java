package storage;

import model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Gère la persistance des données en lisant et écrivant des fichiers CSV dans le dossier "data/"
public class FichierStorage {
    private static final String DOSSIER_DATA = "data/";
    private static final String FICHIER_CHAMBRES = DOSSIER_DATA + "chambres.csv";
    private static final String FICHIER_CLIENTS = DOSSIER_DATA + "clients.csv";
    private static final String FICHIER_RESERVATIONS = DOSSIER_DATA + "reservations.csv";
    private static final String FICHIER_FACTURES = DOSSIER_DATA + "factures.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Crée le dossier "data/" s'il n'existe pas encore pour stocker les fichiers CSV
    public FichierStorage() {
        File dossier = new File(DOSSIER_DATA);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
    }

    // ===================== CHAMBRES =====================

    // Écrit toutes les chambres dans le fichier CSV en écrasant le contenu précédent
    public void sauvegarderChambres(List<Chambre> chambres) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_CHAMBRES))) {
            writer.println("numero;etage;prixParNuit;disponible;type");
            for (Chambre c : chambres) {
                writer.println(c.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des chambres : " + e.getMessage());
        }
    }

    // Lit le fichier CSV des chambres et retourne la liste reconstruite des objets Chambre
    public List<Chambre> chargerChambres() {
        List<Chambre> chambres = new ArrayList<>();
        File fichier = new File(FICHIER_CHAMBRES);
        if (!fichier.exists()) return chambres;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            reader.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(";");
                int numero = Integer.parseInt(parts[0]);
                int etage = Integer.parseInt(parts[1]);
                double prix = Double.parseDouble(parts[2].replace(",", "."));
                boolean dispo = Boolean.parseBoolean(parts[3]);
                TypeChambre type = TypeChambre.valueOf(parts[4]);

                Chambre chambre;
                switch (type) {
                    case SIMPLE:
                        chambre = new ChambreSimple(numero, etage, prix);
                        break;
                    case DOUBLE:
                        chambre = new ChambreDouble(numero, etage, prix);
                        break;
                    case SUITE:
                        chambre = new Suite(numero, etage, prix);
                        break;
                    default:
                        continue;
                }
                chambre.setDisponible(dispo);
                chambres.add(chambre);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des chambres : " + e.getMessage());
        }
        return chambres;
    }

    // ===================== CLIENTS =====================

    // Écrit tous les clients dans le fichier CSV en écrasant le contenu précédent
    public void sauvegarderClients(List<Client> clients) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_CLIENTS))) {
            writer.println("id;nom;prenom;telephone;email");
            for (Client c : clients) {
                writer.println(c.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des clients : " + e.getMessage());
        }
    }

    // Lit le fichier CSV des clients et retourne la liste reconstruite des objets Client
    public List<Client> chargerClients() {
        List<Client> clients = new ArrayList<>();
        File fichier = new File(FICHIER_CLIENTS);
        if (!fichier.exists()) return clients;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            reader.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(";");
                int id = Integer.parseInt(parts[0]);
                clients.add(new Client(id, parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des clients : " + e.getMessage());
        }
        return clients;
    }

    // ===================== RESERVATIONS =====================

    // Écrit toutes les réservations dans le fichier CSV en écrasant le contenu précédent
    public void sauvegarderReservations(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_RESERVATIONS))) {
            writer.println("id;clientId;chambreNumero;dateArrivee;dateDepart;statut");
            for (Reservation r : reservations) {
                writer.println(r.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des réservations : " + e.getMessage());
        }
    }

    // Lit le fichier CSV des réservations et reconstruit les objets en liant clients et chambres par identifiant
    public List<Reservation> chargerReservations(List<Client> clients, List<Chambre> chambres) {
        List<Reservation> reservations = new ArrayList<>();
        File fichier = new File(FICHIER_RESERVATIONS);
        if (!fichier.exists()) return reservations;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            reader.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(";");
                int id = Integer.parseInt(parts[0]);
                int clientId = Integer.parseInt(parts[1]);
                int chambreNum = Integer.parseInt(parts[2]);
                LocalDate dateArrivee = LocalDate.parse(parts[3], FORMATTER);
                LocalDate dateDepart = LocalDate.parse(parts[4], FORMATTER);
                StatutReservation statut = StatutReservation.valueOf(parts[5]);

                Client client = null;
                for (Client c : clients) {
                    if (c.getId() == clientId) {
                        client = c;
                        break;
                    }
                }

                Chambre chambre = null;
                for (Chambre c : chambres) {
                    if (c.getNumero() == chambreNum) {
                        chambre = c;
                        break;
                    }
                }

                if (client != null && chambre != null) {
                    reservations.add(new Reservation(id, client, chambre, dateArrivee, dateDepart, statut));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }
        return reservations;
    }

    // ===================== FACTURES =====================

    // Écrit toutes les factures dans le fichier CSV en écrasant le contenu précédent
    public void sauvegarderFactures(List<Facture> factures) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_FACTURES))) {
            writer.println("id;reservationId;montant;dateEmission;payee");
            for (Facture f : factures) {
                writer.println(f.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des factures : " + e.getMessage());
        }
    }

    // Lit le fichier CSV des factures et reconstruit les objets en liant les réservations par identifiant
    public List<Facture> chargerFactures(List<Reservation> reservations) {
        List<Facture> factures = new ArrayList<>();
        File fichier = new File(FICHIER_FACTURES);
        if (!fichier.exists()) return factures;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            reader.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(";");
                int id = Integer.parseInt(parts[0]);
                int resaId = Integer.parseInt(parts[1]);
                double montant = Double.parseDouble(parts[2].replace(",", "."));
                LocalDate dateEmission = LocalDate.parse(parts[3], FORMATTER);
                boolean payee = Boolean.parseBoolean(parts[4]);

                Reservation reservation = null;
                for (Reservation r : reservations) {
                    if (r.getId() == resaId) {
                        reservation = r;
                        break;
                    }
                }

                if (reservation != null) {
                    factures.add(new Facture(id, reservation, montant, dateEmission, payee));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des factures : " + e.getMessage());
        }
        return factures;
    }
}
