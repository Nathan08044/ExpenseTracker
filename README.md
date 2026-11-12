## Overview

Expense Tracker is a JavaFX desktop app for recording income and expenses.
It stores transactions locally and shows income and spending trends over the last six months.

## Features

- Add and delete income and expense transactions.
- Filter transactions by month and sort them by date.
- Compare monthly income and expenses in a chart. Hover over a point to see its amount.
- View total income, total expenses, and current balance.

## Run locally

1. Open the project in IntelliJ IDEA.
2. Let IntelliJ import the Maven project from `pom.xml`.
3. Open `Launcher.java` and click the green Run arrow next to `main`.
4. ![Expense Tracker dashboard](docs/dashboard.png)
## Scan a receipt

Install Tesseract OCR with English language data to use **Scan receipt** (on macOS with Homebrew: `brew install tesseract`). On other platforms, make sure the `tesseract` command is on your PATH.

Choose a PNG or JPEG receipt. The scan runs locally and suggests a merchant and total in empty form fields. The scanner also fills the date when it finds one unambiguous U.S. date (month/day/four-digit year). Review the fields and set the category and transaction type before clicking **Add transaction**. If the date is missing or ambiguous, the existing date stays in place and the app asks you to check it. If the total is missing or ambiguous, enter it manually.

## Build a Mac installer

On the build Mac, install JDK 21, Python 3.9+, Xcode Command Line Tools and Homebrew Tesseract. Run `bash scripts/package-macos.sh` to build a DMG under `target/dist`. The installer bundles Java and English OCR for the build Mac's architecture. Before sharing it, test launch and receipt scanning on a Mac without Homebrew. Public distribution may require Developer ID signing and notarization.
