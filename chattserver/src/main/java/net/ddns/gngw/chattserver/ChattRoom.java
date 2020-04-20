package net.ddns.gngw.chattserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChattRoom {
    String chattRoomName;
    private boolean exit= false;
    ArrayList<Person> persons;
    
    public static int pid;

    public ChattRoom(Socket socket1, Socket socket2, String chattRoomName) {
        System.out.println("Welcome to the Room Brodies");
        persons = new ArrayList<Person>();
        this.chattRoomName = chattRoomName;
        addPerson(socket1);
        addPerson(socket2);
    }


    public void printMessage(String message)
    {
        for(Person person:persons)
        {
            person.tell(message);
        }
    }

    public void addPerson(Socket socket) {
        try {

            PrintWriter tempWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader tempReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Person tempPerson = new Person("Unkown", tempWriter, tempReader);
            Thread thread = new Thread(new ReadThread(tempPerson));
            thread.start();
        } catch (IOException e) {
            System.out.println("Error Instantiating Person or Starting Thread");
            e.printStackTrace();
        }

    }
    public void removePerson(Person person)
    {
        persons.remove(person);
        System.out.println(person.getName() + " Has Disconnected From the Server");
        printMessage(person.getName() + " Has Disconnected");
        if (persons.size() == 0)
        {
            
        }
    }
    
    
    private class ReadThread implements Runnable
    {
        private boolean keepReading = true;
        BufferedReader bufferedReader;
        Person person;
        public ReadThread(Person person)
        {
            this.person = person;
            this.bufferedReader = person.getBufferedReader();
        }

        @Override
        public void run() {
            try {
                person.tell("Connected!");
                person.tell("What is your name?: ");
                person.setName(bufferedReader.readLine());
                printMessage(person.getName() + " Has Joined The Chat");
                person.tell("Welcome to the chat " + person.getName());
                persons.add(person);
                while (keepReading) {
                    printMessage(person.getName() + ": " + bufferedReader.readLine());
                }
                } catch (IOException e) {
                    removePerson(person);
            }
        }
    }

    private class Person
    {
        private PrintWriter printWriter;
        private String name;
        private BufferedReader reader;
        private int id;

        public Person(String name, PrintWriter writer, BufferedReader bufferedReader)
        {
            this.printWriter = writer;
            this.reader = bufferedReader;
            this.name = name;
            this.id = pid++;
        }

        public BufferedReader getBufferedReader(){return reader;}
        public String getName(){return name;}
        public void setName(String name) {this.name = name;}
        public int getId() {
            return id;
        }

        public void tell(String message)
        {
            printWriter.println(message);
        }

        @Override
        public String toString() {
            return String.format("Name: %s\nID:%d", name, id);
        }

    }

}