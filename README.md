# FinanceTracker - Budget Management System

FinanceTracker is a command-line application developed using **pure Java** (no external libraries). Designed for individual users, it enables tracking of financial inflows and outflows, implements category-based spending limits, and produces comprehensive financial insights. The system utilizes text-based file storage, maintaining simplicity while meeting academic project standards for modular design, architectural layering, and data analysis capabilities.

## Key Features
- **Financial Entry System**: log income sources and expenditure items with detailed notes; built-in safeguards prevent spending beyond available funds.
- **Spending Limit Controller**: define and modify monthly spending caps for different categories, with alerts generated when thresholds are approached.
- **Financial Insights Generator**: displays net balance, identifies highest expenditure areas, shows budget consumption metrics, and produces a complete transaction log.
- **Text-Based Data Storage**: all financial records and budget configurations are saved in `data/` directory, allowing straightforward data inspection and management.
- **Modular Code Organization**: codebase is divided into distinct packages (`model`, `dao`, `service`, `main`) ensuring clear separation of responsibilities.

## Technology Used
- Java Development Kit 17 or higher (Standard Edition)
- Built exclusively with JDK built-in libraries (file I/O, collections framework)
- PlantUML diagramming tool (diagram specifications located in `docs/diagrams`)

## Project Structure
```
src/
  model/Transaction.java
  dao/FileTransactionStore.java
  service/BudgetManager.java
  service/AnalyticsService.java
  service/BudgetSnapshot.java
  main/App.java
data/
  transactions.txt
  budgets.txt
docs/
  diagrams/*.puml
  report.md
statement.md
```

## Installation & Execution
1. Verify Java 17 or newer is installed and accessible via command line.
2. Download or clone this repository, then navigate to the project root directory.
3. Compile all source files:
   ```
   javac -d out src/model/*.java src/dao/*.java src/service/*.java src/main/App.java
   ```
4. Launch the application:
   ```
   java -cp out main.App
   ```
5. The `data/` directory will be automatically generated upon first execution.

## Testing Procedures
- Validation is conducted through interactive command-line testing. Recommended test cases:
  - Record a financial inflow, then add an outflow that doesn't exceed available funds (expected: successful entry).
  - Try to log an expense that exceeds current balance (expected: operation blocked).
  - Establish a category limit and attempt to spend beyond it (expected: warning message with user confirmation prompt).
  - Access insights and reports to verify that computed values update correctly.

## Project Documentation
- `statement.md`: contains the problem definition, project boundaries, intended user base, and high-level functionality overview.
- `docs/diagrams/`: contains PlantUML source files for Use Case, Class, Sequence, Workflow, and Entity-Relationship diagrams.
- `docs/report.md`: comprehensive project documentation following the required submission format.

## Planned Improvements
- Implement data export functionality (CSV/PDF formats).
- Support multi-user access with login mechanisms.
- Develop a graphical user interface (Swing/JavaFX) or web-based frontend utilizing existing service layer.

