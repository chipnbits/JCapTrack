package ui.gui;

import model.Security;

import javax.swing.*;
import java.util.List;

import static ui.gui.jcaptrack.JCapTrackMenu.DEFAULT_BACKGROUND_COLOR;


public class SecuritySelectionFrame extends JPanel {


    private List<Security> securities;

    protected SecuritySelectionFrame(List<Security> group) {
        securities = group;
        initialize();
    }

    private void initialize() {
        setBackground(DEFAULT_BACKGROUND_COLOR);

    }


}
