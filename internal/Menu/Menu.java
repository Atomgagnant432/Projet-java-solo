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
    private static final double RANDOM_FAILURE_PROBABILITY = 0.05;

    private static missions missionSelectionnee;
    private static rocket fusee;

    public static int printMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Simulateur de fusee ===");
            System.out.println("1) Choisir une mission");
            System.out.println("2) Configurer la fusee");
            System.out.println("3) Lancer la simulation");
            System.out.println("0) Quitter");
            System.out.print("Votre choix: ");

            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice < 0 || choice > 3) {
                    System.out.println("Choix invalide (0 a 3).");
                    continue;
                }
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Entree invalide : veuillez entrer un nombre.");
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
                    System.out.println("Choix invalide (" + minInclusive + " a " + maxInclusive + ").");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Entree invalide : veuillez entrer un nombre.");
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
                    default -> throw new IllegalStateException("Invalid mission choice");
                };

        System.out.println("Mission selectionnee: " + missionSelectionnee.getClass().getSimpleName());
    }

    private static void configurerFusee() {
        System.out.println();
        System.out.println("=== Configurer la fusee ===");

        spacelauncher launcher = choisirLauncher();
        capsule capsule = choisirCapsule();
        List<Booster> boosters = choisirBoosters(launcher, capsule);

        fusee = new rocket(launcher, capsule, boosters);
        System.out.println("Fusee configuree.");
        afficherPoidsEtCapacites();
        System.out.println("Prix fusee (EUR): " + fusee.getRocketPrice());
        System.out.println("Boosters: " + fusee.getBoosterCount() + "/" + launcher.MaxBooster);
    }

    private static spacelauncher choisirLauncher() {
        System.out.println("Choisir un lanceur:");
        System.out.println("1) Ariane 5 (charge utile max: 20 t)");
        System.out.println("2) Falcon 9 (charge utile max: 22 t)");
        System.out.println("3) Saturn V (charge utile max: 140 t)");
        System.out.println("4) SLS (charge utile max: 130 t)");

        int choice = askInt("Votre choix: ", 1, 4);
        return switch (choice) {
            case 1 -> new ariane5();
            case 2 -> new falcon9();
            case 3 -> new saturnV();
            case 4 -> new sls();
            default -> throw new IllegalStateException("Invalid launcher choice");
        };
    }

    private static capsule choisirCapsule() {
        System.out.println("Choisir une capsule:");
        System.out.println("1) Apollo (poids: 5.6 t)");
        System.out.println("2) Crew Dragon (poids: 12.0 t)");
        System.out.println("3) Cargo Dragon (poids: 9.5 t)");
        System.out.println("4) Orion (poids: 10.4 t)");

        int choice = askInt("Votre choix: ", 1, 4);
        return switch (choice) {
            case 1 -> new apollo();
            case 2 -> new crewdragon();
            case 3 -> new cargodragon();
            case 4 -> new orion();
            default -> throw new IllegalStateException("Invalid capsule choice");
        };
    }

    private static List<Booster> choisirBoosters(spacelauncher launcher, capsule capsule) {
        List<Booster> boosters = new ArrayList<>();
        if (launcher.MaxBooster <= 0) {
            return boosters;
        }

        System.out.println("Configurer les boosters (max " + launcher.MaxBooster + "):");
        System.out.println("Charge utile max supportee: " + launcher.payload + " t");
        System.out.println("Poids capsule: " + capsule.Weight + " t");
        int count = askInt("Combien de boosters (0 a " + launcher.MaxBooster + "): ", 0, launcher.MaxBooster);

        for (int i = 1; i <= count; i++) {
            while (true) {
                double currentMass = capsule.Weight;
                for (Booster b : boosters) {
                    currentMass += b.getWeight();
                }
                double remaining = launcher.payload - currentMass;

                System.out.println();
                System.out.println("Booster #" + i + " (marge charge utile: " + remaining + " t):");
                System.out.println("0) Annuler (arreter d'ajouter des boosters)");
                System.out.println("1) BE-3 (25 t)");
                System.out.println("2) EAP (270 t)");
                System.out.println("3) SRB (590 t)");

                int choice = askInt("Votre choix: ", 0, 3);
                if (choice == 0) {
                    return boosters;
                }
                Booster booster =
                        switch (choice) {
                            case 1 -> new BE3();
                            case 2 -> new EAP();
                            case 3 -> new SRB();
                            default -> throw new IllegalStateException("Invalid booster choice");
                        };

                double nextMass = currentMass + booster.getWeight();
                if (nextMass > launcher.payload) {
                    System.out.println(
                            "Surcharge: poids total deviendrait " + nextMass + " t (max " + launcher.payload + " t). Choisis un autre booster.");
                    continue;
                }

                boosters.add(booster);
                break;
            }
        }
        return boosters;
    }

    private static void lancerSimulation() {
        System.out.println();
        System.out.println("=== Lancer la simulation ===");

        if (missionSelectionnee == null) {
            System.out.println("Aucune mission selectionnee. Choisis d'abord une mission (menu 1).");
            return;
        }
        if (fusee == null) {
            System.out.println("Aucune fusee configuree. Configure d'abord la fusee (menu 2).");
            return;
        }

        afficherPoidsEtCapacites();

        boolean succes = true;
        String raison = "";

        if (missionSelectionnee.PeopleNeeded != null && missionSelectionnee.PeopleNeeded) {
            if (!fusee.getCapsule().PeopleIn) {
                succes = false;
                raison = "Capsule incompatible avec une mission habitee";
            }
        }

        if (succes) {
            if (fusee.getBoosterCount() > fusee.getLauncher().MaxBooster) {
                succes = false;
                raison = "Trop de boosters";
            }
        }

        if (succes) {
            double mass = fusee.getTotalMass();
            if (mass > fusee.getLauncher().payload) {
                succes = false;
                raison = "Surcharge depassee (" + mass + " t > " + fusee.getLauncher().payload + " t)";
            }
        }

        if (succes) {
            double carburant = fusee.getRequiredFuel(missionSelectionnee);
            if (carburant > fusee.getLauncher().MaxFuel) {
                succes = false;
                raison = "Carburant insuffisant";
            }
        }

        if (succes) {
            double roll = Math.random();
            if (roll < RANDOM_FAILURE_PROBABILITY) {
                succes = false;
                raison = "Anomalie technique imprevue";
            }
        }

        start lancement = start.fromSimulation(fusee, missionSelectionnee, succes, raison);
        System.out.println(lancement);
    }

    private static void afficherPoidsEtCapacites() {
        if (fusee == null) {
            return;
        }

        double totalMass = fusee.getTotalMass();
        double payloadMax = fusee.getLauncher().payload;
        double margin = payloadMax - totalMass;

        System.out.println();
        System.out.println("--- Poids & capacites ---");
        System.out.println(
                "Lanceur: "
                        + fusee.getLauncher().getClass().getSimpleName()
                        + " (charge utile max: "
                        + payloadMax
                        + " t)");
        System.out.println("  Carburant max: " + fusee.getLauncher().MaxFuel + " t");
        System.out.println(
                "Capsule: "
                        + fusee.getCapsule().getClass().getSimpleName()
                        + " (poids: "
                        + fusee.getCapsule().Weight
                        + " t)");

        if (fusee.getBoosters().isEmpty()) {
            System.out.println("Boosters: aucun");
        } else {
            System.out.println("Boosters:");
            int index = 1;
            for (Booster booster : fusee.getBoosters()) {
                System.out.println(
                        "  #"
                                + index
                                + " "
                                + booster.getClass().getSimpleName()
                                + " (poids: "
                                + booster.getWeight()
                                + " t)");
                index++;
            }
        }

        System.out.println("Poids total (capsule + boosters): " + totalMass + " t");
        System.out.println("Marge charge utile: " + margin + " t");
        System.out.println("-------------------------");
        System.out.println();
    }
}
