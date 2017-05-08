package main;

import gui.MainFrame;
import gui.util.GuiDialogsUtils;
import server.Server;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Domino on 2016-12-07.
 */

public class Main {

    public static void main(String[] args) throws Exception {
//        copyRequiredDllToJREBin();

//        SwingUtilities.invokeLater(() -> {
//            String selection = GuiDialogsUtils.showSerialPortChoosingDialog();
//
//            MainFrame.createFrame(selection);
//        });

        Server server = new Server("localhost", 9000);
        server.listenAndHandleConnections();
    }

    public static void copyRequiredDllToJREBin() throws IOException {
        //TODO access denied

        String javaHome = System.getProperty("java.home");

            if (javaHome == null) {
            return;
        }

        File dllFile = new File("lib/rxtxSerial.dll");
        File rxtxDllInBin = new File(System.getProperty("java.home") + "/bin/rxtxSerial.dll");

        if (Files.exists(rxtxDllInBin.toPath())) {
            System.out.println("Already exists");
            return;
        }

        Files.copy(dllFile.toPath(), rxtxDllInBin.toPath());
    }
}
