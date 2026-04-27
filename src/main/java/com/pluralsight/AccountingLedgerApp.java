package com.pluralsight;

import java.util.Scanner;

public class AccountingLedgerApp
{
    static Scanner scanner = new Scanner(System.in);

    static void main()
    {
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
                System.out.println("Create the Deposit Screen");
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


}
