# CoinTrack Project Report

> This document follows the official VITyarthi template. Export it to PDF (or copy into Word/LaTeX) after inserting screenshots and diagram images generated from the provided PlantUML sources in `docs/diagrams/`.

## 1. Cover Page
- **Title:** CoinTrack – Personal Finance Manager  

- **Developed By:** Aditya Kumar Tiwari 24BSA10233 
 
- **Course: Programming in Java CSE2006

- **Faculty: Dr. Baseera

- **Submission Date: 25/11/25


## 2. Introduction

FinanceTracker represents a streamlined terminal-based solution designed to assist users in managing their personal financial activities without requiring complex software installations. This project showcases proficiency in fundamental Java programming through integration of file handling operations, object-oriented modeling techniques, computational data processing algorithms, and well-structured code organization. The initiative supports the course's flipped learning approach by enabling students to identify practical challenges, develop tiered solutions, and comprehensively document design choices. The following sections will demonstrate how the system incorporates key syllabus topics including data structures, error management strategies, and architectural separation principles.



## 3. Problem Statement

Individuals, particularly students and entry-level workers, often manage their finances using informal methods such as handwritten notes or mental calculations. This approach frequently results in uncontrolled spending and lack of insight into expenditure patterns across different categories. FinanceTracker solves this problem by providing an organized process for documenting financial activities, implementing spending controls, and producing detailed analytical reports—all functioning independently of network connectivity and executable on any system equipped with the Java Development Kit. Key constraints include operating without internet requirements and delivering transparent analytical outputs appropriate for academic assessment purposes.


## 4. Functional Requirements


1. **Financial Entry Component (Input Layer):**  
   - Record revenue transactions including monetary value, classification, and explanatory text.  
   - Log cost transactions with integrated checks to prevent account deficit situations.  
   - Maintain persistent storage of all entries to support subsequent analysis operations.
2. **Spending Control Module (Processing Layer):**  
   - Accept and store monthly spending ceilings for each classification.  
   - Issue notifications or prevent transactions that violate category-specific limits or overall account balance.  
   - Dynamically retrieve and modify limit configurations from the `budgets.txt` storage file.
3. **Financial Analysis & Documentation Component (Output Layer):**  
   - Calculate net account balance through aggregation techniques (total revenue subtracted by total costs).  
   - Determine the classification with highest expenditure by organizing and ranking category totals.  
   - Produce a formatted multi-line document listing all recorded transactions with associated date-time information.  
The system's menu navigation flow should be clearly documented to help reviewers understand the complete user interaction path from initial selection through final output generation.

## 5. Non-functional Requirements


| Requirement | Description | Implementation |
|-------------|-------------|----------------|
| Usability | Simple numbered menu, contextual prompts, confirmations for risky operations | `main.App` guides the user through each step, validating numeric inputs |
| Reliability | Data should survive restarts and resist partial writes | Text files created upfront; writes use append or truncate semantics to avoid corruption |
| Maintainability | Code should be easy to extend with new modules | Packages (`model`, `dao`, `service`, `main`) isolate responsibilities |
| Error Handling | System must gracefully handle invalid categories, malformed data, or I/O failures | Extensive try/catch blocks with friendly messages; validation of budgets and balance |
| Performance (optional) | Even with hundreds of records, analytics should finish instantly | In-memory processing of the small dataset; efficient aggregation using streams |
| Portability (optional) | Run on Windows/Linux/macOS without extra dependencies | Pure JDK solution; flat files under project folder |

## 6. System Architecture


The system follows a three-layer architectural pattern:

- **User Interface Layer:** The command-line interface (`main.App`) manages option selection, performs input verification, and handles communication with the user.  
- **Business Logic Layer:** `BudgetManager` handles storage and retrieval of category spending limits; `AnalyticsService` coordinates system-wide computational tasks; `BudgetSnapshot` serves as a data transfer object.  
- **Persistence Layer:** `FileTransactionStore` converts `Transaction` instances into text format using pipe separators, storing them in `data/transactions.txt`, with budget information maintained in `data/budgets.txt`.  
Include or link to `docs/diagrams/system-architecture.puml` to illustrate the complete data flow from user interaction through to storage mechanisms.

## 7. Design Diagrams

Include high-resolution exports (PNG/SVG) or reference the `.puml` sources:
- **Use Case Diagram:** Highlights actor interactions for adding transactions, managing budgets, and viewing analytics.  
- **Workflow Diagram:** Shows the looped menu and branching paths for each feature.  
- **Sequence Diagram (Add Expense):** Details the call order between UI, services, and DAO while validating budgets.  
- **Class Diagram:** Documents all classes, key attributes, and relationships to prove the “5+ modules” requirement.  
- **ER Diagram:** Represents `transactions.txt` and `budgets.txt` as entities with relationships, satisfying storage design expectations.

## 8. Design Decisions & Rationale

- **Text-based storage approach** simplifies project setup, allowing reviewers to execute the application without database installations or driver configurations.  
- **Separation between data access and business logic layers** enables the application layer to function with alternative storage mechanisms (such as relational databases) without modifying user interface components.  
- **Universally unique identifier system** ensures transaction entries remain distinct even when files are modified across different computing environments.  
- **PlantUML diagramming tool** was selected for its text-based source format that integrates with version control systems and allows easy updates compared to static image files.  
- **Pre-validation approach** catches data inconsistencies prior to disk writes, enhancing overall system dependability.

## 9. Implementation Details

### Component Structure
- `model.Transaction`: contains transaction information, implements conversion between objects and text format, and provides utility methods for transaction types.  
- `dao.FileTransactionStore`: manages data operations (creation, retrieval, aggregation) utilizing Java's New I/O package functionality.  
- `service.BudgetManager`: performs loading and saving of spending limit configurations, guaranteeing the budget storage file is always present.  
- `service.AnalyticsService` and `service.BudgetSnapshot`: deliver computed insights and data transfer objects for presentation purposes.  
- `main.App`: serves as the command-line entry point, implementing the menu iteration loop, input cleaning procedures, and module coordination logic.

### Algorithms & Data Structures

- Data aggregation operations utilize Java Stream API to calculate revenue totals versus expenditure totals and determine category-specific sums.  
- Budget configurations are maintained in `Map<String, Double>` data structures for efficient retrieval, with snapshot objects presenting calculated values such as remaining budget allowances.  
- Input validation employs early-exit patterns to immediately halt operations that violate system constraints.

### Data Serialization

- Transaction records follow the format `id|date|type|category|amount|description`. Any pipe characters appearing in description fields are escaped to maintain parsing integrity.  
- Budget entries utilize the `category|limit` structure, enabling straightforward manual editing when required.

### Error Handling

- Every file input/output operation is wrapped within try-with-resources constructs to ensure automatic resource cleanup.  
- Numeric input validation continues prompting the user until valid number formats are provided.  
- Budget violation scenarios trigger warning messages requesting user approval before proceeding, creating an audit trail of override decisions.

## 10. Screenshots / Results

This section summarizes the observed CLI output during a representative run:

- **Figure 10.1 – Main Menu Preview:** console shows the numbered options (Add Income, Add Expense, Set Budget, View Analytics, View Report, Exit) confirming the clear input pathway.  
- **Figure 10.2 – Expense Validation Prompt:** when attempting to spend more than the current balance, the CLI responds with “Cannot spend more than current balance (₹X.XX)”, demonstrating the guard clause.  
- **Figure 10.3 – Analytics Summary:** the analytics option prints the formatted balance, top spending category, and a tabular view of each configured budget with limit/spent/remaining fields.  
- **Figure 10.4 – Report Output:** selecting the report option lists every transaction in the format `2025-11-24 | EXPENSE | Food | 250.00 | Dinner`, preceded by the aggregate balance line.

All figures are available under `docs/images/` for inclusion in the final PDF.

## 11. Testing Approach

- **Scenario Testing:**  
  - Add income followed by multiple expenses and confirm balance math.  
  - Attempt overspending to verify the hard stop.  
  - Set a tight budget and try exceeding it to trigger the warning/confirmation path.  
  - Run analytics/report after each change to ensure data reflects latest operations.  
- **Data Reset Strategy:** delete or archive files inside `data/` between runs to start fresh.  
- **Future Automation:** mention potential for JUnit tests around `FileTransactionStore` and `BudgetManager` by using temporary directories.

## 12. Challenges Faced

- Designing file-based serialization that remains human-readable yet resilient to malformed entries.  
- Ensuring CLI usability while keeping the code purely Core Java (no Spring/Swing).  
- Mapping rubric expectations (multi-module, analytics, diagrams, documentation) to a minimalist codebase without over-complicating it.  
- Time spent iterating on PlantUML diagrams to accurately reflect the evolving architecture.

## 13. Learnings & Key Takeaways

- Strengthened understanding of Java's New I/O package, immutable data structures, and abstraction patterns in service-oriented design.  
- Acquired skills in creating thorough system documentation covering problem statements, user guides, technical reports, and visual diagrams.  
- Learned to evaluate the advantages and disadvantages of database systems versus file-based storage, and how to design data access layers that facilitate future migration.  
- Applied validation techniques and computational analysis methods comparable to business rule implementations in commercial software systems.

## 14. Future Enhancements

- Transition data storage to a relational database system using JDBC connectivity to support concurrent multi-user access.  
- Implement user authentication mechanisms and profile management to expand beyond single-user operation.  
- Develop data export features supporting CSV and PDF formats, along with automated email reporting capabilities.  
- Create graphical interfaces using Swing or JavaFX, or develop web-based frontends that leverage existing service components.  
- Integrate visualization features such as spending trend graphs and intelligent budget recommendations.

## 15. References

1. Oracle Java SE Documentation – java.nio.file, java.time, java.util.stream.  
2. PlantUML Reference Guide – https://plantuml.com/ for diagram syntax.  
3. Course lecture slides on DAO patterns, modularization, and error handling.  
4. Any additional textbooks/articles on budgeting systems or software design (cite as needed).  
5. GitHub repository commits (self-reference) showing evolution of the project.

