package persistence;


import model.Transaction;

import java.util.List;

// Represents a csv file converted into a data type readily processed by JCapTrack
public class CsvExportData {

    private final List<String> securityNames;
    private final List<Transaction> transactions;

    public CsvExportData(List<String> securityNames, List<Transaction> transactions) {
        this.securityNames = securityNames;
        this.transactions = transactions;
    }

    public List<String> getSecurityNames() {
        return securityNames;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
