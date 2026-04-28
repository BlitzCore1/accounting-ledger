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

        String choice = scanner.nextLine().toUpperCase().strip();

        System.out.println();
        switch (choice) {
            case "D":
                displayDepositScreen();
                break;
            case "P":
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

    static void displayDepositScreen()
    {
        System.out.println("Make a Deposit");
        System.out.println("-------------");
        System.out.println();

        System.out.print("Enter a description: ");
        String description = scanner.nextLine().strip();

        System.out.print("Enter a vendor: ");
        String vendor = scanner.nextLine().strip();

        System.out.print("Enter the amount: ");
        double amount = Double.parseDouble(scanner.nextLine().strip());

        logTransaction(description, vendor, amount);

        System.out.println();
        System.out.println("Press enter to return to the home screen..." );
        scanner.nextLine();

        displayHomeScreen();
    }

    static void displayLedgerScreen()
    {
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
                System.out.println("Create the Reports Screen");
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
}
