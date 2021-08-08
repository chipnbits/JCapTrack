package ui.gui;

import model.Security;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class SecurityMenu extends MenuFrame {
    public static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.CANADA);
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 640;


    Map<String, SecurityMenu> menus;

    Security security;
    TransactionTable listings;



    public SecurityMenu(Security security, Map<String, SecurityMenu> openMenus) {
        super(security.getTicker());
        this.security = security;
        this.menus = openMenus;
        this.setSize(1280,640);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = this;

        //Create and set up the content pane.
        listings = new TransactionTable(security);
        listings.setOpaque(true);
        listings.setBounds(0,0,WIDTH,HEIGHT / 2);
        //listings.getTable().getSelectionModel().addListSelectionListener
        frame.add(listings);

        //Display the window.

        frame.setVisible(true);
    }

    @Override
    protected void closePrompt() {
        menus.remove(security.getTicker());
        this.dispose();
    }
}
