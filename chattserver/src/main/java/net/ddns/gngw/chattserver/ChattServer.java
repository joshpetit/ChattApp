package net.ddns.gngw.chattserver;

import java.util.Scanner;
public class ChattServer
{
    public ChattServer(){
    ConnectionHandler handler = new ConnectionHandler();
    System.out.println("Done Creating Handler");
    Scanner scan = new Scanner(System.in);
    
    scan.nextLine();
    }

    public static void main(String[] args) {
        new ChattServer();
    }
}