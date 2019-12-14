package sample;

import com.mysql.cj.xdevapi.Table;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class setscore {
    private String teacher_id;
    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList<String> coursename = new ArrayList<>();
    private ArrayList<String> studentid = new ArrayList<>();
    private ArrayList<String> studentname = new ArrayList<>();
    private ArrayList<Integer> score = new ArrayList<>();
    VBox outerbox = new VBox();

    Connection conn = null;
    Statement stmt= null;

    Label label = new Label("Your courses: ");
    Button button = new Button("Select");
    HBox hBox = new HBox();
    VBox vbox= new VBox();
    ChoiceBox<String> courses = new ChoiceBox<>();

    public setscore(String id){
        this.teacher_id = id;
    }

    public VBox scorelist() throws SQLException {
        vbox.setPadding(new Insets(15));
        vbox.setSpacing(10);
        ArrayList<studentlist.resultdata> resultdataArrayList = new ArrayList<>();
        ObservableList<studentlist.resultdata> data = FXCollections.observableArrayList();
        hBox.getChildren().clear();
        vbox.getChildren().clear();
        outerbox.getChildren().clear();
        courses.getItems().clear();

        courseidlist.clear();
        coursename.clear();

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
            listrs = stmt.executeQuery("select c_id, c_name from course where c_t_id ='"+ teacher_id+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(listrs.next()){
            courseidlist.add(listrs.getString(1));
            coursename.add((listrs.getString(2)));
        }

        courses.getItems().addAll(coursename);
        courses.setValue(coursename.get(0));

        String selectedcourse = courses.getSelectionModel().getSelectedItem();

        outerbox.getChildren().add(studentlist(selectedcourse));

        button.setOnAction(e-> {
            try {
                outerbox.getChildren().clear();
                System.out.println();
                outerbox.getChildren().add(studentlist(courses.getSelectionModel().getSelectedItem()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        hBox.getChildren().addAll(label, courses, button);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_LEFT);

        vbox.getChildren().addAll(hBox, outerbox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15));

        return vbox;
    }

    public VBox studentlist(String id) throws SQLException {
        VBox box = new VBox();
        String c_id = id;
        box.getChildren().clear();

        TableView table= new TableView();
        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Score = new TableColumn("Score");

        studentid.clear();
        studentname.clear();
        score.clear();

        ResultSet studentsrs = null;
        ArrayList<resultdata> resultdataArrayList = new ArrayList<>();
        ObservableList<resultdata> data = FXCollections.observableArrayList();

        try{
            studentsrs = stmt.executeQuery("SELECT sc.s_id, s.s_name, sc.grade FROM selectedcourses sc NATURAL JOIN student s NATURAL JOIN course c WHERE sc.s_id=s.s_id and c.c_name='"+id+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while(studentsrs.next()){
            studentid.add(studentsrs.getString(1));
            studentname.add(studentsrs.getString(2));
            score.add(Integer.parseInt(studentsrs.getString(3)));
        }

        int results = studentid.size();

        for(int i=0; i<results; i++){
            resultdataArrayList.add(new resultdata(studentid.get(i), studentname.get(i), score.get(i)));
        }

        for (resultdata x :
                resultdataArrayList) {
            data.add(x);
        }

        IDcol.setCellValueFactory(
                new PropertyValueFactory<studentlist.resultdata, String>("id")
        );

        Name.setCellValueFactory(
                new PropertyValueFactory<studentlist.resultdata, String>("name")
        );

        Score.setCellValueFactory(
                new PropertyValueFactory<studentlist.resultdata, Integer>("score")
        );

        table.setRowFactory(e->{
            TableRow<resultdata> row = new TableRow<>();

                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        try {
                        //System.out.println(row.getItem().id.get());
                            studentscore(c_id, row.getItem().id.get());
                    } catch (NullPointerException | SQLException e1) {
                        System.out.println("wtf");
                    }
                    }
                });


            return row;
        });

        table.setItems(data);
        table.getColumns().addAll(IDcol, Name, Score);
        //table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        box.getChildren().add(table);

        return box;
    }

    public void studentscore(String cour_id, String stu_id) throws SQLException {
         String course_id = null;
         String student_id = stu_id;

         Stage stage = new Stage();
         HBox hBox = new HBox();
         Label label = new Label("Input score:");
         TextField textField = new TextField();
         Button confirm = new Button("Confirm");
         hBox.setAlignment(Pos.BASELINE_CENTER);
         

        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setContentText("Please enter a score between 0 and 4");

        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setContentText("Score updated");

        ResultSet coursename = stmt.executeQuery("Select c_id from course where c_name = '"+cour_id+"'");
        while(coursename.next()){
            course_id=coursename.getString(1);
        }

        String c = course_id;

        ResultSet scorers = null;
        String sql = "Update selectedcourses set grade = '"+textField.getText() +"'where s_id ='"+student_id+"' and c_id='"+course_id+"'";

         confirm.setOnAction(e->{
             System.out.println(c);
             System.out.println(student_id);
             //sql shit here
             if (isInteger(textField.getText())){
                 if (Integer.parseInt(textField.getText()) >= 0 && Integer.parseInt(textField.getText())<=4){
                     try {
                         PreparedStatement preparedStatement = conn.prepareStatement("Update selectedcourses set grade = ? where s_id =? and c_id =?");
                         preparedStatement.setInt(1, Integer.parseInt(textField.getText()));
                         preparedStatement.setString(2, student_id);
                         preparedStatement.setString(3,c);
                         preparedStatement.executeUpdate();
                         alert2.showAndWait();
                         outerbox.getChildren().clear();
                         outerbox.getChildren().add(studentlist(courses.getSelectionModel().getSelectedItem()));
                         stage.close();
                     } catch (SQLException ex) {
                         ex.printStackTrace();
                     }

                 }
             }else{
                 alert1.showAndWait();
             }


         });

         hBox.getChildren().addAll(label, textField, confirm);
         Scene scene = new Scene(hBox);
         stage.setScene(scene);
         stage.show();

    }

    public class resultdata{
        private SimpleStringProperty id;
        private SimpleStringProperty name;
        private SimpleIntegerProperty score;

        public resultdata(String id, String name, int score){
            this.id= new SimpleStringProperty(id);
            this.name= new SimpleStringProperty(name);
            this.score = new SimpleIntegerProperty(score);
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

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
