package net.ddns.gngw.chattserverclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class MainFrame extends JFrame {
    private SendArea sendArea;
    private DisplayArea displayArea;
    private JTextField ipAddrField;
    private JLabel ipAddrLabel;
    private JTextField portField;
    private JLabel portLabel;
    private JButton connectButton;
    private JLabel joinCodeLabel;
    private JTextField joinCodeField;
    private Socket socket;
    private PrintWriter printWriter;

    MainFrame() {
        super("ChattApp Client");
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 503);
        setResizable(false);

        sendArea = new SendArea();

        displayArea = new DisplayArea();
        displayArea.setListener(new Startable(){
            public void start() {
                sendArea.start();
            }
        });

        ipAddrLabel = new JLabel("IP Address: ");
        ipAddrField = new JTextField("gngw.ddns.net");
        portLabel = new JLabel("Port: ");
        portField = new JTextField("8080");

        joinCodeLabel = new JLabel("Room Code: " );
        joinCodeField = new JTextField("123");

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpNetworking();
            }
        });
        // #region
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
        int width = 7;
        // Row 1 Display
        gc.weightx = 100;
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 40;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = width;
        add(displayArea, gc);

        // Row 2 Message Area
        gc.weighty = 5;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = width;
        add(sendArea, gc);

        // Row 3 Col 1 Network Setup
        gc.weightx = 10;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        add(ipAddrLabel, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        add(ipAddrField, gc);

        // Row 3 Col 2
        gc.gridx = 2;
        gc.gridy = 2;
        add(portLabel, gc);

        gc.gridx = 3;
        gc.gridy = 2;
        add(portField, gc);

        gc.gridx = 4;
        gc.gridy = 2;
        add(joinCodeLabel, gc);

        gc.weightx = 40;
        gc.gridx = 5;
        gc.gridy = 2;
        add(joinCodeField, gc);
        gc.weightx = 20;
        // Row 3 Col 3
        gc.gridx = 6;
        gc.gridy = 2;
        add(connectButton, gc);
        // #endregion

        setVisible(true);
    }

    public void setUpNetworking() {
        System.out.println("Trying To connect... please wait");
        try {
            socket = new Socket(ipAddrField.getText(), Integer.parseInt(portField.getText()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            displayArea.printMessage("Connected To Server...");
            printWriter.println(joinCodeField.getText());
            displayArea.printMessage("Searching For Room/Waiting for more members...");
            sendArea.setOutputWriter(socket.getOutputStream());
            displayArea.setInputReader(socket.getInputStream());

        } catch (NumberFormatException e) {
            System.out.println("NOOO, That's not a number you silly goose :)");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Host Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

}