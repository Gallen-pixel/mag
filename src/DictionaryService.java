import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The Dictionary Service Class
public class DictionaryService {
    private Map<String, String> dictionary;
    private List<String> invalidRawLines; // Храним невалидные строки для сохранения в файл
    private String filePath;
    private DictionaryValidator validator;

    public DictionaryService(String filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;
        this.dictionary = new HashMap<>();
        this.invalidRawLines = new ArrayList<>();
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
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (validator.isValidKey(key)) {
                        dictionary.put(key, value);
                    } else {
                        // Сохраняем невалидную строку, но не добавляем в словарь
                        invalidRawLines.add(line);
                        System.err.println("Warning: Skipping invalid entry - key '" + key + "' does not match validation rules of " + validator.getLanguageName());
                    }
                } else {
                    // Сохраняем плохо сформированную строку
                    invalidRawLines.add(line);
                    System.err.println("Warning: Skipping malformed line: " + line);
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Save data to disk
    private void saveToFile() {
        try {
            List<String> linesToWrite = new ArrayList<>(
                    dictionary.entrySet().stream()
                            .map(entry -> entry.getKey() + ";" + entry.getValue())
                            .toList()
            );

            // Добавляем невалидные строки, но пропускаем те, чьи ключи теперь валидны
            invalidRawLines.stream()
                    .filter(invalidLine -> {
                        String[] parts = invalidLine.split(";", 2);
                        if (parts.length != 2) return true; // Сохраняем плохо сформированные строки
                        String key = parts[0].trim();
                        return !dictionary.containsKey(key); // Сохраняем только если ключ не в валидном словаре
                    })
                    .forEach(linesToWrite::add);

            Files.write(Paths.get(filePath), linesToWrite);
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