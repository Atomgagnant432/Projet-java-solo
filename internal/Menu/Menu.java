package internal.Menu;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

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
                case 1 -> System.out.println("TODO: sélection mission");
                case 2 -> System.out.println("TODO: configuration fusée");
                case 3 -> System.out.println("TODO: lancement simulation");
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
            }
        }
    }
}
