import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by Domino on 2016-12-07.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        CommPortIdentifier portIdentifier = getCOMMPortByName("COM8");

        SerialPort serialPort = (SerialPort) portIdentifier.open("viscaSerialSteering", 5000);
        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        PrintWriter printWriter = new PrintWriter(serialPort.getOutputStream(), true);

        printWriter.print("AT");
        printWriter.print("\r\n");

        Thread.sleep(1000);

        System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());

        printWriter.close();
        bufferedReader.close();
        serialPort.close();
    }

    private static CommPortIdentifier getCOMMPortByName(String name) throws Exception {
        Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();

        while (commPorts.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) commPorts.nextElement();

            if (CommPortIdentifier.PORT_SERIAL == commPortIdentifier.getPortType()
                    && commPortIdentifier.getName().equalsIgnoreCase(name)) {

                System.out.println(commPortIdentifier.getName());
                return commPortIdentifier;
            }
        }

        throw new Exception("Port not found: " + name);
    }

}
