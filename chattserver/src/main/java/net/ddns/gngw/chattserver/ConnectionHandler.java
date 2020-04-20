package net.ddns.gngw.chattserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ConnectionHandler {
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean acceptConnections= true;
    private HashMap<String, Socket> waitingConnections;
    private BufferedReader bufferedReader;
    private String joinCode;
    private HashMap<String, ChattRoom>chattRooms;
    private ChattRoom newRoom;
    public ConnectionHandler() {
        waitingConnections = new HashMap<String, Socket>();
        chattRooms = new HashMap<String, ChattRoom>();
        Thread receiveConnections = new Thread(new NewConnection());
        receiveConnections.start();
    }
    
    
    
    public void stopNewConnections(){acceptConnections = false;}
    public void resumeNewConnections(){acceptConnections = true;}
    
    
    private void personAdded(Socket socket, String joinCode) {
        if (waitingConnections.containsKey(joinCode))
        {
            System.out.println("Match Found for Socket");
            newRoom = new ChattRoom(waitingConnections.remove(joinCode), socket, joinCode);
            chattRooms.put(joinCode, newRoom);

        }
        else if(chattRooms.containsKey(joinCode))
        {
            chattRooms.get(joinCode).addPerson(socket);
        }
        else{
            System.out.println("Put in Waiting Room");
            waitingConnections.put(joinCode, socket);
        }
        
    }

    
    private class NewConnection implements Runnable {  
        public void run() {

            try {
                serverSocket = new ServerSocket(8080);
                while (acceptConnections){
                    socket = serverSocket.accept();
                    System.out.println("Socket Accepted");
                    System.out.println("Waiting For JoinCode...");
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    joinCode = bufferedReader.readLine();
                    System.out.println("Received Join Code");
                    personAdded(socket, joinCode);
                }
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Error Accepting Sockets...");
                e.printStackTrace();
            }

        }

    }

}