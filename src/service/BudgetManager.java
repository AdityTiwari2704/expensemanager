package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles saving and loading category budget limits in a plain text file.
 */
public class BudgetManager {
    private final Path budgetsFile;

    public BudgetManager(Path dataDir) throws IOException {
        Files.createDirectories(dataDir);
        this.budgetsFile = dataDir.resolve("budgets.txt");
        if (Files.notExists(budgetsFile)) {
            Files.createFile(budgetsFile);
        }
    }

    public Map<String, Double> loadBudgets() throws IOException {
        List<String> lines = Files.readAllLines(budgetsFile, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Double> map = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                map.put(parts[0], Double.parseDouble(parts[1]));
            }
        }
        return map;
    }

    public void setBudget(String category, double amount) throws IOException {
        Map<String, Double> budgets = new HashMap<>(loadBudgets());
        budgets.put(category, amount);
        saveBudgets(budgets);
    }

    public double budgetFor(String category) throws IOException {
        return loadBudgets().getOrDefault(category, 0.0);
    }

    private void saveBudgets(Map<String, Double> budgets) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            builder.append(entry.getKey())
                    .append("|")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }
        Files.writeString(budgetsFile, builder.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}

