package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a single income or expense entry saved in the flat-file store.
 */
public class Transaction {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String id;
    private final double amount;
    private final String type;
    private final String category;
    private final String description;
    private final LocalDate date;

    public Transaction(double amount, String type, String category, String description) {
        this(UUID.randomUUID().toString(), amount, type, category, description, LocalDate.now());
    }

    public Transaction(String id, double amount, String type, String category, String description, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isExpense() {
        return "EXPENSE".equalsIgnoreCase(type);
    }

    public String toRecord() {
        return String.join("|",
                id,
                FORMATTER.format(date),
                type,
                category,
                String.valueOf(amount),
                description == null ? "" : description.replace("|", "/"));
    }

    public static Transaction fromRecord(String recordLine) {
        String[] parts = recordLine.split("\\|", -1);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Corrupted transaction record: " + recordLine);
        }
        String id = parts[0];
        LocalDate date = LocalDate.parse(parts[1], FORMATTER);
        String type = parts[2];
        String category = parts[3];
        double amount = Double.parseDouble(parts[4]);
        String description = parts[5];
        return new Transaction(id, amount, type, category, description, date);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %.2f (%s)", date, type, amount, category);
    }
}

