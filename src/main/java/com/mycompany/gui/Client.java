package com.mycompany.gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Author: Lucas
 */
public class Client extends Application {

    //MODEL
    class IncorrectActionException extends Exception{
    public IncorrectActionException(String m){
        super(m);
    }
}
    class LectureRequest{
        private String action;
        private String date, time, room, module;
        
        public LectureRequest(String a, String b, String c, String d, String e ){
            this.action = a;
            this.date = b;
            this.time = c;
            this.room = d;
            this.module = e;
        }
        
        public String getAction(){
            return action;
        }
        public String getDate(){
            return date;
        }
        public String getTime(){
            return time;
        }
        public String getRoom(){
            return room;
        }
        public String getModule(){
            return module;
        }  
        
        public String getDetails(){
            return date + ", " + time + ", " + room + ", " + module;
        }
    }
    
    
    
    
    
    //VIEW
    
    public Button badd, bdelete, bdisplay, bother, bexit;  //main menu buttons
    public Button baddsubmit, bdeletesubmit;               //submit buttons
    public Button bbs[] = new Button[4];                   //back buttons
    public Alert successAlert, errorAlert;                 //Alerts for message display
    
    public ChoiceBox aTimeBox, aDayBox, aModuleBox;        //checkBoxes for add
    public ChoiceBox bTimeBox, bDayBox, bModuleBox;        //checkBoxes for delete
    public TextField aRoom, bRoom, otherBox;               //textfield for the room number
    
    
    public String[] hours = {"9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
    public String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public String[] modules = {"CS-1987", "CS-1906", "CS-4424", "CS-3301", "CS1777"};
    
    
    public VBox menu = new VBox();
    public VBox addMenu = new VBox(10);
    public VBox deleteMenu = new VBox(10);
    public VBox otherMenu = new VBox(10);
    public GridPane timetable = new GridPane();
    
    public Scene main,addSc,delSc,otherSc,ttSc;
    
    public Stage window;
    public Boolean isAdding;
            
    @Override
    public void start(Stage stage){
        window = stage; //allow stage to be acessed from outside the method
        
        //setting up buttons
        badd = new Button("Add lecture");
        badd.setMinSize(300, 60);

        bdelete = new Button("Remove lecture");
        bdelete.setMinSize(300, 60);

        bdisplay = new Button("Get timetable");
        bdisplay.setMinSize(300, 60);

        bother = new Button("Other...");
        bother.setMinSize(300, 60);

        bexit = new Button("Exit");
        bexit.setMinSize(300, 60);

        baddsubmit = new Button("Submit");
        baddsubmit.setMinSize(100, 30);

        bdeletesubmit = new Button("Submit");
        bdeletesubmit.setMinSize(100, 30);

        for (int i = 0; i < 4; i++) {
            bbs[i] = new Button("Back");
            bbs[i].setMinSize(80, 30);
        }
        
        //setting up alerts
        successAlert = new Alert(AlertType.INFORMATION);
        successAlert.setTitle("Confirmation");
        errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");

        //setting up choiceBox and textFields
        aDayBox = new ChoiceBox<String>();
        aDayBox.getItems().addAll(days);

        aTimeBox = new ChoiceBox<String>();
        aTimeBox.getItems().addAll(hours);

        aModuleBox = new ChoiceBox<String>();
        aModuleBox.getItems().addAll(modules);

        aRoom = new TextField();
        aRoom.setPromptText("Room code");
        aRoom.setMaxWidth(150);

        bDayBox = new ChoiceBox<String>();
        bDayBox.getItems().addAll(days);

        bTimeBox = new ChoiceBox<String>();
        bTimeBox.getItems().addAll(hours);

        bModuleBox = new ChoiceBox<String>();
        bModuleBox.getItems().addAll(modules);

        bRoom = new TextField();
        bRoom.setPromptText("Room code");
        bRoom.setMaxWidth(150);

        otherBox = new TextField();
        otherBox.setMaxWidth(400);
        
        //main menu
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(badd, bdelete, bdisplay, bother, bexit);
        main = new Scene(menu, 400, 450);
        window.setScene(main);
        window.show();
        
        //add lecture scene
        addMenu.getChildren().addAll(
                new HBox(10, new Label("Time of the lecture:"), aTimeBox),
                new HBox(10, new Label("Date of the lecture:"), aDayBox),
                new HBox(10, new Label("Room number:"), aRoom),
                new HBox(10, new Label("Lecture module code:"), aModuleBox),
                new HBox(20, baddsubmit, bbs[0])
        );
        addMenu.setPadding(new Insets(20));
        addSc = new Scene(addMenu,450,500);
        
        //delete lecture scene
        deleteMenu.getChildren().addAll(
                new HBox(10, new Label("Time of the lecture:"), bTimeBox),
                new HBox(10, new Label("Date of the lecture:"), bDayBox),
                new HBox(10, new Label("Room number:"), bRoom),
                new HBox(10, new Label("Lecture module code:"), bModuleBox),
                new HBox(20, bdeletesubmit, bbs[1])
        );
        deleteMenu.setPadding(new Insets(20));
        delSc = new Scene(deleteMenu,450,500);
        
        //other option scene
        otherMenu.getChildren().addAll(new Label("What option, excluding the ones listed in the page before, would you like to do?"),
                otherBox, bbs[2]
        );
        otherMenu.setAlignment(Pos.CENTER);
        otherSc = new Scene(otherMenu,450,500);
        
        //generate the timetable
        buildTimetable();
        ttSc = new Scene(timetable,740,470);
        
        //handler calls
        badd.setOnAction(e -> window.setScene(addSc));
        bdelete.setOnAction(e -> window.setScene(delSc));
        bother.setOnAction(e -> window.setScene(otherSc));
        bdisplay.setOnAction(e -> {
            handleTimetable();
            window.setScene(ttSc);
                });
        bexit.setOnAction(e -> System.exit(0));
        
        for (int i = 0; i < 4; i++) {
            bbs[i].setOnAction(e -> window.setScene(main));
        }
        
        baddsubmit.setOnAction(e -> {
            isAdding = true;
            handleSubmit();
                });
        bdeletesubmit.setOnAction(e -> {
            isAdding = false;
            handleSubmit();
        });
        
    } 

    
    
    public void buildTimetable() {
        timetable.add(bbs[3], 0, 0);

        for (int col = 0; col < days.length; col++) {
            Label dayLabel = new Label(days[col]);
            dayLabel.setMinWidth(100);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            GridPane.setHgrow(dayLabel, javafx.scene.layout.Priority.ALWAYS);
            timetable.add(dayLabel, col + 1, 0);
        }

        for (int row = 0; row < hours.length; row++) {
            Label hourLabel = new Label(hours[row]);
            hourLabel.setMinWidth(80);
            hourLabel.setStyle("-fx-font-weight: bold;");
            timetable.add(hourLabel, 0, row + 1);
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
    }
    
    
    
    public static void main(String[] args) {
        launch();
    }
    
    
    
    
    //CONTROLLER
            
    public void handleSubmit(){
        if(isAdding == true){
            LectureRequest newRequest = new LectureRequest("Add",aDayBox.getValue().toString(),
                    aTimeBox.getValue().toString(),
                    aRoom.getText(),
                    aModuleBox.getValue().toString()
            );
        }
        else{
            LectureRequest newRequest = new LectureRequest("Delete",bDayBox.getValue().toString(),
                    bTimeBox.getValue().toString(),
                    bRoom.getText(),
                    bModuleBox.getValue().toString()
            );
        }
    }
    
    public void handleTimetable(){
        timetable.getChildren().clear();
        buildTimetable();
        
    }

    

}