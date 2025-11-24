package dao;

import model.Transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple persistence layer that stores transactions in a text file.
 */
public class FileTransactionStore {
    private final Path storageFile;

    public FileTransactionStore(Path dataDir) throws IOException {
        Files.createDirectories(dataDir);
        this.storageFile = dataDir.resolve("transactions.txt");
        if (Files.notExists(storageFile)) {
            Files.createFile(storageFile);
        }
    }

    public void add(Transaction transaction) throws IOException {
        String record = transaction.toRecord() + System.lineSeparator();
        Files.writeString(storageFile, record, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

    public List<Transaction> findAll() throws IOException {
        List<String> lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            transactions.add(Transaction.fromRecord(line));
        }
        return transactions;
    }

    public double calculateBalance() throws IOException {
        return findAll().stream()
                .mapToDouble(t -> t.isExpense() ? -t.getAmount() : t.getAmount())
                .sum();
    }

    public double spentForCategory(String category) throws IOException {
        return findAll().stream()
                .filter(Transaction::isExpense)
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public String topSpendingCategory() throws IOException {
        Map<String, Double> totals = new HashMap<>();
        for (Transaction t : findAll()) {
            if (!t.isExpense()) {
                continue;
            }
            totals.merge(t.getCategory(), t.getAmount(), Double::sum);
        }
        return totals.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(entry -> entry.getKey() + " - " + String.format("%.2f", entry.getValue()))
                .orElse("No expenses recorded yet.");
    }

    public String report() throws IOException {
        List<Transaction> transactions = findAll();
        if (transactions.isEmpty()) {
            return "No transactions recorded.";
        }

        String txLines = transactions.stream()
                .map(t -> String.format("%s | %s | %s | %.2f | %s",
                        t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription()))
                .collect(Collectors.joining(System.lineSeparator()));

        double balance = calculateBalance();
        return "Balance: " + String.format("%.2f", balance) + System.lineSeparator() + txLines;
    }
}

