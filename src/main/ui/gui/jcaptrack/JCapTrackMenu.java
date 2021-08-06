package ui.gui.jcaptrack;

import ui.gui.MenuFrame;

import javax.swing.*;

public class JCapTrackMenu extends MenuFrame {
    private static final int HEIGHT = 640;
    private static final int WIDTH = 640;
    PortfolioSelectionPanel psp;

    // EFFECTS: makes a new frame in the default style of JCapTrackMenu
    public JCapTrackMenu() {
        super("JCapTrack");
        this.setSize(WIDTH, HEIGHT);  // Set the dimensions
        psp = new PortfolioSelectionPanel();
        this.setContentPane(psp);
        this.setVisible(true);
    }


    @Override
    protected void closePrompt() {

    }


}
