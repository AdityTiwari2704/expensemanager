package main;

import dao.FileTransactionStore;
import model.Transaction;
import service.AnalyticsService;
import service.BudgetManager;
import service.BudgetSnapshot;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * Console entry-point for the simplified CoinTrack application.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            FileTransactionStore store = new FileTransactionStore(Paths.get("data"));
            BudgetManager budgetManager = new BudgetManager(Paths.get("data"));
            AnalyticsService analyticsService = new AnalyticsService(store, budgetManager);
            runMenu(scanner, store, budgetManager, analyticsService);
        } catch (IOException e) {
            System.out.println("Failed to start CoinTrack: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runMenu(Scanner scanner, FileTransactionStore store,
                                BudgetManager budgetManager, AnalyticsService analyticsService) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner);
            try {
                switch (choice) {
                    case 1 -> addTransaction(scanner, store, budgetManager, analyticsService, "INCOME");
                    case 2 -> addTransaction(scanner, store, budgetManager, analyticsService, "EXPENSE");
                    case 3 -> setBudget(scanner, budgetManager);
                    case 4 -> showAnalytics(analyticsService);
                    case 5 -> showReport(analyticsService);
                    case 6 -> running = false;
                    default -> System.out.println("Unknown option. Try again.");
                }
            } catch (IOException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
        System.out.println("Thanks for using CoinTrack!");
    }

    private static void printMenu() {
        System.out.println("\n=== CoinTrack ===");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. Set/Update Budget Limit");
        System.out.println("4. View Analytics");
        System.out.println("5. View Full Report");
        System.out.println("6. Exit");
        System.out.print("Choose option: ");
    }

    private static void addTransaction(Scanner scanner, FileTransactionStore store,
                                       BudgetManager budgetManager, AnalyticsService analyticsService,
                                       String type) throws IOException {
        System.out.print("Category: ");
        String category = scanner.next().trim();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Short description: ");
        String description = scanner.nextLine();

        if ("EXPENSE".equals(type)) {
            double balance = analyticsService.currentBalance();
            if (amount > balance) {
                System.out.println("Cannot spend more than current balance (" + balance + ").");
                return;
            }
            double limit = budgetManager.budgetFor(category);
            if (limit > 0) {
                double spent = store.spentForCategory(category);
                if (spent + amount > limit) {
                    System.out.printf(Locale.US,
                            "Warning: This expense exceeds the %s budget (limit %.2f, already spent %.2f).%n",
                            category, limit, spent);
                    System.out.print("Continue anyway? (y/n): ");
                    String confirmation = scanner.next().trim().toLowerCase(Locale.ROOT);
                    if (!confirmation.equals("y")) {
                        System.out.println("Expense cancelled.");
                        return;
                    }
                    scanner.nextLine(); // consume leftover newline
                }
            }
        }

        Transaction transaction = new Transaction(amount, type, category, description);
        store.add(transaction);
        System.out.println("Saved: " + transaction);
    }

    private static void setBudget(Scanner scanner, BudgetManager budgetManager) throws IOException {
        System.out.print("Category to limit: ");
        String category = scanner.next().trim();
        System.out.print("Monthly limit: ");
        double limit = scanner.nextDouble();
        budgetManager.setBudget(category, limit);
        System.out.println("Budget saved for " + category);
    }

    private static void showAnalytics(AnalyticsService analyticsService) throws IOException {
        double balance = analyticsService.currentBalance();
        System.out.println("Current balance: " + String.format(Locale.US, "%.2f", balance));
        System.out.println("Top spending category: " + analyticsService.topSpendingCategory());

        Map<String, BudgetSnapshot> budgets = analyticsService.budgetSnapshots();
        if (budgets.isEmpty()) {
            System.out.println("No budgets configured.");
        } else {
            System.out.println("Budgets:");
            for (BudgetSnapshot snapshot : budgets.values()) {
                System.out.printf(Locale.US, "- %s: limit %.2f | spent %.2f | remaining %.2f%n",
                        snapshot.getCategory(), snapshot.getLimit(), snapshot.getSpent(), snapshot.getRemaining());
            }
        }
    }

    private static void showReport(AnalyticsService analyticsService) throws IOException {
        System.out.println(analyticsService.fullReport());
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Enter a number.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }
}

