package gui;

import comm.CameraCommandUtil;
import comm.SerialPortCommunicationHandler;
import comm.error.ExceptionHandler;
import gui.helper.ButtonLabels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Domino on 2016-12-09.
 */
public class MainFrame extends JFrame implements ActionListener {

    private SerialPortCommunicationHandler portCommHandler;
    private JTextArea textArea;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JSlider panSlider;
    private JSlider tiltSlider;

    private MainFrame(String serialPortName) throws Exception {
        super("Visca serial port controller");
        attachWindowListener();
        portCommHandler = new SerialPortCommunicationHandler(serialPortName);

        JPanel toolsPanel = new JPanel(new BorderLayout());
        JPanel navButtonPanel = createNavButtonPanel();
        JPanel downNavPanel = createDownNavPanel();

        toolsPanel.add(navButtonPanel);
        toolsPanel.add(downNavPanel, BorderLayout.SOUTH);

        JPanel cmdPanel = createCmdResponsesPanel();

        JPanel contentPane = new JPanel(new GridLayout(1, 2));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        contentPane.add(cmdPanel);
        contentPane.add(toolsPanel);

        add(contentPane);
        pack();
        setVisible(true);
    }

    public static MainFrame createFrame(String serialPort) {
        MainFrame mainFrame = null;

        try {
            mainFrame = new MainFrame(serialPort);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        } finally {
            if (mainFrame != null) {
                mainFrame.closeSerialPortHandler();
            }
        }

        return mainFrame;
    }

    public JPanel createDownNavPanel() {
        panSlider = createPanSlider();
        tiltSlider = createTiltSlider();

        JButton stopButton = new JButton(ButtonLabels.STOP);
        stopButton.addActionListener(this);
        JButton clearButton = new JButton(ButtonLabels.CLEAR_BUFFER);
        clearButton.addActionListener(this);

        JPanel stopClearPanel = new JPanel(new GridLayout(1, 2));
        stopClearPanel.add(stopButton);
        stopClearPanel.add(clearButton);

        JPanel sliderPanel = new JPanel(new GridLayout(2, 1));
        sliderPanel.add(panSlider);
        sliderPanel.add(tiltSlider);

        JPanel downNavPanel = new JPanel(new BorderLayout());
        downNavPanel.add(stopClearPanel);
        downNavPanel.add(sliderPanel, BorderLayout.SOUTH);

        return downNavPanel;
    }

    private JSlider createPanSlider() {
        JSlider speedSlider = new JSlider(1, 18, 9);
        speedSlider.setBorder(BorderFactory.createTitledBorder("Pan speed"));

        speedSlider.setMinorTickSpacing(1);
        speedSlider.setMajorTickSpacing(2);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        return speedSlider;
    }

    private JSlider createTiltSlider() {
        JSlider tiltSlider = new JSlider(1, 14, 7);
        tiltSlider.setBorder(BorderFactory.createTitledBorder("Tilt speed"));

        tiltSlider.setMinorTickSpacing(1);
        tiltSlider.setMajorTickSpacing(2);
        tiltSlider.setPaintTicks(true);
        tiltSlider.setPaintLabels(true);

        return tiltSlider;
    }

    private JScrollPane createCameraResponsesScrollPaneWithList() {
        JList<String> responseList = new JList<>(listModel);
        responseList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        responseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(responseList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Camera responses"));

        return scrollPane;
    }

    private JPanel createNavButtonPanel() {
        JPanel navButtonPanel = new JPanel(new GridLayout(3, 3));

        JButton upLeftBtn = new JButton(ButtonLabels.UP_LEFT);
        upLeftBtn.addActionListener(this);
        navButtonPanel.add(upLeftBtn);

        JButton upBtn = new JButton(ButtonLabels.UP);
        upBtn.addActionListener(this);
        navButtonPanel.add(upBtn);

        JButton upRightBtn = new JButton(ButtonLabels.UP_RIGHT);
        upRightBtn.addActionListener(this);
        navButtonPanel.add(upRightBtn);

        JButton leftBtn = new JButton(ButtonLabels.LEFT);
        leftBtn.addActionListener(this);
        navButtonPanel.add(leftBtn);

        JButton homeBtn = new JButton(ButtonLabels.HOME);
        homeBtn.addActionListener(this);
        navButtonPanel.add(homeBtn);

        JButton rightBtn = new JButton(ButtonLabels.RIGHT);
        rightBtn.addActionListener(this);
        navButtonPanel.add(rightBtn);

        JButton downLeftBtn = new JButton(ButtonLabels.DOWN_LEFT);
        downLeftBtn.addActionListener(this);
        navButtonPanel.add(downLeftBtn);

        JButton downBtn = new JButton(ButtonLabels.DOWN);
        downBtn.addActionListener(this);
        navButtonPanel.add(downBtn);

        JButton downRightBtn = new JButton(ButtonLabels.DOWN_RIGHT);
        downRightBtn.addActionListener(this);
        navButtonPanel.add(downRightBtn);

        return navButtonPanel;
    }

    private JPanel createCmdResponsesPanel() {
        JPanel cmdPanel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = createCameraResponsesScrollPaneWithList();

        JTextField cmdJTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String text = cmdJTextField.getText();
            String[] commands = text.split(" ");

            ArrayList<Integer> cmdList = new ArrayList<>();

            for (String cmd : commands) {
                try {
                    cmdList.add(Integer.decode("0x" + cmd));

                } catch (NumberFormatException ex) {
                    appendTextToResponseList("Wrong input!");
                    return;
                }
            }

            int[] arr = new int[cmdList.size()];
            for (int i = 0; i < cmdList.size(); i++) {
                arr[i] = cmdList.get(i);
            }

            appendTextToResponseList("Sending: " + text);
            String response = sendCommandToSerialPort(arr);

            appendTextToResponseList(response);
            cmdJTextField.setText("");
        });

        JPanel cmdDownPanel = new JPanel(new BorderLayout());
        cmdDownPanel.add(cmdJTextField);
        cmdDownPanel.add(sendButton, BorderLayout.EAST);

        cmdPanel.add(scrollPane);
        cmdPanel.add(cmdDownPanel, BorderLayout.SOUTH);

        return cmdPanel;
    }

    private void attachWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                closeSerialPortHandler();

                System.exit(0);
            }
        });
    }

    private void closeSerialPortHandler() {
        if (portCommHandler != null) {
            try {
                portCommHandler.close();
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceBtn = (JButton) e.getSource();

        int panSpeed = Integer.decode("0x" + panSlider.getValue());
        int tiltSpeed = Integer.decode("0x" + tiltSlider.getValue());

        String response = null;
        int[] command = null;

        switch (sourceBtn.getText()) {
            case ButtonLabels.UP_LEFT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_UP_LEFT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.UP:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_UP, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.UP_RIGHT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_UP_RIGHT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.LEFT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_LEFT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.RIGHT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_RIGHT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.DOWN_LEFT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_DOWN_LEFT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.DOWN:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_DOWN, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.DOWN_RIGHT:
                command = CameraCommandUtil.getCommandCopyWithChangedSpeed(
                        CameraCommandUtil.MOVE_DOWN_RIGHT, panSpeed, tiltSpeed);

                response = sendCommandToSerialPort(command);
                break;

            case ButtonLabels.STOP:
                response = sendCommandToSerialPort(CameraCommandUtil.STOP);
                break;

            case ButtonLabels.CLEAR_BUFFER:
                response = sendCommandToSerialPort(CameraCommandUtil.CLEAR_BUFFER);
                break;

            case ButtonLabels.HOME:
                response = sendCommandToSerialPort(CameraCommandUtil.HOME);
                break;
        }

        appendTextToResponseList(response);
    }

    private void appendTextToResponseList(String response) {
        String currentDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss"));

        listModel.addElement("[ " + currentDateString + " ]    " + response + "\n");
    }

    private String sendCommandToSerialPort(int[] command) {
        System.out.println("Sending to serial port: " + Arrays.toString(command));

        String response = null;
        try {
            response = portCommHandler.sendCommandAndGetResponse(command);
        } catch (IOException e) {
            closeSerialPortHandler();
            ExceptionHandler.handleException(e);
        }

        return response;
    }
}
