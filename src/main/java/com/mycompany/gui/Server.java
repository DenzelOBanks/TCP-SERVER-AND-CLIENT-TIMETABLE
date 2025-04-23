package com.mycompany.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;



public class Server {
    private static ServerSocket servSock;
    private static final int PORT = 1234;
    private static final HashMap<String, Lecture> schedule = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Opening port...");
        try {
            servSock = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }

        while (true) {
            run(); // Execute the program while the connection is open
        }
    }

    private static void run() {
        try (Socket clientSocket = servSock.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream())) {

            System.out.println("Client connected!");

            String command;
            while (true) { // Keep the connection open for multiple requests
                command = in.readLine();
                System.out.println("Command received");
                if (command == null) break; // Handle client disconnecting

                if (command.equalsIgnoreCase("Exit")) {
                    System.out.println("Client requested exit. Closing connection...");
                    System.exit(0);
                    break; 
                }

                String data = in.readLine();
                if (data == null) {
                    out.println("Invalid input received.");
                    continue;
                }

                switch (command) {
                    case "Add":
                        System.out.println("Adding lecture...");
                        //System.out.println(data);
                        processAddCommand(data, out);     
                        break;
                    case "Remove":
                        processRemoveCommand(data, out);
                        break;
                    case "Display":
                        System.out.println("Sending schedule over...");
                        display(objOut);
                        break;
                    default:
                        out.println("Unknown command received: " + command);
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void processAddCommand(String data, PrintWriter out) {
        String[] parts = data.split(",");
        if (parts.length < 4 || parts[0] == null || parts[1] == null || parts[3] == null || parts[2].equals("") ) {
            out.println("Missing information. Expected: day,time,room,module");
        } else {
            out.println(addLecture(parts[0], parts[1], parts[2], parts[3]));
        }
    }

    private static void processRemoveCommand(String data, PrintWriter out) {
        String[] parts = data.split(",");
        if (parts.length < 4) {
            out.println("Invalid Remove format. Expected: day,time,room,module");
        } else {
            out.println(removeLecture(parts[0], parts[1], parts[2], parts[3]));
        }
    }

    private static String addLecture(String day, String time, String module, String room) {
        String key = day + " " + time;
        if (schedule.containsKey(key)) {
            return "Error: Room is already booked at this time.";
        }
        schedule.put(key, new Lecture(day, time, module, room));
        return "Lecture Added";
    }

    private static String removeLecture(String day, String time, String module, String room) {
        String key = day + " " + time;
        if (schedule.containsKey(key)) {
            Lecture lecture = schedule.get(key);
            if (lecture.getModule().equals(module) && lecture.getRoom().equals(room)) {
                schedule.remove(key);
                return "Lecture Removed";
            }
        }
        return "Lecture doesn't exist";
    }

    private static void display(ObjectOutputStream objOut) {
        try {
            HashMap<String, Lecture> clonedSchedule = new HashMap<>(schedule); // Clone to ensure fresh data
            objOut.reset(); //reset the OOS to make sure the old data isn't there
            objOut.writeObject(clonedSchedule);
            objOut.flush();
            System.out.println("Schedule sent!");
        } catch (IOException e) {
            System.err.println("Error sending schedule: " + e.getMessage());
        }
    }
}
class Lecture implements Serializable {
    private static final long serialVersionUID = 1L;
    private String room;
    private String module;
    private String day;
    private String time;
    
 
    public Lecture(){
        this.day = null;
        this.module = null;
        this.room = null;
        this.time = null;
    }
    public Lecture(String day, String time, String module, String room){
        this.day = day;
        this.module = module;
        this.room = room;
        this.time = time;
    }
    
    public String getRoom(){
        return room;
    }
    public String getDay(){
        return day;
    }   
    public String getModule(){
        return module;
    }    
    public String getTime(){
        return time;
    }
    @Override
    public String toString(){
         String data = String.format( "%s\n%s\n%s\n%s" ,getDay(),getTime(),getModule(),getRoom());
        return data;
    }

}
