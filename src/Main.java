import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the two dictionaries with their specific files and validators
        DictionaryService dict1 = new DictionaryService("dict_latin4.txt", new Latin4Validator());
        DictionaryService dict2 = new DictionaryService("dict_digit5.txt", new Digit5Validator());

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the Dictionary System!");

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Work with Dictionary 1 (" + dict1.getValidator().getLanguageName() + ")");
            System.out.println("2. Work with Dictionary 2 (" + dict2.getValidator().getLanguageName() + ")");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput(scanner);

            DictionaryService currentDict = null;

            if (choice == 1) {
                currentDict = dict1;
            } else if (choice == 2) {
                currentDict = dict2;
            } else if (choice == 0) {
                running = false;
                continue;
            } else {
                System.out.println("Invalid option.");
                continue;
            }

            handleDictionaryMenu(scanner, currentDict);
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
                case 1:
                    viewAll(dict);
                    break;
                case 2:
                    search(scanner, dict);
                    break;
                case 3:
                    add(scanner, dict);
                    break;
                case 4:
                    delete(scanner, dict);
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid action.");
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
        System.out.print("Enter Key (must match rules): ");
        String key = scanner.nextLine();
        System.out.print("Enter Value (Russian translation): ");
        String value = scanner.nextLine();

        if (dict.addEntry(key, value)) {
            System.out.println("Entry added successfully!");
        } else {
            System.out.println("Error: Invalid Key format!");
            System.out.println("Rules for this dictionary: " + dict.getValidator().getLanguageName());
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

    // Helper to handle integer input safely
    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number.");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }
}