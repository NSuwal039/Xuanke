package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.sql.*;
import java.util.ArrayList;

public class coursefullstudent {
    private String stu_id;
    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <Integer> classtime = new ArrayList<>();
    private ArrayList <String> venuelist = new ArrayList<>();
    private ArrayList <Integer> numberlist = new ArrayList<>();
    private ArrayList <Integer> capacitylist = new ArrayList<>();
    private ArrayList <ArrayList<Integer>> timelist= new ArrayList<>();
    private ArrayList <String> teachername= new ArrayList<>();

    private Label label = new Label("Enter course id: ");
    private TextField textField = new TextField();
    private Button button = new Button("Search");
    private Button confirm = new Button("Confirm");

    private Label Code = new Label("Course Code: ");
    private Label Name = new Label("Course: ");
    private Label Teacher = new Label("Teacher: ");
    private Label Classroom = new Label("Classroom: ");
    private Label Time = new Label("Time: ");
    private Label Capacity = new Label("Capacity");
    private Label Code1 = new Label("");
    private Label Name1 = new Label("");
    private Label Teacher1 = new Label("");
    private Label Classroom1 = new Label("");
    private Label Time1 = new Label("");
    private Label Capacity1 = new Label("");

    GridPane grid = new GridPane();

    Connection conn = null;
    Statement stmt = null;

    HBox topMenu = new HBox();
    VBox resultbox = new VBox();

    Alert nocourse = new Alert(Alert.AlertType.ERROR);
 //--------------------------------------------------------------------------------------------------------------------------------------------

    public coursefullstudent (String id){
        this.stu_id = id;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------

    public VBox showWindow (){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(25));
        vBox.setSpacing(10);
        topMenu.getChildren().clear();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setPadding(new Insets(20));

        grid.getChildren().clear();
        grid.add(Code ,0, 0);
        grid.add(Name ,0, 1);
        grid.add(Teacher ,0, 2);
        grid.add(Classroom ,0, 3);
        grid.add(Time ,0, 4);
        grid.add(Capacity,0,5);

        grid.add(Code1 ,1, 0);
        grid.add(Name1 ,1, 1);
        grid.add(Teacher1 ,1, 2);
        grid.add(Classroom1 ,1, 3);
        grid.add(Time1 ,1, 4);
        grid.add(Capacity1,1,5);
        grid.add(confirm,0,6);
        confirm.setDisable(true);


        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
                stmt = conn.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        button.setOnAction(e->{
            try {
                searchaction(textField.getText());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        confirm.setOnAction((e->{
            try {
                confirmadd();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }));


        topMenu.getChildren().addAll(label, textField, button);
        topMenu.setSpacing(20);
        topMenu.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().addAll(topMenu,grid);

        return vBox;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    public void searchaction(String courseid) throws SQLException {
        courseidlist.clear();
        coursenamelist.clear();
        teacherlist.clear();
        classtime.clear();
        venuelist.clear();
        timelist.clear();
        String temp = "";
        /**if (confirm.isDisabled()==false){
            confirm.setDisable(true);
        }**/

        int size =0;
        ResultSet rs =null;
        try{
            rs = stmt.executeQuery("SELECT c.c_id, c.c_name, t.t_name, c.c_place, c.c_number, c.c_capacity, c.c_t_id FROM course c JOIN teacher t ON c.c_t_id=t.t_id WHERE (c.c_capacity=c.c_number OR c.c_number>c.c_capacity) AND c.c_id='"+courseid+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (rs.next()){
            courseidlist.add(rs.getString(1));
            coursenamelist.add(rs.getString(2));
            teacherlist.add(rs.getString(3));
            venuelist.add(rs.getString(4));
            numberlist.add(Integer.parseInt(rs.getString(5)));
            capacitylist.add(Integer.parseInt(rs.getString(6)));
            teachername.add(rs.getString(7));
            size++;
        }

        try{
            ResultSet timers = stmt.executeQuery("Select ctime_t_id from classtime WHERE ctime_c_id ='"+ courseid+"'");
            while(timers.next()){
                classtime.add(Integer.parseInt(timers.getString(1)));
            }
            //System.out.println(classtime);

        }catch (Exception e){
            e.printStackTrace();
        }
        timelist.add(classtime);

        if (size==0) {
            nocourse.setContentText("No course of id:" + courseid + "found. The code you entered might be incorrect or the course may not be full.");
            if (confirm.isDisabled()==false){
             confirm.setDisable(true);
             }
            nocourse.showAndWait();
        }else{
            Code1.setText(courseidlist.get(0));
            Name1.setText(coursenamelist.get(0));
            Teacher1.setText(teacherlist.get(0));
            Classroom1.setText(venuelist.get(0));
            for (int i :
                    classtime) {
                temp +=timecodeToString.timecodeToString(i) + "\n";
            }

            Time1.setText(temp);
            Capacity1.setText(numberlist.get(0)+"/"+capacitylist.get(0));
            confirm.setDisable(false);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

    private void confirmadd() throws SQLException {
        ResultSet check = stmt.executeQuery("SELECT Count(*) from course_request where req_s_id = '"+stu_id +"' and req_c_id='"+courseidlist.get(0)+"' and req_t_id='"+teachername.get(0)+"'");
        int results=0;
        while (check.next()){
            results=check.getInt(1);
        }
        System.out.println(results);

        if(results==0) {
            String sql1 = "INSERT into course_request (req_s_id, req_c_id, req_t_id, Status) values (?,?,?,?)";
            PreparedStatement statement1 = conn.prepareStatement(sql1);
            statement1.setString(1, stu_id);
            statement1.setString(2, courseidlist.get(0));
            statement1.setString(3, teachername.get(0));
            statement1.setString(4,"Pending");

            statement1.execute();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Application successful. Please wait for the professor to review your application.");
            alert1.showAndWait();
            confirm.setDisable(true);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You have already applied for this course. Please wait for the professor to review your application");
            alert.showAndWait();
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------

}
