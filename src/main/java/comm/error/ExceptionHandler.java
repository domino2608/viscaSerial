package comm.error;

import gui.util.GuiDialogsUtils;

import javax.swing.*;

/**
 * Created by Domino on 2016-12-10.
 */
public class ExceptionHandler {

    public static void handleException(Exception e) {
        e.printStackTrace();
        GuiDialogsUtils.showErrorDialog(e.toString() + "\nProgram will now close.");
        System.exit(1);
    }

}
