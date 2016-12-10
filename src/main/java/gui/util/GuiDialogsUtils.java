package gui.util;

import comm.SerialPortCommunicationHandler;

import javax.swing.*;

/**
 * Created by Domino on 2016-12-10.
 */
public abstract class GuiDialogsUtils {

    public static final String showSerialPortChoosingDialog() {
        String selection = (String) JOptionPane.showInputDialog(null,
                "Select serial port for communication", "Serial port chooser", JOptionPane.QUESTION_MESSAGE,
                null, SerialPortCommunicationHandler.getActivePortNames().toArray(), null);

        if (selection == null) {
            JOptionPane.showMessageDialog(null, "Nothing selected. Exiting...");
            System.exit(0);
        }

        return selection;
    }

    public static final void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error occurred", JOptionPane.ERROR_MESSAGE);
    }

}
