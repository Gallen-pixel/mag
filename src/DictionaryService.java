import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// The Dictionary Service Class
public class DictionaryService {
    private Map<String, String> dictionary;
    private String filePath;
    private DictionaryValidator validator;

    public DictionaryService(String filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;
        this.dictionary = new HashMap<>();
        loadFromFile();
    }

    // Load data from disk
    private void loadFromFile() {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return; // File doesn't exist yet, start empty
        }

        try {
            Files.lines(path).forEach(line -> {
                String[] parts = line.split(";", 2); // Split by semicolon
                if (parts.length == 2) {
                    dictionary.put(parts[0].trim(), parts[1].trim());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Save data to disk
    private void saveToFile() {
        try {
            Files.write(Paths.get(filePath), dictionary.entrySet().stream()
                    .map(entry -> entry.getKey() + ";" + entry.getValue())
                    .toList());
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    // Add entry
    public boolean addEntry(String key, String value) {
        if (!validator.isValidKey(key)) {
            return false;
        }
        dictionary.put(key, value);
        saveToFile();
        return true;
    }

    // Delete entry
    public boolean deleteEntry(String key) {
        if (dictionary.remove(key) != null) {
            saveToFile();
            return true;
        }
        return false;
    }

    // Search entry
    public String searchEntry(String key) {
        return dictionary.get(key);
    }

    // Get all entries
    public Map<String, String> getAllEntries() {
        return new HashMap<>(dictionary);
    }

    public DictionaryValidator getValidator() {
        return validator;
    }
}