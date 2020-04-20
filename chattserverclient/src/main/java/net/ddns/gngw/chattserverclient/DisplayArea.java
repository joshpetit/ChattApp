package net.ddns.gngw.chattserverclient;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DisplayArea extends JPanel {
    private JTextArea chattArea;
    private BufferedReader bufferedReader;
    private boolean read = true;
    private Startable app;
    private JScrollPane chattPane;
    public DisplayArea()
    {
        setLayout(new BorderLayout());
        chattArea = new JTextArea();
        chattArea.setEditable(false);
        chattArea.setLineWrap(true);
        chattArea.setAutoscrolls(true);
        chattPane = new JScrollPane(chattArea);
        chattPane.setAutoscrolls(true);
        add(chattPane, BorderLayout.CENTER);
    }
    
    public void setInputReader(InputStream stream)
    {
        this.bufferedReader = new BufferedReader(new InputStreamReader(stream));
        Thread readThread = new Thread(new ReadStream());
        readThread.start();
    }
    
    public void printMessage(String message)
    {
        chattArea.append("\n" + message);
        chattPane.getVerticalScrollBar().setValue(chattPane.getVerticalScrollBar().getMaximum());

    }

    public void setListener(Startable app) {this.app = app;}

    private class ReadStream implements Runnable
    {
        String message;
        @Override
		public void run() {
			try {
                message = bufferedReader.readLine();
                printMessage(message);
                app.start();
                while(read)
                {
                    message = bufferedReader.readLine();
                    printMessage(message);
                }
            } catch (Exception e) {
                printMessage("Connection Lost");
            }
			
		}
    }
}
