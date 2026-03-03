# Que fais mon projet
Création de la gestion d'un hotel en Java développé pour console. Permet de gérer les chambres, les clients, les réservations et la facturation avec une persistance des données en CSV.

## Fonctionnalités

- Gestion des chambres (simple, double, suite) avec tarifs et disponibilités
- Ajout et recherche de clients
- Création, modification et annulation de réservations
- Génération et paiement de factures
- Statistiques (taux d'occupation, chiffre d'affaires)

## Structure
- model
  - Chambre.java — classe abstraite de base pour les chambres
  - ChambreSimple.java — chambre simple, pas de réduction
  - ChambreDouble.java — chambre double, -5% dès 5 nuits
  - Suite.java — suite, -10% dès 3 nuits et -15% dès 7 nuits
  - Client.java — représente un client de l'hôtel
  - Reservation.java — lie un client à une chambre sur une période
  - Facture.java — facture générée à partir d'une réservation
  - TypeChambre.java — enum : SIMPLE, DOUBLE, SUITE
  - StatutReservation.java — enum : EN_ATTENTE, CONFIRMEE, ANNULEE
- service
  - HotelService.java — toute la logique métier
- storage
  - FichierStorage.java — lecture et écriture des fichiers CSV
- ui
  - MenuConsole.java — interface utilisateur en console



Les données sont sauvegardées automatiquement dans un dossier `data/` créé au premier démarrage.
