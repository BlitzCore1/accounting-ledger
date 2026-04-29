package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingLedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions;

    static void main()
    {
        // make sure all transactions are loaded before we display the home screen
        transactions = loadTransactions();
        displayHomeScreen();
    }

    static void displayHomeScreen()
    {
        System.out.println("Welcome to the Accounting Ledger App!");
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("D) Make a Deposit");
        System.out.println("P) Make a Payment");
        System.out.println("L) View Ledger");
        System.out.println("X) Exit");
        System.out.print("Make a selection: ");

        String userInput = scanner.nextLine().toUpperCase().strip();

        System.out.println();
        switch (userInput.toUpperCase())
        {
            case "D":
                makeTransactionScreen(scanner, "D");
                break;
            case "P":
                makeTransactionScreen(scanner, "P");
                System.out.println("Create the Payment Screen");
                break;
            case "L":
                displayLedgerScreen();
                break;
            case "X":
                System.out.println("Goodbye!");
                // return exits the loop and the method, which in this case ends the application
                return;
            default:
                System.out.println("Invalid selection. Please try again.");
                displayHomeScreen();
        }
    }

    static ArrayList<Transaction> loadTransactions()
    {
        // load transactions from a file
        ArrayList<Transaction> transactions = new ArrayList<>();

        // populate the list
        try {
            FileReader fileReader = new FileReader("data/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine(); // read the header line and ignore it
            line = bufferedReader.readLine();

            while (line != null)
            {
                if (line.trim().isEmpty())
                {
                    line = bufferedReader.readLine();
                    continue;
                }
                String[] fields = line.split("\\|");
                Transaction transaction = new Transaction(
                        LocalDate.parse(fields[0]),
                        LocalTime.parse(fields[1]),
                        fields[2],
                        fields[3],
                        Double.parseDouble(fields[4])
                );
                transactions.add(transaction);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return transactions;
    }

    static void makeTransactionScreen(Scanner scanner, String transactionType)
    {
        String screenTitle;
        String amountLine;

        switch (transactionType.toUpperCase()) {
            case "D":
                screenTitle = "Make a Deposit";
                amountLine = "Enter the amount to deposit: ";
                break;
            case "P":
                screenTitle = "Make a Payment";
                amountLine = "Enter the amount to pay: ";
                break;
            default:
                System.out.println("Invalid transaction type. Returning to home screen.");
                displayHomeScreen();
                return;
        }

        System.out.println("\n=== " + screenTitle + " ===");
        System.out.println("--------------------");
        System.out.println();

        System.out.print("Enter a brief description: ");
        String description = scanner.nextLine().strip();

        System.out.print("Enter a vendor: ");
        String vendor = scanner.nextLine().strip();

        System.out.print(amountLine);
        double amount = Double.parseDouble(scanner.nextLine().strip());

        // if it's a payment, we want to store the amount as a negative number
        if (transactionType.equalsIgnoreCase("P"))
        {
            amount = -Math.abs(amount);
        }

        logTransaction(description, vendor, amount);

        System.out.println();
        System.out.println("Press enter to return to the home screen..." );
        scanner.nextLine();

        displayHomeScreen();
    }

    static void displayLedgerScreen()
    {
        //ensure transactions are loaded before we display the ledger screen
        transactions = loadTransactions();

        System.out.println("Ledger");
        System.out.println("------");
        System.out.println();
        System.out.println("A) All Transactions");
        System.out.println("D) Deposits Only");
        System.out.println("P) Payments Only");
        System.out.println("R) Reports");
        System.out.println("H) Return Home");
        System.out.print("Make a selection: ");

        String choice = scanner.nextLine().toUpperCase().strip();

        System.out.println();
        switch (choice) {
            case "A":
                // display all transactions
                displayTransactions(transactions, "A");
                System.out.println("Press enter to return to the ledger screen...");
                scanner.nextLine();
                displayLedgerScreen();
                break;
            case "D":
                // display deposits only
                displayTransactions(transactions, "D");
                System.out.println("Press enter to return to the ledger screen...");
                scanner.nextLine();
                displayLedgerScreen();
                break;
            case "P":
                // display payments only
                displayTransactions(transactions, "P");
                System.out.println("Press enter to return to the ledger screen...");
                scanner.nextLine();
                displayLedgerScreen();
                break;
            case "R":
                displayReportsScreen();
                break;
            case "H":
                displayHomeScreen();
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                displayLedgerScreen();

        }
    }

    public static void displayTransactions(ArrayList<Transaction> transactions, String filterType)
    {
        // loop through the transactions and display them
        for (Transaction transaction : transactions)
        {
            if (filterType.equalsIgnoreCase("D") && transaction.getAmount() < 0)
            {
                continue;
            }
            else if (filterType.equalsIgnoreCase("P") && transaction.getAmount() > 0)
            {
                continue;
            }
            System.out.println(transaction.getDate()
                    + " "
                    + transaction.getTime()
                    + " " + transaction.getVendor()
                    + " " + transaction.getDescription()
                    + " " + transaction.getAmount());
        }
    }

    public static void logTransaction(String description, String vendor, double amount)
    {
        // log the transaction to the file
        FileOutputStream fileOutputStream = null;
        PrintWriter printWriter = null;

        try
        {
            fileOutputStream = new FileOutputStream("data/transactions.csv", true);
            printWriter = new PrintWriter(fileOutputStream);

            printWriter.printf("%s|%s|%s|%s|%.2f%n",
                    LocalDate.now(),
                    LocalTime.now().withNano(0),
                    description,
                    vendor,
                    amount);

            System.out.println("Transaction logged successfully.");
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (printWriter != null)
            {
                printWriter.close();
            }
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    static void displayReportsScreen()
    {
            System.out.println("\n=== Reports ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search Transactions");
            System.out.println("0) Return to Ledger");
            System.out.print("Make a selection: ");

            String choice = scanner.nextLine().strip();

            ArrayList<Transaction> transactions = loadTransactions();

            switch (choice)
            {
                case "1":
                    System.out.println("\n=== Month To Date ===");
                    //displayMonthToDate(transactions);
                    break;

                case "2":
                    System.out.println("\n=== Previous Month ===");
                   // displayPreviousMonth(transactions);
                    break;

                case "3":
                    System.out.println("\n=== Year To Date ===");
                   // displayYearToDate(transactions);
                    break;

                case "4":
                    System.out.println("\n=== Previous Year ===");
                  //  displayPreviousYear(transactions);
                    break;

                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    //displayByVendor(transactions, vendor);
                    break;

                case "0":

                    displayLedgerScreen();

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
    }
}
