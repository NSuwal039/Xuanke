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

public class kaoshi {
    private String stu_id;
    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <String> examformat = new ArrayList<>();
    private ArrayList <String> examtime = new ArrayList<>();

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public kaoshi(String sid){this.stu_id = sid;}

    public VBox kaoshipage() throws SQLException {
        VBox vBox = new VBox();
        Label label = new Label("Your scores");
        TableView table = new TableView();
        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Teacher = new TableColumn("Teacher");
        TableColumn Format = new TableColumn("Format");
        TableColumn Time = new TableColumn("Time");
        ObservableList<resultdata> data = FXCollections.observableArrayList();
        ArrayList<resultdata> resultdataArrayList = new ArrayList<>();

        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c.c_id, c.c_name, t.t_name, c.c_exam_form, c.c_exam_time FROM selectedcourses sc NATURAL JOIN course c, teacher t WHERE sc.s_id = '"+stu_id+"' and t.t_id=c.c_t_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (rs.next()){
            courseidlist.add(rs.getString(1));
            coursenamelist.add(rs.getString(2));
            teacherlist.add(rs.getString(3));
            examformat.add(rs.getString(4));
            examtime.add(rs.getString(5));
        }

        Integer results = courseidlist.size();
        for(int i=0; i<results; i++){
            resultdataArrayList.add(new resultdata(courseidlist.get(i), coursenamelist.get(i), teacherlist.get(i),examformat.get(i),examtime.get(i)));
        }

        for (resultdata x :
                resultdataArrayList) {
            data.add(x);
        }

        IDcol.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("courseid")
        );

        Name.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("coursename")
        );

        Teacher.setCellValueFactory(
                new PropertyValueFactory<resultdata,String >("teacher")
        );

        Format.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("format")
        );

        Time.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("time")
        );


        table.setItems(data);
        table.getColumns().addAll(IDcol,Name,Teacher,Format,Time);
        table.setEditable(false);
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(label,table);

        return vBox;
    }

    public class resultdata{
        SimpleStringProperty courseid;
        SimpleStringProperty coursename;
        SimpleStringProperty teacher;
        SimpleStringProperty format;
        SimpleStringProperty time;

        public resultdata(String id, String name, String teacher, String format, String time){
            this.courseid=new SimpleStringProperty(id);
            this.coursename=new SimpleStringProperty(name);
            this.teacher=new SimpleStringProperty(teacher);
            this.format=new SimpleStringProperty(format);
            this.time=new SimpleStringProperty(time);
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

        public String getFormat() {
            return format.get();
        }

        public SimpleStringProperty formatProperty() {
            return format;
        }

        public void setFormat(String format) {
            this.format.set(format);
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
