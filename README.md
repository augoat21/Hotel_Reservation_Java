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
  - Chambre.java classe abstraite de base pour les chambres
  - ChambreSimple.java chambre simple, pas de réduction
  - ChambreDouble.java chambre double, -5% dès 5 nuits
  - Suite.java suite, -10% dès 3 nuits et -15% dès 7 nuits
  - Client.java représente un client de l'hôtel
  - Reservation.java lie un client à une chambre sur une période
  - Facture.java crée une facture générée à partir d'une réservation
  - TypeChambre.java donne le type de la chambre : SIMPLE, DOUBLE, SUITE
  - StatutReservation.java met le statut de la réservation : EN_ATTENTE, CONFIRMEE, ANNULEE
- service
  - HotelService.java toute la logique métier
- storage
  - FichierStorage.java lecture et écriture des fichiers CSV
- ui
  - MenuConsole.java interface utilisateur en console

## Commentaires 

J'ai pris l'habitude de commenter un maximum mon code pour qu'il soit lisible et compréhensible par n'importe qui, même quelqu'un qui découvre le projet. C'est aussi une bonne pratique que j'ai apprise en regardant des projets open source sur GitHub, où le code est toujours bien documenté pour faciliter la collaboration. Pour certaines parties comme l'interface Pygame que je ne connaissais pas bien, je me suis aidé de la documentation, de tutoriels et aussi d'outils d'IA pour comprendre comment fonctionnent certaines fonctions.Les commentaires me servaient justement à mémoriser certaines fonctions quand je relierais mon code plus tard.
