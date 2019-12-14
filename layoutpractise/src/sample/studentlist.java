package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.Grid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class studentlist {
    private String teacher_id;
    Connection conn = null;
    Statement stmt = null;
    private ArrayList<String> listcontents = new ArrayList<>();
    private ArrayList<String> placelist = new ArrayList<>();
    private ArrayList<Integer> numberlist = new ArrayList<>();
    private ArrayList<Integer> capactiylist = new ArrayList<>();
    Label label = new Label("Your courses: ");
    Button button = new Button("Select");
    HBox hBox = new HBox();
    private  ArrayList<String> studentcode = new ArrayList<>();
    private ArrayList<String > studentname = new ArrayList<>();
    private ArrayList<Integer> score = new ArrayList<>();
    private ArrayList<String > courseid = new ArrayList<>();


    public studentlist(String id) {
        this.teacher_id = id;
    }

    public VBox showWindow() throws SQLException {
        listcontents.clear();
        VBox vBox = new VBox();
        ChoiceBox<String> courses = new ChoiceBox<>();
        hBox.getChildren().clear();

        try {
            if (conn == null) {
                conn=ConnectionConstruct.getConnection();
            }
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResultSet listrs = null;
        try{
            listrs = stmt.executeQuery("select c_id, c_name, c_place, c_number, c_capacity from course where c_t_id ='"+ teacher_id+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(listrs.next()){
            courseid.add(listrs.getString(1));
            listcontents.add(listrs.getString(2));
            placelist.add(listrs.getString(3));
            numberlist.add(Integer.parseInt(listrs.getString(4)));
            capactiylist.add(Integer.parseInt(listrs.getString(5)));
        }

        courses.getItems().addAll(listcontents);
        courses.setValue(listcontents.get(0));

        String selectedcourse = courses.getSelectionModel().getSelectedItem();

        VBox outerbox = new VBox();

        outerbox.getChildren().add(studentlist(selectedcourse, listcontents.indexOf(courses.getSelectionModel().getSelectedItem())));

        button.setOnAction(e-> {
            try {
                outerbox.getChildren().clear();
                System.out.println();
                outerbox.getChildren().add(studentlist(courses.getSelectionModel().getSelectedItem(),listcontents.indexOf(courses.getSelectionModel().getSelectedItem())));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        hBox.getChildren().addAll(label, courses, button);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().addAll(hBox, outerbox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(15));
        return vBox;
    }

    public VBox studentlist(String id, int j) throws SQLException {
        VBox stulist = new VBox();
        TableView table = new TableView();
        studentcode.clear();
        studentname.clear();
        score.clear();
        ResultSet studentsrs = null;
        ArrayList<resultdata> resultdataArrayList = new ArrayList<>();
        ObservableList<resultdata> data = FXCollections.observableArrayList();
        String s1 = "Classroom: "+ placelist.get(j);
        String s2 = "No. of students: "+ numberlist.get(j) +"/"+capactiylist.get(j);
        Label classroom = new Label(s1);
        Label stunumber = new Label(s2);
        HBox classlocation = new HBox();
        HBox capacity = new HBox();
        classlocation.getChildren().add(classroom);
        //classlocation.setAlignment(Pos.CENTER_LEFT);
        capacity.getChildren().add(stunumber);
        //capacity.setAlignment(Pos.CENTER_RIGHT);
        GridPane topbar = new GridPane();
        topbar.add(classlocation,1,1);
        topbar.add(capacity,1, 2);



        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Score = new TableColumn("Score");

        try{
            studentsrs = stmt.executeQuery("SELECT sc.s_id, s.s_name, sc.grade FROM selectedcourses sc NATURAL JOIN student s NATURAL JOIN course c WHERE sc.s_id=s.s_id and c.c_name='"+id+"'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        while(studentsrs.next()){
            studentcode.add(studentsrs.getString(1));
            studentname.add(studentsrs.getString(2));
            score.add(Integer.parseInt(studentsrs.getString(3)));
        }

        int results = studentcode.size();

        for(int i=0; i<results; i++){
            resultdataArrayList.add(new resultdata(studentcode.get(i),studentname.get(i),score.get(i)));
        }

        for (resultdata x :
                resultdataArrayList) {
            data.add(x);
        }

        IDcol.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("id")
        );

        Name.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("name")
        );

        Score.setCellValueFactory(
                new PropertyValueFactory<resultdata, Integer>("score")
        );


        table.setItems(data);
        table.getColumns().addAll(IDcol, Name, Score);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        stulist.getChildren().addAll(topbar, table);
        stulist.setPadding(new Insets(15));
        stulist.setSpacing(10);

        return stulist;
    }

    public class resultdata{
        private SimpleIntegerProperty score;
        private SimpleStringProperty id;
        private SimpleStringProperty name;

        public resultdata(String id, String name, int score){
            this.id=new SimpleStringProperty(id);
            this.name=new SimpleStringProperty(name);
            this.score=new SimpleIntegerProperty(score);
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

        public String getId() {
            return id.get();
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }
}
