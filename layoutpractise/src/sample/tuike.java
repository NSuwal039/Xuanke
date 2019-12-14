package sample;

import com.mysql.cj.protocol.Resultset;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.util.ArrayList;

public class tuike {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <String> venuelist = new ArrayList<>();
    private ArrayList <Integer> numberlist = new ArrayList<>();
    private ArrayList <Integer> capacitylist = new ArrayList<>();
    private ArrayList <String> originalcourseid = new ArrayList<>();
    private ArrayList <Integer> classtime = new ArrayList<>();
    private ArrayList <ArrayList<Integer>> timelist= new ArrayList<>();
    private String stu_id;
    VBox vBox = new VBox();

    Label label = new Label("Your Courses:");
    TableView table = new TableView();
    ObservableList<resultdata> data = FXCollections.observableArrayList();
    TableColumn IDcol = new TableColumn("ID");
    TableColumn Name = new TableColumn("Name");
    TableColumn Teacher = new TableColumn("Teacher");
    TableColumn Classroom = new TableColumn("Classroom");
    TableColumn Capacity = new TableColumn("Capacity");
    TableColumn Time = new TableColumn("Time");

    public tuike(String id) throws SQLException {this.stu_id = id;}

    public VBox tuikepage() throws SQLException {

        courseidlist.clear();
        coursenamelist.clear();
        teacherlist.clear();
        venuelist.clear();
        numberlist.clear();
        capacitylist.clear();
        timelist.clear();
        classtime.clear();

        try{
            if(conn==null){
                conn = ConnectionConstruct.getConnection();
                stmt = conn.createStatement();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            rs = stmt.executeQuery("SELECT c.c_id,c.c_name,t.t_name,c.c_number,c.c_capacity,c.c_place FROM teacher t , course c NATURAL JOIN selectedcourses WHERE s_id='" + stu_id + "' and (c.c_t_id = t.t_id)");
            while (rs.next()) {
                courseidlist.add(rs.getString(1));
                coursenamelist.add(rs.getString(2));
                teacherlist.add(rs.getString(3));
                numberlist.add(Integer.parseInt(rs.getString(4)));
                capacitylist.add(Integer.parseInt(rs.getString(5)));
                venuelist.add(rs.getString(6));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

            try{
                for (String x :
                        courseidlist) {
                    ResultSet timers = stmt.executeQuery("Select t.ctime_t_id from classtime t where t.ctime_c_id = '"+x +"'");
                    while (timers.next()) {
                        classtime.add(Integer.parseInt(timers.getString(1)));
                    }
                    timelist.add(new ArrayList<Integer>(classtime));

                    /**ArrayList<Integer> temp = new ArrayList<>();
                    for (Integer y :
                            classtime) {
                        temp.add(y);
                    }
                    timelist.add(temp);**/

                    classtime.clear();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        /**ResultSet timers = stmt.executeQuery("Select t.ctime_t_id from classtime t where t.ctime_c_id = '"+courseidlist.get(0) +"'");
        while (timers.next()) {
            classtime.add(Integer.parseInt(timers.getString(1)));
        }


        classtime.clear();
         **/

            Integer results = courseidlist.size();
            ArrayList<resultdata> resultdataArrayList = new ArrayList();

            for(int i=0; i<results;i++){
                resultdataArrayList.add(new resultdata(courseidlist.get(i),coursenamelist.get(i),teacherlist.get(i),venuelist.get(i),numberlist.get(i),capacitylist.get(i),timelist.get(i)));
            }

        for (resultdata x :
                resultdataArrayList) {
            data.add(x);
        }

        IDcol.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata, String>("courseid")
        );

        Name.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata, String>("coursename")
        );

        Teacher.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata,String >("teacher")
        );

        Classroom.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata,String>("classroom")
        );

        Capacity.setCellValueFactory(

                new PropertyValueFactory<xuanke.resultdata, String>("capacitydisplay")
        );
        Time.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata,String >("time")
        );

        table.setItems(data);
        table.getColumns().addAll(IDcol,Name,Teacher,Classroom,Capacity,Time);
        table.setEditable(false);


        vBox.getChildren().addAll(label,table);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(15);
        return vBox;
        }

        public void deleteCourse() throws SQLException {
            resultdata selectedcourse = (resultdata) table.getSelectionModel().getSelectedItem();
            String deletesql = "Delete from selectedcourses where s_id ='"+ stu_id+"'and c_id ='"+ selectedcourse.courseid.get()+"'";
            String subsql = "Update course set c_number = ? where c_id =?";
            int s = 0;

            PreparedStatement preparedStatement = conn.prepareStatement(subsql);
            preparedStatement.setInt(1, selectedcourse.number.get()-1);
            preparedStatement.setString(2, selectedcourse.courseid.get());

            try{
                 s = stmt.executeUpdate(deletesql);
                 preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            table.getItems().remove(selectedcourse);
        }

    public class resultdata {

        SimpleStringProperty courseid;
        SimpleStringProperty coursename;
        SimpleStringProperty teacher;
        SimpleStringProperty classroom;
        SimpleStringProperty capacitydisplay;
        SimpleIntegerProperty number;
        SimpleIntegerProperty capacity;
        SimpleStringProperty time;
        ArrayList<Integer> timearray;

        public resultdata(String id, String name, String teacher, String classroom, Integer num, Integer cap, ArrayList<Integer> times){
            this.courseid = new SimpleStringProperty(id);
            this.coursename = new SimpleStringProperty(name);
            this.teacher = new SimpleStringProperty(teacher);
            this.classroom = new SimpleStringProperty(classroom);
            this.number = new SimpleIntegerProperty(num);
            this.capacity = new SimpleIntegerProperty(cap);
            this.capacitydisplay = new SimpleStringProperty(number.get() + "/" + capacity.get());
            String temp = "";
            for (int i :
                    times) {
                temp +=timecodeToString.timecodeToString(i) + "\n";
            }
            this.time = new SimpleStringProperty(temp);
            this.timearray = times;
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

        public void setCapacity(int capacity) {
            this.capacity.set(capacity);
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

        public String getTeacher() {
            return teacher.get();
        }

        public SimpleStringProperty teacherProperty() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher.set(teacher);
        }

        public String getClassroom() {
            return classroom.get();
        }

        public SimpleStringProperty classroomProperty() {
            return classroom;
        }

        public void setClassroom(String classroom) {
            this.classroom.set(classroom);
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

    }


}
