package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class coursefullteacher {
    private String teacher_id;

    public coursefullteacher(String id){this.teacher_id=id;}

    private ArrayList<String> courseidlist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <Integer> numberlist = new ArrayList<>();
    private ArrayList<String> studentlist = new ArrayList<>();
    private ArrayList<String> studentname = new ArrayList<>();
    private ArrayList<String> coursename = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();

    private TableView<resultdata> table = new TableView<>();
    private TableColumn Student = new TableColumn("StudentID");
    private TableColumn StudentName = new TableColumn("StudentName");
    private TableColumn Course = new TableColumn("CourseID");
    private TableColumn CourseName = new TableColumn("CourseName");
    private TableColumn Status = new TableColumn("Status");

    Button confirm = new Button("Confirm");
    Button reject = new Button("Reject");
    HBox bottombar = new HBox();

    Connection conn=null;
    Statement stmt =null;
    ResultSet rs= null;
    ObservableList<resultdata> data = FXCollections.observableArrayList();
    ArrayList<resultdata> resultdataArrayList = new ArrayList();

    public VBox showWindow() throws SQLException {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(15));
        courseidlist.clear();
        teacherlist.clear();
        numberlist.clear();
        studentlist.clear();
        studentname.clear();
        coursename.clear();
        vBox.getChildren().clear();
        bottombar.getChildren().clear();
        table.getColumns().clear();
        resultdataArrayList.clear();
        table.getItems().clear();

        ResultSet students = null;
        ResultSet course = null;

        try{
            if (conn == null) {
                conn=ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt=conn.createStatement();

        try{
            rs=stmt.executeQuery("Select req_s_id, req_c_id, Status from course_request where req_t_id = '" +teacher_id+"' and Status='Pending'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(rs.next()){
            studentlist.add((rs.getString(1)));
            courseidlist.add(rs.getString(2));
            status.add(rs.getString(3));
        }

        for (String x :
                studentlist) {
            students = stmt.executeQuery("Select s_name from student where s_id='"+x+"'");
            while (students.next()) {
                studentname.add(students.getString(1));
            }
        }

        for (String x :
                courseidlist) {
            course=stmt.executeQuery("Select c_name from course where c_id ='"+x+"'");
            while(course.next()) {
                coursename.add(course.getString(1));
            }
        }

        int results = studentlist.size();

        for(int i=0; i<results; i++){
            resultdataArrayList.add(new resultdata(studentlist.get(i), courseidlist.get(i),studentname.get(i),coursename.get(i),status.get(i)));
        }
        for (resultdata x :
                resultdataArrayList) {
            data.add(x);
        }

        Student.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("studentid")
        );

        StudentName.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("studentname")
        );

        Course.setCellValueFactory(
                new PropertyValueFactory<resultdata,String>("courseid")
        );

        CourseName.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("coursename")
        );

        Status.setCellValueFactory(
                new PropertyValueFactory<resultdata, String>("status")
        );

        confirm.setOnAction(e->{
            try {
                confirmrequest();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        reject.setOnAction(e->{
            try {
                rejectrequest();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        table.setItems(data);
        table.getColumns().addAll(Student, StudentName, Course, CourseName,Status);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        bottombar.setSpacing(50);
        bottombar.getChildren().addAll(confirm,reject);

        vBox.getChildren().addAll(table,bottombar);
        return vBox;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void confirmrequest() throws SQLException {
        resultdata selectedcourse = (resultdata) table.getSelectionModel().getSelectedItem();
        ResultSet coursers = stmt.executeQuery("Select c_number from course where c_id ='"+selectedcourse.courseid.get()+"' and c_t_id='"+teacher_id+"'");
        while(coursers.next()){
            numberlist.add(Integer.parseInt(coursers.getString(1)));
        }

        String addsql ="insert into selectedcourses (s_id, c_id, grade, course_status) values (?,?,?,?)";
        String numbersql="update course set c_number = ?  where c_id=?";

        PreparedStatement addStatement = conn.prepareStatement(addsql);
        addStatement.setString(1,selectedcourse.studentid.get());
        addStatement.setString(2,selectedcourse.courseid.get());
        addStatement.setInt(3, 0);
        addStatement.setString(4,"Enrolled");

        PreparedStatement numberstatement = conn.prepareStatement(numbersql);
        numberstatement.setInt(1, numberlist.get(0)+1);
        numberstatement.setString(2, selectedcourse.courseid.get());

        PreparedStatement statusstatement = conn.prepareStatement("update course_request set Status=? where req_s_id=? and req_c_id=? and req_t_id=?");
        statusstatement.setString(1, "Accepted");
        statusstatement.setString(2, selectedcourse.studentid.get());
        statusstatement.setString(3, selectedcourse.courseid.get());
        statusstatement.setString(4, teacher_id);

        addStatement.executeUpdate();
        numberstatement.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Application accepted.");
        alert.showAndWait();
        table.getItems().remove(selectedcourse);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void rejectrequest() throws SQLException {
        resultdata selectedcourse = (resultdata) table.getSelectionModel().getSelectedItem();

        String deletesql ="Update course_request set Status=? where req_s_id =? and req_c_id=? and req_t_id=?";
        PreparedStatement deletestatement = conn.prepareStatement(deletesql);
        deletestatement.setString(1, "Rejected");
        deletestatement.setString(2, selectedcourse.studentid.get());
        deletestatement.setString(3, selectedcourse.courseid.get());
        deletestatement.setString(4, teacher_id);
        deletestatement.execute();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Application Rejected");
        alert.showAndWait();
        table.getItems().remove(selectedcourse);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    public class resultdata{
        private SimpleStringProperty studentid;
        private SimpleStringProperty courseid;
        private SimpleStringProperty studentname;
        private SimpleStringProperty coursename;
        private SimpleStringProperty status;

        public resultdata(String student, String course, String studentname, String coursename, String status){
            this.studentid= new SimpleStringProperty(student);
            this.courseid=new SimpleStringProperty(course);
            this.studentname = new SimpleStringProperty(studentname);
            this.coursename= new SimpleStringProperty(coursename);
            this.status=new SimpleStringProperty(status);
        }

        public String getStudentid() {
            return studentid.get();
        }

        public SimpleStringProperty studentidProperty() {
            return studentid;
        }

        public void setStudentid(String studentid) {
            this.studentid.set(studentid);
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

    public String getStudentname() {
        return studentname.get();
    }

    public SimpleStringProperty studentnameProperty() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname.set(studentname);
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

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
}
