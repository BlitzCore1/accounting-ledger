package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction
{
    private final LocalDate date;
    private final LocalTime time;
    private final String vendor;
    private final String description;
    private final double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public String getVendor()
    {
        return vendor;
    }

    public String getDescription()
    {
        return description;
    }

    public double getAmount()
    {
        return amount;
    }

    @Override
    public String toString(){
        return String.format("%-12s %-10s %-25s %-20s %10.2f", date, time, description, vendor, amount);
    }
}
