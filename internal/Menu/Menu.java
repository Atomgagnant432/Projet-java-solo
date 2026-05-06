package internal.Menu;

import internal.SpaceLauncher.ariane5;
import internal.SpaceLauncher.falcon9;
import internal.SpaceLauncher.saturnV;
import internal.SpaceLauncher.sls;
import internal.SpaceLauncher.spacelauncher;
import internal.boosters.BE3;
import internal.boosters.Booster;
import internal.boosters.EAP;
import internal.boosters.SRB;
import internal.capsules.apollo;
import internal.capsules.capsule;
import internal.capsules.cargodragon;
import internal.capsules.crewdragon;
import internal.capsules.orion;
import internal.missions.Iss;
import internal.missions.earthOrbite;
import internal.missions.mars;
import internal.missions.missions;
import internal.missions.moon;
import internal.missions.uranus;
import internal.rocket.rocket;
import internal.start;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    private static missions missionSelectionnee;
    private static rocket fusee;

    public static int printMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Simulateur de fusée ===");
            System.out.println("1) Choisir une mission");
            System.out.println("2) Configurer la fusée");
            System.out.println("3) Lancer la simulation");
            System.out.println("0) Quitter");
            System.out.print("Votre choix: ");

            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice < 0 || choice > 3) {
                    System.out.println("Choix invalide (0 à 3).");
                    continue;
                }
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide : veuillez entrer un nombre.");
            }
        }
    }

    public static void boucle() {
        while (true) {
            int choice = printMenu();
            switch (choice) {
                case 1 -> choisirMission();
                case 2 -> configurerFusee();
                case 3 -> lancerSimulation();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
            }
        }
    }

    private static int askInt(String prompt, int minInclusive, int maxInclusive) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < minInclusive || value > maxInclusive) {
                    System.out.println("Choix invalide (" + minInclusive + " à " + maxInclusive + ").");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide : veuillez entrer un nombre.");
            }
        }
    }

    private static void choisirMission() {
        System.out.println();
        System.out.println("=== Choisir une mission ===");
        System.out.println("1) Orbite terrestre (400 km)");
        System.out.println("2) ISS (400 km)");
        System.out.println("3) Lune (400 000 km)");
        System.out.println("4) Mars (225 000 000 km)");
        System.out.println("5) Uranus (315 491 000 km)");

        int choice = askInt("Votre choix: ", 1, 5);
        missionSelectionnee =
                switch (choice) {
                    case 1 -> new earthOrbite();
                    case 2 -> new Iss();
                    case 3 -> new moon();
                    case 4 -> new mars();
                    case 5 -> new uranus();
                    default -> throw new IllegalStateException("Choix mission invalide");
                };

        System.out.println("Mission sélectionnée: " + missionSelectionnee.getClass().getSimpleName());
    }

    private static void configurerFusee() {
        System.out.println();
        System.out.println("=== Configurer la fusée ===");

        spacelauncher launcher = choisirLauncher();
        capsule capsule = choisirCapsule();
        List<Booster> boosters = choisirBoosters(launcher.MaxBooster);

        fusee = new rocket(launcher, capsule, boosters);
        System.out.println("Fusée configurée.");
        afficherPoidsEtCapacites();
        System.out.println("Prix fusée (€): " + fusee.getRocketPrice());
        System.out.println("Boosters: " + fusee.getBoosterCount() + "/" + launcher.MaxBooster);
    }

    private static spacelauncher choisirLauncher() {
        System.out.println("Choisir un lanceur:");
        System.out.println("1) Ariane 5");
        System.out.println("2) Falcon 9");
        System.out.println("3) Saturn V");
        System.out.println("4) SLS");

        int choice = askInt("Votre choix: ", 1, 4);
        return switch (choice) {
            case 1 -> new ariane5();
            case 2 -> new falcon9();
            case 3 -> new saturnV();
            case 4 -> new sls();
            default -> throw new IllegalStateException("Choix lanceur invalide");
        };
    }

    private static capsule choisirCapsule() {
        System.out.println("Choisir une capsule:");
        System.out.println("1) Apollo");
        System.out.println("2) Crew Dragon");
        System.out.println("3) Cargo Dragon");
        System.out.println("4) Orion");

        int choice = askInt("Votre choix: ", 1, 4);
        return switch (choice) {
            case 1 -> new apollo();
            case 2 -> new crewdragon();
            case 3 -> new cargodragon();
            case 4 -> new orion();
            default -> throw new IllegalStateException("Choix capsule invalide");
        };
    }

    private static List<Booster> choisirBoosters(int maxBoosters) {
        List<Booster> boosters = new ArrayList<>();
        if (maxBoosters <= 0) {
            return boosters;
        }

        System.out.println("Configurer les boosters (max " + maxBoosters + "):");
        int count = askInt("Combien de boosters (0 à " + maxBoosters + "): ", 0, maxBoosters);
        for (int i = 1; i <= count; i++) {
            System.out.println("Booster #" + i + ":");
            System.out.println("1) BE-3");
            System.out.println("2) EAP");
            System.out.println("3) SRB");
            int choice = askInt("Votre choix: ", 1, 3);
            Booster booster =
                    switch (choice) {
                        case 1 -> new BE3();
                        case 2 -> new EAP();
                        case 3 -> new SRB();
                        default -> throw new IllegalStateException("Choix booster invalide");
                    };
            boosters.add(booster);
        }
        return boosters;
    }

    private static void lancerSimulation() {
        System.out.println();
        System.out.println("=== Lancer la simulation ===");

        if (missionSelectionnee == null) {
            System.out.println("Aucune mission sélectionnée. Choisis d'abord une mission (menu 1).");
            return;
        }
        if (fusee == null) {
            System.out.println("Aucune fusée configurée. Configure d'abord la fusée (menu 2).");
            return;
        }

        afficherPoidsEtCapacites();

        boolean succes = true;
        String raison = "";

        if (missionSelectionnee.PeopleNeeded != null && missionSelectionnee.PeopleNeeded) {
            if (!fusee.getCapsule().PeopleIn) {
                succes = false;
                raison =
                        "La mission nécessite des astronautes, mais la capsule ne transporte personne.";
            }
        }

        if (succes) {
            double carburant = fusee.getRequiredFuel(missionSelectionnee);
            if (carburant > fusee.getLauncher().MaxFuel) {
                succes = false;
                raison =
                        "Carburant nécessaire (" + carburant + ") > capacité MaxFuel (" + fusee.getLauncher().MaxFuel + ").";
            }
        }

        start lancement = start.fromSimulation(fusee, missionSelectionnee, succes, raison);
        System.out.println(lancement);
    }

    private static void afficherPoidsEtCapacites() {
        if (fusee == null) {
            return;
        }

        System.out.println();
        System.out.println("--- Poids & capacités ---");
        System.out.println("Lanceur: " + fusee.getLauncher().getClass().getSimpleName());
        System.out.println("  Payload max supporté: " + fusee.getLauncher().payload);
        System.out.println("  Carburant max (MaxFuel): " + fusee.getLauncher().MaxFuel);
        System.out.println("Capsule: " + fusee.getCapsule().getClass().getSimpleName());
        System.out.println("  Poids capsule: " + fusee.getCapsule().Weight);

        if (fusee.getBoosters().isEmpty()) {
            System.out.println("Boosters: aucun");
        } else {
            System.out.println("Boosters:");
            int index = 1;
            for (Booster booster : fusee.getBoosters()) {
                System.out.println(
                        "  #" + index + " " + booster.getClass().getSimpleName() + " - poids: " + booster.getWeight());
                index++;
            }
        }

        System.out.println("Poids total (capsule + boosters): " + fusee.getTotalMass());
        System.out.println("-------------------------");
        System.out.println();
    }
}
