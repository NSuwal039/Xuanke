package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class teacherclasslist {
    private String teacher_id;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> venuelist = new ArrayList<>();
    private ArrayList <Integer> numberlist = new ArrayList<>();
    private ArrayList <Integer> capacitylist = new ArrayList<>();
    private ArrayList <Integer> classtime = new ArrayList<>();
    private ArrayList <ArrayList<Integer>> timelist= new ArrayList<>();


    public teacherclasslist (String id){
        this.teacher_id=id;
    }

    public VBox showWindow() throws SQLException {
        VBox vBox = new VBox();
        Label label = new Label("Your classes");
        ObservableList<resultdata> data= FXCollections.observableArrayList();
        ArrayList<resultdata> resultdataArraylist = new ArrayList();
        courseidlist.clear();
        coursenamelist.clear();
        venuelist.clear();
        numberlist.clear();
        capacitylist.clear();
        classtime.clear();
        timelist.clear();

        TableView table = new TableView();
        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Classroom = new TableColumn("Classroom");
        TableColumn Capacity = new TableColumn("Capacity");
        TableColumn Time = new TableColumn("Time");


        try {
            if (conn == null) {
                conn= ConnectionConstruct.getConnection();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            rs=stmt.executeQuery("SELECT c.c_id,c.c_name,c.c_place,c.c_number,c.c_capacity FRom course c WHERE c.c_t_id='"+teacher_id+"'");
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()){
            courseidlist.add(rs.getString(1));
            coursenamelist.add(rs.getString(2));
            venuelist.add(rs.getString(3));
            numberlist.add(Integer.parseInt(rs.getString(4)));
            capacitylist.add(Integer.parseInt(rs.getString(5)));
        }

        for (String x:
             courseidlist) {
            try{
                ResultSet timers = stmt.executeQuery("Select ctime_t_id from classtime where ctime_c_id='"+x +"'");
                while (timers.next()) {
                    classtime.add(Integer.parseInt(timers.getString(1)));
                }
                timelist.add(new ArrayList<Integer>(classtime));
                classtime.clear();
            }catch (Exception e){e.printStackTrace();}
        }


        int results = courseidlist.size();
        /**System.out.println(results);
        System.out.println(classtime.size());
        System.out.println(classtime);
        System.out.println(timelist.size());
        System.out.println(timelist);**/

        for(int i=0; i<results; i++){
            resultdataArraylist.add(new resultdata(courseidlist.get(i), coursenamelist.get(i), venuelist.get(i),numberlist.get(i), capacitylist.get(i), timelist.get(i)));
        }

        for (resultdata x :
                resultdataArraylist) {
            data.add(x);
        }

        IDcol.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("courseid")
        );

        Name.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("coursename")
        );

        Classroom.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("place")
        );

        Capacity.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("capacitydisplay")
        );

        Time.setCellValueFactory(
                new PropertyValueFactory<resultdata,String >("time")
        );

        table.setItems(data);
        table.getColumns().addAll(IDcol, Name, Classroom, Capacity, Time);
        table.setEditable(false);

        Label label1 = new Label("Your classes:");
        vBox.getChildren().addAll(label1, table);
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(10);

        return vBox;
    }

    public class resultdata{
        SimpleStringProperty courseid;
        SimpleStringProperty coursename;
        SimpleStringProperty place;
        SimpleStringProperty capacitydisplay;
        SimpleIntegerProperty number;
        SimpleIntegerProperty capacity;
        SimpleStringProperty time;
        ArrayList<Integer> timearray;

        public resultdata (String id, String name, String place, Integer num, Integer cap, ArrayList<Integer> times){
            this.courseid = new SimpleStringProperty(id);
            this.coursename= new SimpleStringProperty(name);
            this.place = new SimpleStringProperty(place);
            this.number = new SimpleIntegerProperty(num);
            this.capacity = new SimpleIntegerProperty(cap);
            this.capacitydisplay = new SimpleStringProperty(number.get() +"/"+capacity.get());

            String temp = "";
            for (int i :
                    times) {
                temp +=timecodeToString.timecodeToString(i) + "\n";
            }
            this.time = new SimpleStringProperty(temp);
            this.timearray = times;

        }

        public String getCourseid() {
            return courseid.get();
        }

        public SimpleStringProperty courseidProperty() {
            return courseid;
        }

        public void setCourseid(String courseid) {
            this.courseid.set(courseid);
        }

        public String getCoursename() {
            return coursename.get();
        }

        public SimpleStringProperty coursenameProperty() {
            return coursename;
        }

        public void setCoursename(String coursename) {
            this.coursename.set(coursename);
        }

        public String getPlace() {
            return place.get();
        }

        public SimpleStringProperty placeProperty() {
            return place;
        }

        public void setPlace(String place) {
            this.place.set(place);
        }

        public String getCapacitydisplay() {
            return capacitydisplay.get();
        }

        public SimpleStringProperty capacitydisplayProperty() {
            return capacitydisplay;
        }

        public void setCapacitydisplay(String capacitydisplay) {
            this.capacitydisplay.set(capacitydisplay);
        }

        public int getNumber() {
            return number.get();
        }

        public SimpleIntegerProperty numberProperty() {
            return number;
        }

        public void setNumber(int number) {
            this.number.set(number);
        }

        public int getCapacity() {
            return capacity.get();
        }

        public SimpleIntegerProperty capacityProperty() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity.set(capacity);
        }

        public String getTime() {
            return time.get();
        }

        public SimpleStringProperty timeProperty() {
            return time;
        }

        public void setTime(String time) {
            this.time.set(time);
        }

        public ArrayList<Integer> getTimearray() {
            return timearray;
        }

        public void setTimearray(ArrayList<Integer> timearray) {
            this.timearray = timearray;
        }
    }
}
