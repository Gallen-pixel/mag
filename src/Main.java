import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Dictionary System Initialization ===");

        System.out.print("Enter file path for Dictionary 1: ");
        String path1 = scanner.nextLine();

        System.out.print("Enter file path for Dictionary 2: ");
        String path2 = scanner.nextLine();

        // Initialize services with user-provided paths
        DictionaryService dict1 = new DictionaryService(path1, new Latin4Validator());
        DictionaryService dict2 = new DictionaryService(path2, new Digit5Validator());

        boolean running = true;
        System.out.println("\nSystem started successfully.");
        System.out.println("Dict 1 file: " + path1);
        System.out.println("Dict 2 file: " + path2);

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Work with Dictionary 1 (" + dict1.getValidator().getLanguageName() + ")");
            System.out.println("2. Work with Dictionary 2 (" + dict2.getValidator().getLanguageName() + ")");
            System.out.println("3. View contents of BOTH dictionaries");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput(scanner);

            if (choice == 1) {
                handleDictionaryMenu(scanner, dict1);
            } else if (choice == 2) {
                handleDictionaryMenu(scanner, dict2);
            } else if (choice == 3) {
                System.out.println("\n--- Contents of Dictionary 1 ---");
                viewAll(dict1);
                System.out.println("\n--- Contents of Dictionary 2 ---");
                viewAll(dict2);
            } else if (choice == 0) {
                running = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void handleDictionaryMenu(Scanner scanner, DictionaryService dict) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Working with: " + dict.getValidator().getLanguageName() + " ---");
            System.out.println("1. View all entries");
            System.out.println("2. Search by key");
            System.out.println("3. Add new entry");
            System.out.println("4. Delete entry");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select action: ");

            int action = getIntInput(scanner);

            switch (action) {
                case 1: viewAll(dict); break;
                case 2: search(scanner, dict); break;
                case 3: add(scanner, dict); break;
                case 4: delete(scanner, dict); break;
                case 0: inMenu = false; break;
                default: System.out.println("Invalid action.");
            }
        }
    }

    private static void viewAll(DictionaryService dict) {
        Map<String, String> entries = dict.getAllEntries();
        if (entries.isEmpty()) {
            System.out.println("Dictionary is empty.");
        } else {
            System.out.println("Key\t\tValue");
            System.out.println("-------------------------");
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                System.out.println(entry.getKey() + "\t\t" + entry.getValue());
            }
        }
    }

    private static void search(Scanner scanner, DictionaryService dict) {
        System.out.print("Enter key to search: ");
        String key = scanner.nextLine();
        String value = dict.searchEntry(key);
        if (value != null) {
            System.out.println("Found: " + key + " -> " + value);
        } else {
            System.out.println("Entry not found.");
        }
    }

    private static void add(Scanner scanner, DictionaryService dict) {
        System.out.print("Enter Key: ");
        String key = scanner.nextLine();
        System.out.print("Enter Value (translation): ");
        String value = scanner.nextLine();

        if (dict.addEntry(key, value)) {
            System.out.println("Entry added successfully!");
        } else {
            System.out.println("Error: Invalid Key format!");
            System.out.println("Required format: " + dict.getValidator().getLanguageName());
        }
    }

    private static void delete(Scanner scanner, DictionaryService dict) {
        System.out.print("Enter key to delete: ");
        String key = scanner.nextLine();
        if (dict.deleteEntry(key)) {
            System.out.println("Entry deleted.");
        } else {
            System.out.println("Entry not found.");
        }
    }

    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }
}