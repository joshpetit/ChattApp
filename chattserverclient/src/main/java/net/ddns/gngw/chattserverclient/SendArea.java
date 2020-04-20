package net.ddns.gngw.chattserverclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SendArea extends JPanel {
    private JLabel messageLabel;
    private JTextField messageField;
    private PrintWriter printWriter;

    public SendArea() {
        super(new GridBagLayout());
        messageLabel = new JLabel("Message:");

        messageField = new JTextField(20);
        messageField.setEnabled(false);
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {sendMessage();} 
        });
        
        GridBagConstraints gc = new GridBagConstraints();
        //#region
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridy = 0;
        add(messageLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.BOTH;
        add(messageField, gc);
        //#endregion

    }
    public void setOutputWriter(OutputStream stream)
    {
        this.printWriter = new PrintWriter(stream, true);
    }

    public void sendMessage()
    {
        printWriter.println(messageField.getText());
        messageField.setText("");
    }

    public void sendMessage(String message)
    {
        printWriter.println(message);
    }

    public void start()
    {
        messageField.setEnabled(true);
    }
}
