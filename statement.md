# CoinTrack – Statement

## Problem Statement
Individuals often rely on ad-hoc notes or memory to manage day-to-day spending, which makes it hard to understand where their money goes, stick to budgets, or justify expenses during financial reviews. A lightweight yet structured tracker is needed to capture every transaction, enforce simple limits, and surface actionable insights.

## Scope
CoinTrack is a single-user console application that:
- records incomes/expenses with categories and descriptions,
- stores entries in local flat files (no DB dependency),
- enforces per-category budgets and balance validation,
- generates analytics (balance, top spending category, printable report).

## Target Users
- Students managing allowances
- Early professionals tracking rent, food, commute
- Anyone who wants a no-frills, offline finance log

## High-Level Features
1. **Transaction Module** – capture income/expense entries, prevent overspending.
2. **Budget Manager** – configure category limits, warn when exceeded.
3. **Analytics Dashboard** – view balance, category breakdown, and text-based reports.

