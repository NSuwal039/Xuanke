package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class chengji {
    private String stu_id;
    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <Integer> scorelist = new ArrayList<>();

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public chengji(String sid){this.stu_id = sid;}

    public VBox chengjipage() throws SQLException {
        VBox vBox = new VBox();
        Label label = new Label("Your scores");
        TableView table = new TableView();
        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Teacher = new TableColumn("Teacher");
        TableColumn Score = new TableColumn("Score");
        ObservableList<resultdata> data =FXCollections.observableArrayList();
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
            rs = stmt.executeQuery("select sc.c_id, c.c_name, t.t_name, sc.grade FROM teacher t, selectedcourses sc NATURAL JOIN course c where sc.s_id = '"+ stu_id+"' AND t.t_id = c.c_t_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (rs.next()){
            courseidlist.add(rs.getString(1));
            coursenamelist.add(rs.getString(2));
            teacherlist.add(rs.getString(3));
            scorelist.add((Integer.parseInt(rs.getString(4))));
        }

        Integer results = courseidlist.size();
        for(int i=0; i<results; i++){
            resultdataArrayList.add(new resultdata(courseidlist.get(i), coursenamelist.get(i), teacherlist.get(i),scorelist.get(i)));
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

        Score.setCellValueFactory(
                new PropertyValueFactory<xuanke.resultdata,Integer>("score")
        );

        table.setItems(data);
        table.getColumns().addAll(IDcol,Name,Teacher,Score);
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
        SimpleIntegerProperty score;

        public resultdata(String id, String name, String teacher, Integer score){
            this.courseid=new SimpleStringProperty(id);
            this.coursename=new SimpleStringProperty(name);
            this.teacher=new SimpleStringProperty(teacher);
            this.score=new SimpleIntegerProperty(score);
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

        public int getScore() {
            return score.get();
        }

        public SimpleIntegerProperty scoreProperty() {
            return score;
        }

        public void setScore(int score) {
            this.score.set(score);
        }

    }
}
