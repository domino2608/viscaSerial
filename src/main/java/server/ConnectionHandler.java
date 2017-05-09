package server;

import comm.CameraCommandUtil;
import comm.SerialPortCommunicationHandler;

import java.io.*;
import java.net.Socket;

import static comm.CameraCommandUtil.*;

/**
 * Created by Dominik C.
 */
public class ConnectionHandler implements Closeable {

    private Socket sck;
    private BufferedReader reader;
    private PrintWriter writer;

    private static SerialPortCommunicationHandler serialPortCommunicationHandler;

    static {

        try {
            serialPortCommunicationHandler =
                    new SerialPortCommunicationHandler(SerialPortCommunicationHandler.getActiveCommPortsList().get(0));
        } catch (Exception e) {
            e.printStackTrace();

            System.exit(-1);
        }
    }

    public ConnectionHandler(Socket sck) throws Exception {
        this.sck = sck;

        initStreams();
    }

    private void initStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()), true);
    }

    public void handleCommunication() throws IOException {
        boolean isRunning = true;

        String cmd = null;

        while((cmd = reader.readLine()) != null) {
            System.out.println("Got command: " + cmd);

            String response = null;

            switch (cmd) {
                case "right":
                    response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                            CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_RIGHT, 7, 7));
                    break;
                case "left":
                    response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                            CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_LEFT, 7, 7));
                    break;
                case "up":
                    response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                            CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_UP, 7, 7));
                    break;
                case "down":
                    response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                            CameraCommandUtil.getCommandCopyWithChangedSpeed(CameraCommandUtil.MOVE_DOWN, 7, 7));
                    break;
                case "stop":
                    response = serialPortCommunicationHandler.sendCommandAndGetResponse(CameraCommandUtil.STOP);
                    break;
            }

            if (cmd.startsWith("POS: ")) {
                String xChange = cmd.substring(cmd.indexOf('x') + 2, cmd.indexOf(';'));
                String yChange = cmd.substring(cmd.indexOf('y') + 2);

                int x = (int) (10 * Double.parseDouble(xChange));
                int y = (int) (10 * Double.parseDouble(yChange));

                System.out.println("x=" + x);
                System.out.println("y=" + y);

                if (x < 0) {
                    x = NEGATIVE_START + HORIZONTAL_CHANGE * x;
                } else {
                    x = POSITIVE_START + HORIZONTAL_CHANGE * x;
                }

                if (y < 0) {
                    y = NEGATIVE_START + VERTICAL_CHANGE * y;
                } else {
                    y = POSITIVE_START + VERTICAL_CHANGE * y;
                }

                System.out.println("After change");
                System.out.println("x = " + x);
                System.out.println("y = " + y);

                response = serialPortCommunicationHandler.sendCommandAndGetResponse(
                        CameraCommandUtil.getCommandCopyWithChangedValueAtIndex(CameraCommandUtil.RELATIVE_POSITION,
                                getRelativePositionPositionValues(x, y)));
            }

            System.out.println(response);
        }
    }

    private NewIndexValueCommandHolder[] getRelativePositionPositionValues(int x, int y) {
        String hexX = Integer.toHexString(x);
        hexX = getPaddingForHex(hexX) + hexX;

        String hexY = Integer.toHexString(y);
        hexY = getPaddingForHex(hexY) + hexY;

        System.out.println("hex x=" + hexX);
        System.out.println("hex y=" + hexY);

        NewIndexValueCommandHolder[] newValues = new NewIndexValueCommandHolder[8];
        newValues[0] = new NewIndexValueCommandHolder(POSITION_PAN_1_INDEX, Integer.decode("0x" + hexX.charAt(0)));
        newValues[1] = new NewIndexValueCommandHolder(POSITION_PAN_2_INDEX, Integer.decode("0x" + hexX.charAt(1)));
        newValues[2] = new NewIndexValueCommandHolder(POSITION_PAN_3_INDEX, Integer.decode("0x" + hexX.charAt(2)));
        newValues[3] = new NewIndexValueCommandHolder(POSITION_PAN_4_INDEX, Integer.decode("0x" + hexX.charAt(3)));

        newValues[4] = new NewIndexValueCommandHolder(POSITION_TILT_1_INDEX, Integer.decode("0x" + hexY.charAt(0)));
        newValues[5] = new NewIndexValueCommandHolder(POSITION_TILT_2_INDEX, Integer.decode("0x" + hexY.charAt(1)));
        newValues[6] = new NewIndexValueCommandHolder(POSITION_TILT_3_INDEX, Integer.decode("0x" + hexY.charAt(2)));
        newValues[7] = new NewIndexValueCommandHolder(POSITION_TILT_4_INDEX, Integer.decode("0x" + hexY.charAt(3)));

        return newValues;
    }

    private String getPaddingForHex(String hex) {
        int paddingLength = 4 - hex.length();
        String padding = "";

        for (int i = 0; i < paddingLength; i++) {
            padding += "0";
        }

        return padding;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        sck.close();

        if (serialPortCommunicationHandler != null) {
            try {
                serialPortCommunicationHandler.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
