package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class addCourse {

    private Label courseid = new Label("Course ID: ");
    private Label coursename = new Label("Course Name: ");
    private Label creditlabel = new Label("Credits: ");
    private Label teacher = new Label("Professor: ");
    private Label finishweeklabel = new Label("Course End Week: ");
    private Label capacitylabel = new Label("Capacity: ");
    private Label examformat = new Label("Exam format: ");
    private Label examdate = new Label("Exam Date: ");
    private Label examtime = new Label("Exam Time: ");
    private Label classroomlabel = new Label("Classroom: ");
    private Label deptlabel = new Label("Department: ");
    private Label semesterlabel = new Label("Semester: ");
    private Label yearlabel = new Label("Year: ");
    private Label classtime = new Label("Class Time: ");

    private TextField courseidField = new TextField();
    private TextField coursenameField = new TextField();
    private TextField creditField = new TextField();
    private ChoiceBox<String> teacherField = new ChoiceBox();
    private TextField finishField = new TextField();
    private TextField capacityField = new TextField();
    private ChoiceBox<String> examformatField = new ChoiceBox<>();
    private TextField examyearField = new TextField();
    private TextField exammonthField = new TextField();
    private TextField examdateField = new TextField();
    private ChoiceBox<String> examtimeField = new ChoiceBox();
    private ChoiceBox<String> classroomField = new ChoiceBox();
    private ChoiceBox<String> deptField = new ChoiceBox();
    private ChoiceBox<Integer> semesterField = new ChoiceBox();
    private ChoiceBox<Integer> yearField = new ChoiceBox();
    //dayList dayList = new dayList();

    private ChoiceBox TimeDay1 = new dayList().dayList();
    private ChoiceBox TimePeriod1 = new periodList().periodList();
    private ChoiceBox TimeDay2 = new dayList().dayList();
    private ChoiceBox TimePeriod2 = new periodList().periodList();
    private ChoiceBox TimeDay3 = new dayList().dayList();
    private ChoiceBox TimePeriod3 = new periodList().periodList();
    private ChoiceBox TimeDay4 = new dayList().dayList();
    private ChoiceBox TimePeriod4 = new periodList().periodList();

    private GridPane grid = new GridPane();
    private Button addTime = new Button("Add");
    private GridPane timegrid = new GridPane();
    private ScrollPane scrollPane = new ScrollPane();
    private HBox timebox = new HBox();
    private Button confirm = new Button("Confirm");
    private HBox bottom = new HBox();
    private VBox inside = new VBox();

    Connection conn = null;
    Statement stmt = null;

    ArrayList<String> teacheridlist = new ArrayList<>();
    ArrayList<String> teacherlist = new ArrayList<>();
    ArrayList<String> roomlist = new ArrayList<>();
    ArrayList<String> deptidlist = new ArrayList<>();
    ArrayList<String> deptlist = new ArrayList<>();

    public addCourse() throws SQLException {
    }

    public VBox getWindow() throws SQLException {
        ResultSet teacherrs =null;
        ResultSet roomrs = null;
        ResultSet deptrs = null;
        teacheridlist.clear();
        teacherlist.clear();
        roomlist.clear();
        deptidlist.clear();
        deptlist.clear();

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(15);
        grid.setVgap(10);
        grid.setHgap(10);
        timegrid.setVgap(10);
        timegrid.setHgap(10);
        grid.getChildren().clear();
        timegrid.getChildren().clear();
        timebox.getChildren().clear();
        inside.getChildren().clear();
        bottom.getChildren().clear();

        grid.add(courseid,0,0);
        grid.add(coursename,0,1 );
        grid.add(creditlabel,0,2);
        grid.add(teacher,0,3);
        grid.add(finishweeklabel,0,4);
        grid.add(capacitylabel,0,5);
        grid.add(examformat,0,6);
        grid.add(examdate,0,7);
        grid.add(examtime,0,8);
        grid.add(classroomlabel,0,9);
        grid.add(deptlabel,0,10);
        grid.add(semesterlabel,0,11);
        grid.add(yearlabel,0,12);
        grid.add(classtime,0,13);

        teacherField.setPrefWidth(150);
        exammonthField.setPrefWidth(50);
        examtimeField.setPrefWidth(150);
        examdateField.setPrefWidth(50);
        examyearField.setPrefWidth(50);
        classroomField.setPrefWidth(150);
        deptField.setPrefWidth(150);
        semesterField.setPrefWidth(150);
        yearField.setPrefWidth(150);
        timebox.getChildren().addAll(examyearField, exammonthField, examdateField);
        examyearField.setPromptText("Year");
        exammonthField.setPromptText("Month");
        examdateField.setPromptText("Date");
        timebox.setSpacing(10);
        grid.add(courseidField,1,0);
        grid.add(coursenameField,1,1);
        grid.add(creditField,1,2);
        grid.add(teacherField,1,3);
        grid.add(finishField,1,4);
        grid.add(capacityField,1,5);
        grid.add(examformatField,1,6);;
        grid.add(timebox,1,7);
        grid.add(examtimeField,1,8);
        grid.add(classroomField,1,9);
        grid.add(deptField,1,10);
        grid.add(semesterField,1,11);
        grid.add(yearField,1,12);

        timegrid.add(TimeDay1,0,0);
        timegrid.add(TimeDay2,0,1);
        timegrid.add(TimeDay3,0,2);
        timegrid.add(TimeDay4,0,3);
        timegrid.add(TimePeriod1,1,0);
        timegrid.add(TimePeriod2,1,1);
        timegrid.add(TimePeriod3,1,2);
        timegrid.add(TimePeriod4,1,3);
        grid.add(timegrid,1,13);
        grid.add(addTime,2,13);

        try{
            if (conn == null) {
                conn=ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt = conn.createStatement();

        try{
            teacherrs = stmt.executeQuery("Select t_id, t_name from teacher");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(teacherrs.next()){
            teacheridlist.add(teacherrs.getString(1));
            teacherlist.add(teacherrs.getString(2));
        }

        try{
            deptrs=stmt.executeQuery("Select dept_id, dept_name from department");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(deptrs.next()){
            deptidlist.add(deptrs.getString(1));
            deptlist.add(deptrs.getString(2));
        }

        try {
            roomrs = stmt.executeQuery("Select place from classroom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(roomrs.next()){
            roomlist.add(roomrs.getString(1));
        }
        for (String x :
                deptlist) {
            deptField.getItems().add(x);
        }

        for (String x:
             teacherlist) {
            teacherField.getItems().add(x);
        }

        for (String x :
                roomlist) {
            classroomField.getItems().add(x);
        }

        semesterField.getItems().add(1);
        semesterField.getItems().add(2);

        yearField.getItems().add(1);
        yearField.getItems().add(2);
        yearField.getItems().add(3);
        yearField.getItems().add(4);

        examtimeField.getItems().add("08:00 - 10:00");
        examtimeField.getItems().add("13:30 - 15:30");
        examtimeField.getItems().add("16:00 - 18:00");

        examformatField.getItems().add("Open");
        examformatField.getItems().add("Closed");

        confirm.setOnAction(e->{
            System.out.println(examtimeField.getSelectionModel().getSelectedItem());
            System.out.println(classroomField.getSelectionModel().getSelectedIndex());
        });

        scrollPane.setContent(inside);
        inside.getChildren().addAll(grid, bottom);
        inside.setSpacing(20);
        inside.setPadding(new Insets(10));
        bottom.getChildren().add(confirm);
        bottom.setAlignment(Pos.CENTER);
        vBox.getChildren().add(scrollPane);
        return vBox;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    public void confirmAction() throws SQLException {
        int errorcheckbit = 0;
        String examtime = null;
        ResultSet check = stmt.executeQuery("Select count(*) from course where c_id = '"+courseidField.getText()+"'");
        int checkbit = 0;
        while(check.next()){
            checkbit++;
        }
        if(checkbit==0){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("There is already a course with the same code.");
            alert1.showAndWait();
            errorcheckbit++;
        }

        if(errorcheckbit==0) {
            String insertsql = "Insert into course " +
                    "(c_id, c_name, c_credit, c_t_id, c_period_start, c_period_end, c_number, c_capacity, c_exam_form, c_exam_time, c_place, c_dept_id, c_semester, c_year) values" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            examtime = examyearField.getText() + "/" + exammonthField.getText() + "/" + examdateField.getText() + " " + examtimeField.getSelectionModel().getSelectedItem();
            PreparedStatement preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setString(1, courseidField.getText());
            preparedStatement.setString(2, coursenameField.getText());
            preparedStatement.setInt(3, Integer.parseInt(creditField.getText()));
            preparedStatement.setString(4, teacheridlist.get(teacherField.getSelectionModel().getSelectedIndex()));
            preparedStatement.setInt(5, 1);
            preparedStatement.setInt(6, Integer.parseInt(finishField.getText()));
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, Integer.parseInt(capacityField.getText()));
            preparedStatement.setString(9, examformatField.getSelectionModel().getSelectedItem());
            preparedStatement.setString(10, examtime);
            preparedStatement.setString(11, classroomField.getSelectionModel().getSelectedItem());
            preparedStatement.setString(12, deptidlist.get(deptField.getSelectionModel().getSelectedIndex()));
            preparedStatement.setInt(13, semesterField.getSelectionModel().getSelectedItem());
            preparedStatement.setInt(14, yearField.getSelectionModel().getSelectedItem());

            preparedStatement.execute();

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Course added");
            alert2.showAndWait();
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------------------

}
