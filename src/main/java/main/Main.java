package main;

import comm.error.ExceptionHandler;
import gui.MainFrame;
import gui.util.GuiDialogsUtils;

import javax.swing.*;

/**
 * Created by Domino on 2016-12-07.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            String selection = GuiDialogsUtils.showSerialPortChoosingDialog();

            MainFrame.createFrame(selection);
        });
    }
}
