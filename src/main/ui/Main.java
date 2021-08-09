package ui;


import model.Security;
import model.Transaction;
import ui.gui.main.PortfolioSelectionPanel;
import ui.gui.securities.TransactionHistogram;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;


public class Main {
    public static void main(String[] args) {
//        Calendar date1 = Calendar.getInstance();
//        Calendar date2 = Calendar.getInstance();
//        Calendar date3 = Calendar.getInstance();
//        Transaction buyBNS1;
//        Transaction buyBNS2;
//        Transaction sellBNS;
//        Security bns;
//        bns = new Security("BNS");
//        date1.set(2019, Calendar.NOVEMBER, 20);
//        date2.set(2020, Calendar.JUNE, 5);
//        date3.set(2021, Calendar.MARCH, 20);
//        buyBNS1 = new Transaction("BNS", date1, false, 1089.18,
//                false, 0, 10, 4.99);
//        sellBNS = new Transaction("BNS", date2, true, 420.20,
//                false, 0, 5, 4.99);
//        buyBNS2 = new Transaction("BNS", date3, false, 1850.10,
//                false, 0, 20, 4.99);
//        bns.addTransaction(buyBNS1);
//        bns.addTransaction(buyBNS2);
//        bns.addTransaction(sellBNS);
//
//        JFrame testFrame = new JFrame();
//        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        TransactionHistogram graph = new TransactionHistogram(bns);
//
//        testFrame.getContentPane().add(graph);
//        testFrame.pack();
//        testFrame.setVisible(true);

        new PortfolioSelectionPanel();
    }
}
