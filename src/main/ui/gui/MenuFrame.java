package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class MenuFrame extends JFrame {
    protected static final int HEIGHT = 640;
    protected static final int WIDTH = 640;


    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(200, 200, 200);
    public static final String ICON_LOCATION = "./data/images/jcap.png";

    // EFFECTS: makes a new frame in the default style of JCapTrack
    public MenuFrame(String name) {
        super();
        this.setLayout(null);
        this.setTitle(name);
        ImageIcon icon = new ImageIcon(ICON_LOCATION);
        this.setIconImage(icon.getImage());
        this.getContentPane().setBackground(Color.WHITE);
        this.setSize(WIDTH, HEIGHT);  // Set the dimensions
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
