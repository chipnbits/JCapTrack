package ui.gui.portfolio;

import exceptions.FileCorruptException;
import model.Portfolio;
import persistence.CsvReader;
import persistence.ImportData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class CsvImportListener implements ActionListener {
    private final Portfolio user;
    private final PortfolioNavigatorMenu parent;

    public CsvImportListener(Portfolio user, PortfolioNavigatorMenu parent) {
        this.user = user;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();

        fc.setCurrentDirectory(new File("./data/csv"));
        fc.setFileFilter(new FileNameExtensionFilter(
                "CSV Files", "csv"));

        int val = fc.showOpenDialog(parent);

        if (val == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            try {
                importFromCsv(selectedFile);
            } catch (FileNotFoundException fileNotFoundException) {
                parent.errorMessagePopup("File not found");

            } catch (FileCorruptException fileCorruptException) {
                parent.errorMessagePopup("CSV File is corrupt");
            }
        }

    }

    private void importFromCsv(File selected) throws FileNotFoundException, FileCorruptException {
        CsvReader reader = new CsvReader(selected);
        ImportData importData = reader.parseData();

        importData.addToPortfolio(user);

        String success = "Import was successful with " + importData.getSecurityNames().size()
                + " securities added and " + importData.getTransactions().size() + " transactions added.";

        // Make sure that the buttons are enabled if items added to list
        if (importData.getSecurityNames().size() > 0) {
            parent.enableButtons();
        }
        parent.refreshList();

        JOptionPane.showMessageDialog(parent, success);
    }
}
