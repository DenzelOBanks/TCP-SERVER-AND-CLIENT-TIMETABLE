package com.mycompany.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * JavaFX Client
 */

public class Client extends Application{
    
    private static InetAddress host;
    private static final int PORT = 1234;

    
    //mehtods
    
    public static int findIndex(String a[], String t)
    {
        int len = a.length;
        
        for(int i = 0; i < len ; i++){
            if (a[i].equalsIgnoreCase(t)) {
                return i;
            }
        }
        return -1;
    }
    
    public void addClass(GridPane grid, String classDets, int col, int row) {
        Label classLabel = new Label(classDets);
        classLabel.setMaxWidth(100);
        classLabel.setWrapText(true);
        grid.add(classLabel, col, row);
    }
    
    public void fillTable(GridPane grid, HashMap<String, Lecture> s){
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] hours = {"", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};

        for (Lecture lec : s.values()) {
                    int col = findIndex(days, lec.getDay())+1;
                    int row = findIndex(hours, lec.getTime());
                    addClass(grid, lec.toString(), col, row);
                }
    }
    
    //Interface variables
    Button button1, button2, button3, button4, exitBut, backButA, subButA, backButB, subButB, backButT;
    
    
    //Start of the main code
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        launch(args);
      
    }
Socket link = null;
    @Override
    public void start(Stage stage) throws Exception {
            
    try {
        host = InetAddress.getLocalHost();
        link = new Socket(host, PORT);
        System.out.println("Connected to " + host);

        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintWriter out = new PrintWriter(link.getOutputStream(), true);
        ObjectInputStream objIn = new ObjectInputStream(link.getInputStream());
        
        stage.setTitle("Timetable Manager");
        
        
        //initialise and design all the menu buttons 
        button1 = new Button("Add lecture");
        button1.setMinSize(300, 60  );
        
        button2 = new Button("Remove lecture");
        button2.setMinSize(300, 60);

        button3 = new Button("Get timetable");
        button3.setMinSize(300, 60);

        button4 = new Button("Other...");
        button4.setMinSize(300, 60);

        exitBut = new Button("Exit");
        exitBut.setMinSize(300, 60);
        exitBut.setOnAction(e -> {
            out.println("Exit");
            System.exit(0);
                });

        backButA = new Button("Back");
        backButA.setMinSize(100,30);
        
        subButA = new Button("Submit");
        subButA.setMinSize (100,30);
        
        backButB = new Button("Back");
        backButB.setMinSize(100,30);
        
        subButB = new Button("Submit");
        subButB.setMinSize (100,30);
        
        backButT = new Button("Back");
        backButT.setMinSize(80,30);

        Alert successAlert = new Alert(AlertType.INFORMATION); 
        successAlert.setTitle("Confirmation");
        Alert errorAlert = new Alert(AlertType.ERROR);
        
        
        //code for setting up nad booting the first scene (menu)
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(button1, button2, button3, button4, exitBut);

        Scene main = new Scene(menu, 400, 450);
        stage.setScene(main);
        stage.show();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        //code for the details to add a lecture (Option 1)
        Label label1 = new Label("Enter the details of the lecture:");
        
        ComboBox<String> addDayComboBox = new ComboBox<>();
        addDayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        addDayComboBox.setPromptText("Date of the lecture");

        ComboBox<String> addHourComboBox = new ComboBox<>();
        addHourComboBox.getItems().addAll("9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        addHourComboBox.setPromptText("Time of the lecture");

        TextField addRoomBox = new TextField();
        addRoomBox.setPromptText("Room code");
        addRoomBox.setMaxWidth(150);

        ComboBox<String> addModuleBox = new ComboBox();
        addModuleBox.setPromptText("Module code");
        addModuleBox.getItems().addAll("CS4444", "PBG123", "MA1001", "KA2231", "FN4098");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //code for the details to remove a lecture (Option 2)
        ComboBox<String> dayComboBox = new ComboBox();
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        dayComboBox.setPromptText("Date of the lecture");

        ComboBox<String> hourComboBox = new ComboBox();
        hourComboBox.getItems().addAll("9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
        hourComboBox.setPromptText("Time of the lecture");

        TextField roomBox = new TextField();
        roomBox.setPromptText("Room code");
        roomBox.setMaxWidth(150);

        ComboBox<String> moduleBox = new ComboBox();
        moduleBox.setPromptText("Module code");
        moduleBox.getItems().addAll("CS4444", "PBG123", "MA1001", "KA2231", "FN4098");
       
        
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        

        //Code for event handling (addLecture)
        VBox lecDetails = new VBox(10);
        lecDetails.getChildren().addAll(label1, addDayComboBox, addHourComboBox, addModuleBox, addRoomBox, subButA, backButA);
      
        Scene addLecScene = new Scene(lecDetails, 400, 300);
        
        button1.setOnAction(e -> stage.setScene(addLecScene));
        backButA.setOnAction(e -> stage.setScene(main));
        
         
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        
        
        //event handling lecture deletion scene
        Label label2 = new Label("Enter the details of the lecture you want to delete");
        
        VBox deleteDetails = new VBox(10);
        deleteDetails.getChildren().addAll(label2, dayComboBox, hourComboBox, moduleBox, roomBox, subButB, backButB);
        Scene rmLecScene = new Scene(deleteDetails, 400,300);
        
        
        button2.setOnAction(e -> stage.setScene(rmLecScene));
        backButB.setOnAction(e -> stage.setScene(main));
        

        
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        

        
        //     TIMETABLE CODE
       
        GridPane timetable = new GridPane();
        timetable.add(backButT, 0, 0);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int col = 0; col < days.length; col++) {
            Label dayLabel = new Label(days[col]);
            dayLabel.setMinWidth(100);  
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            GridPane.setHgrow(dayLabel, javafx.scene.layout.Priority.ALWAYS); // Allows columns to grow
            timetable.add(dayLabel, col + 1, 0);
        }

        String[] hours = {"", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        for (int row = 1; row <= 10; row++) {
            Label hourLabel = new Label(hours[row]);
            hourLabel.setMinWidth(80);
            hourLabel.setStyle("-fx-font-weight: bold;");
            timetable.add(hourLabel, 0, row);
        }

        timetable.setHgap(20); 
        timetable.setVgap(15); 
        timetable.setPadding(new Insets(20, 30, 20, 30));

        
        for (int i = 0; i <= days.length; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(100);
            colConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            timetable.getColumnConstraints().add(colConstraints);
        }
     
        Scene ttDisplay = new Scene(timetable, 750, 600);
        
        //Event handling for main buttons
        button3.setOnAction(e -> {

            out.println("Display");
            out.println("nothing");
            System.out.println("Dislpay command sent");
            try{
                    fillTable(timetable,(HashMap) objIn.readObject());
                }catch(IOException | ClassNotFoundException a){
                    System.err.println(a.getMessage());
                }finally{
                    stage.setScene(ttDisplay);
                }
            }
        );
        
        backButT.setOnAction(e -> stage.setScene(main));

        
        
        subButA.setOnAction(e -> {
            String details = addDayComboBox.getValue() + "," + addHourComboBox.getValue() + "," + addRoomBox.getText() + "," + addModuleBox.getValue();
            out.println("Add");
            out.println(details);
            try{
                String response = in.readLine();
                if(response.contains("Missing") || response.contains("Error")){     //was wrong "Invalid"
                    errorAlert.setContentText(response);
                    errorAlert.show();
                }else{
                    successAlert.setContentText("Class successfully added!");
                    successAlert.show();
                }
            }catch(IOException p){
               p.getStackTrace();
            }
            
            
        });
        
        
        
        subButB.setOnAction(e -> {
            String detailsDelete = dayComboBox.getValue() + "," +  hourComboBox.getValue() + "," +  roomBox.getText() + "," +  moduleBox.getValue();
            out.println("Remove");
            out.println(String.join(",", detailsDelete));
            //out.println(detailsDelete);
            
            try{
                String response = in.readLine();
                if(response.contains("Invalid") || response.contains("exist")){
                    errorAlert.setContentText(response);
                    errorAlert.show();
                }else{
                    successAlert.setContentText("Class successfully deleted! Slot is now empty");
                    successAlert.show();
                }
            }catch(IOException p){
               p.getStackTrace();
            }
        });
        
        
        
        
        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        //CODE FOR OTHER OPTION
        
        TextField otherTxtBox = new TextField();
        otherTxtBox.setPromptText("Type desired function here / Type cancel to return");
        Label otherLabel = new Label("What would you like to do?");
        
        VBox enterOther = new VBox();
        enterOther.getChildren().addAll(otherLabel, otherTxtBox);
        Scene otherScene = new Scene(enterOther, 300, 60);
        
        button4.setOnAction(e -> stage.setScene(otherScene));
        otherTxtBox.setOnAction(e -> {
            
            
                if(otherTxtBox.getText().equalsIgnoreCase("cancel") ){
                    stage.setScene(main);
            }else{
                    try {
                        throw new IncorrectActionException("Action not included in the program");
                    } catch (IncorrectActionException ex) {
                        errorAlert.setContentText(ex.getMessage());
                        errorAlert.show();
                    }
                }
        });
                    
        errorAlert.setOnCloseRequest(e -> stage.setScene(main));
        

        
    } catch (UnknownHostException e) {
        System.err.println("Unable to get Localhost IP Address: " + e.getMessage());
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Error connecting to server: " + e.getMessage());
    }
    
}
}
class IncorrectActionException extends Exception{
    public IncorrectActionException(String m){
        super(m);
    }
}

