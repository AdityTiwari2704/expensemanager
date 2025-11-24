package service;

/**
 * Immutable snapshot of budget usage for a category.
 */
public class BudgetSnapshot {
    private final String category;
    private final double limit;
    private final double spent;

    public BudgetSnapshot(String category, double limit, double spent) {
        this.category = category;
        this.limit = limit;
        this.spent = spent;
    }

    public String getCategory() {
        return category;
    }

    public double getLimit() {
        return limit;
    }

    public double getSpent() {
        return spent;
    }

    public double getRemaining() {
        return limit - spent;
    }
}

