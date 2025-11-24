package service;

import dao.FileTransactionStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides analytics operations by combining transaction and budget data.
 */
public class AnalyticsService {
    private final FileTransactionStore transactionStore;
    private final BudgetManager budgetManager;

    public AnalyticsService(FileTransactionStore transactionStore, BudgetManager budgetManager) {
        this.transactionStore = transactionStore;
        this.budgetManager = budgetManager;
    }

    public double currentBalance() throws IOException {
        return transactionStore.calculateBalance();
    }

    public String topSpendingCategory() throws IOException {
        return transactionStore.topSpendingCategory();
    }

    public Map<String, BudgetSnapshot> budgetSnapshots() throws IOException {
        Map<String, Double> budgets = budgetManager.loadBudgets();
        Map<String, BudgetSnapshot> snapshots = new HashMap<>();
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            double spent = transactionStore.spentForCategory(entry.getKey());
            snapshots.put(entry.getKey(), new BudgetSnapshot(entry.getKey(), entry.getValue(), spent));
        }
        return snapshots;
    }

    public String fullReport() throws IOException {
        return transactionStore.report();
    }
}

