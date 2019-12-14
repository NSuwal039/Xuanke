package sample;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class xuanke {

    private String stu_id;
    Label course_code = new Label("Enter course code: ");
    //Label course_name =  new Label("Enter course name: ");
    TextField code_input = new TextField();
    TextField course_input = new TextField();
    private ArrayList <String> courseidlist = new ArrayList<>();
    private ArrayList <String> coursenamelist = new ArrayList<>();
    private ArrayList <String> teacherlist = new ArrayList<>();
    private ArrayList <String> venuelist = new ArrayList<>();
    private ArrayList <Integer> numberlist = new ArrayList<>();
    private ArrayList <Integer> capacitylist = new ArrayList<>();
    private ArrayList <String> originalcourseid = new ArrayList<>();
    private ArrayList <Integer> classtime = new ArrayList<>();
    private ArrayList <ArrayList<Integer>> timelist= new ArrayList<>();
    Connection conn = null;
    Statement stmt = null;

    public xuanke(String id){
        this.stu_id=id;
    }

    public VBox xuankeLayout() throws SQLException {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(25));
        HBox xuankedialog = new HBox();
        studentinfo studentinfo = new studentinfo(stu_id);
        originalcourseid = studentinfo.getCoursecode();
        Button search = new Button("Search");
        xuankedialog.getChildren().addAll(xuankePane(), search);
        xuankedialog.setAlignment(Pos.CENTER_RIGHT);
        course_input.setEditable(false);
        course_input.setDisable(true);
        search.setOnAction(e->{
            //searchresult searchresult = new searchresult();
            //searchresult.displayresult();
            try {
                searchaction(code_input.getText());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(xuankedialog,studentinfo.hometable());
        return vbox;
    }
    public GridPane xuankePane(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setPadding(new Insets(20));

        grid.add(course_code,1,0);
        //grid.add(course_name, 1,1);
        grid.add(code_input, 2, 0);
        grid.add(course_input, 2,1);

        return grid;
    }

    private void searchaction(String coursename1) throws SQLException {

        //ResultSet rsbyname = null;
        ResultSet rsbycode = null;
        courseidlist.clear();
        coursenamelist.clear();
        teacherlist.clear();
        venuelist.clear();
        numberlist.clear();
        capacitylist.clear();
        timelist.clear();
        classtime.clear();

        try{
            if (conn==null){
            conn= ConnectionConstruct.getConnection();}
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            stmt = conn.createStatement();
            rsbycode = stmt.executeQuery("select c.c_id, c.c_name, t.t_name, c.c_place, c.c_number, c.c_capacity from course c, teacher t where c.c_id = '" +coursename1 +"' and t.t_id =c.c_t_id");
            while (rsbycode.next()){
                courseidlist.add(rsbycode.getString(1));
                coursenamelist.add(rsbycode.getString(2));
                teacherlist.add(rsbycode.getString(3));
                venuelist.add(rsbycode.getString(4));
                numberlist.add(Integer.parseInt(rsbycode.getString(5)));
                capacitylist.add(Integer.parseInt(rsbycode.getString(6)));
            }


        }catch (Exception e){e.printStackTrace();}

        try{
            ResultSet timers = stmt.executeQuery("Select ctime_t_id from classtime WHERE ctime_c_id ='"+ coursename1+"'");
            while(timers.next()){
                classtime.add(Integer.parseInt(timers.getString(1)));
                //System.out.println(classtime);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        timelist.add(classtime);


        searchresult searchresult = new searchresult();
        searchresult.displayresult();

    }

    private class searchresult{
        Label coursesfound = new Label("Courses found");
        Integer results= courseidlist.size();
        TableView resultTable = new TableView();
        ObservableList <resultdata>data = FXCollections.observableArrayList();
        TableColumn IDcol = new TableColumn("ID");
        TableColumn Name = new TableColumn("Name");
        TableColumn Teacher = new TableColumn("Teacher");
        TableColumn Classroom = new TableColumn("Classroom");
        TableColumn Capacity = new TableColumn("Capacity");
        TableColumn Time = new TableColumn("Time");


        ArrayList<resultdata> resultdataArrayList = new ArrayList();

        public void displayresult(){
            AtomicReference<Boolean> closethis = new AtomicReference<>(false);

            for(int i=0; i<results; i++){
                resultdataArrayList.add(new resultdata(courseidlist.get(i), coursenamelist.get(i), teacherlist.get(i), venuelist.get(i), numberlist.get(i), capacitylist.get(i),timelist.get(i)));
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

            Classroom.setCellValueFactory(
                    new PropertyValueFactory<resultdata,String>("classroom")
            );

            Capacity.setCellValueFactory(
                    new PropertyValueFactory<resultdata, String>("capacitydisplay")
            );

            Time.setCellValueFactory(
                    new PropertyValueFactory<resultdata,String >("time")
            );

            resultTable.setItems(data);
            resultTable.getColumns().addAll(IDcol,Name,Teacher,Classroom,Capacity,Time);
            //resultTable.setFixedCellSize(30);
            resultTable.setEditable(false);
            resultTable.setPrefSize(365, 180);

            if(resultdataArrayList.size()!=0) {

                Stage results = new Stage();
                VBox vBox = new VBox(10);
                vBox.setPadding(new Insets(10));
                HBox hBox = new HBox();
                Label resultshere = new Label("Courses found:");
                Button button = new Button("Select Course");
                hBox.setAlignment(Pos.BASELINE_RIGHT);
                hBox.getChildren().addAll(button);
                vBox.getChildren().addAll(resultshere, resultTable, hBox);
                Scene secondScene = new Scene(vBox);
                results.setScene(secondScene);
                results.show();

                button.setOnAction(e-> {
                    try {
                        if(selectCourse()){
                            results.close();
                        };
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Course not found.");
                alert.showAndWait();
            }

        }
        private boolean selectCourse() throws SQLException {
            Boolean close = false;
            resultdata  selectedCourse = (resultdata) resultTable.getSelectionModel().getSelectedItem(); //the course that the student selects from the list
            ResultSet classtime = null;
            HashSet distinctID = new HashSet(originalcourseid);     //list of class codes that the student has taken. list without repetition
            ArrayList<String> finalId = new ArrayList(distinctID);  //list of class codes that the student has taken. list without repetition
            ArrayList<Integer> timelist1 = new ArrayList();
            Integer len = finalId.size();
            System.out.println(finalId);
            ResultSet selected = null;
            ArrayList<String> selectarray = new ArrayList<>();

            try{
             if (conn == null){
                 conn = ConnectionConstruct.getConnection();
             }
            }catch (Exception e) {
                e.printStackTrace();
            }

            try{

                for(int i=0; i<len; i++){
                    classtime = stmt.executeQuery("Select ctime_t_id from classtime where ctime_c_id = '"+finalId.get(i)+"'");
                    while(classtime.next()){
                        timelist1.add(Integer.parseInt(classtime.getString(1)));        //time of all classes the student has already taken
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            System.out.println(timelist);
            System.out.println(timelist1);
            int errorbit=0;


            for (String x :
                    finalId) {
                if (x==selectedCourse.courseid.get() && errorbit==0){
                    Alert samealert = new Alert(Alert.AlertType.ERROR);
                    samealert.setContentText("You have already selected this class");
                    samealert.showAndWait();
                    errorbit = 1;
                }
            }

            if(errorbit==0) {
                if (selectedCourse.number.get() == selectedCourse.capacity.get() || selectedCourse.number.get()>selectedCourse.capacity.get()) {
                    Alert fullalert = new Alert(Alert.AlertType.ERROR);
                    fullalert.setContentText("Course is full. \nPlease apply from the shenqing tab");
                    errorbit = 1;
                    fullalert.showAndWait();

                }
            }
            if (errorbit == 0) {
                for (int x :
                        selectedCourse.timearray) {
                    for (int y :
                            timelist1) {
                        if (x == y && errorbit == 0) {
                            Alert timealert = new Alert(Alert.AlertType.ERROR);
                            timealert.setContentText("You already have another class in the same time");
                            timealert.showAndWait();
                            errorbit = 1;
                        }

                    }
                }
            }

            if(errorbit==0){
                String sql = "Insert into selectedcourses (s_id, c_id, grade,course_status) values (?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setString(1,stu_id);
                preparedStatement.setString(2,selectedCourse.courseid.get());
                preparedStatement.setString(3,"0");
                preparedStatement.setString(4,"Enroll");

                String addsql = "Update course set c_number = ? where c_id= ?";
                PreparedStatement preparedStatement1 = conn.prepareStatement(addsql);
                preparedStatement1.setInt(1, selectedCourse.number.get()+1);
                preparedStatement1.setString(2,selectedCourse.courseid.get());

                try{
                    int added =preparedStatement.executeUpdate();
                    preparedStatement1.execute();
                    System.out.println("added:" + added);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Course added");
                alert.showAndWait();
                close=true;
            }
            return close;
        }

    }

    public class resultdata{

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
