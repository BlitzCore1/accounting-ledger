package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingLedgerApp
{
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
        switch (choice)
        {
            case "D":
                //displayDepositScreen();
                break;
            case "P":
                System.out.println("Create the Payment Screen");
                break;
            case "L":
                System.out.println("Create the Ledger Screen");
                break;
            case "X":
                System.out.println("Goodbye!");
                // return exits the loop and the method, which in this case ends the application
                return;
            default:
                System.out.println("Invalid selection. Please try again.");
        }



    }

    static ArrayList<Transaction> loadTransactions()
    {
     // load transactions from a file
     ArrayList<Transaction> transactions = new ArrayList<>();

     // populate the list
        try
        {
            FileReader fileReader = new FileReader("data/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine(); // read the header line and ignore it
            line = bufferedReader.readLine();

            while (line != null)
            {
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
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        return transactions;
    }

   // static void displayDepositScreen()
    {
        System.out.println("Make a Deposit");
        System.out.println("-------------");
        System.out.println();
        System.out.print("Enter the amount to deposit: ");
        String amount = scanner.nextLine().strip();
    }




}
