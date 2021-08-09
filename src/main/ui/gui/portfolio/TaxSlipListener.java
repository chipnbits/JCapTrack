package ui.gui.portfolio;

import model.Portfolio;
import ui.gui.portfolio.PortfolioNavigatorMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//TODO Implement this class for getting tax information
public class TaxSlipListener implements ActionListener {

    private final Portfolio user;
    private final PortfolioNavigatorMenu parent;

    public TaxSlipListener(Portfolio user, PortfolioNavigatorMenu parent) {
        this.user = user;
        this.parent = parent;
    }

    // MODIFIES: this
    // EFFECTS:  Prompts user to enter a year and then displays any transactions that involved a capital gain or loss
    //           for that year.
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
