package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class MenuFrame extends JFrame {
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(200, 200, 200);
    public static final String ICON_LOCATION = "./data/images/jcap.png";

    // EFFECTS: makes a new frame in the default style of JCapTrack
    public MenuFrame(String name) {
        super();
        this.setTitle(name);
        ImageIcon icon = new ImageIcon(ICON_LOCATION);
        this.setIconImage(icon.getImage());
        this.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setVisible(true);
        this.setResizable(false);
        setLocationRelativeTo(null);
        setClosingBehavior();
    }

    private void setClosingBehavior() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closePrompt();
            }
        });
    }

    protected abstract void closePrompt();
}
