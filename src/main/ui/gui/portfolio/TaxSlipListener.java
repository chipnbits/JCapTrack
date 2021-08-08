package ui.gui.portfolio;

import model.Portfolio;
import ui.gui.portfolio.PortfolioNavigatorMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaxSlipListener implements ActionListener {


    private final Portfolio user;
    private final PortfolioNavigatorMenu parent;

    public TaxSlipListener(Portfolio user, PortfolioNavigatorMenu parent) {
        this.user = user;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
