package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class studentinfo {

    private String stu_id;
    private ResultSet rs=null;
    private ArrayList<Integer> courselist = new ArrayList<>();
    private ArrayList<String> coursecode = new ArrayList<>();
    private ArrayList<String> coursename = new ArrayList<>();


    private ResultSet rs2 = null;

    public studentinfo(String id){
        this.stu_id = id;
    }
    public VBox hometable() throws SQLException {
        int x = 16;
        int y;
        int dayvalue , periodvalue;

        //connect
        Connection conn = null;
        coursecode.clear();
        courselist.clear();
        coursename.clear();
        try{
            conn = ConnectionConstruct.getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }

        Statement stmt = null;

        //rs = stmt.executeQuery("Select t.ctime_t_id FROM student s, selectedcourses c, classtime t WHERE (s.s_id = c.s_id) AND (c.c_id = t.ctime_c_id) AND s.s_id = 's001'");
        try{
            stmt=conn.createStatement();
            //String sqlstmt = "Select t.ctime_t_id FROM student s, selectedcourses c, classtime t WHERE (s.s_id = c.s_id) AND (c.c_id = t.ctime_c_id) AND s.s_id = 's001'";
            rs = stmt.executeQuery("Select t.ctime_t_id, c.c_id FROM student s, selectedcourses c, classtime t WHERE (s.s_id = c.s_id) AND (c.c_id = t.ctime_c_id) AND s.s_id = '"+ stu_id+"'");


            while(rs.next()){
                courselist.add(Integer.parseInt(rs.getString(1)));
                coursecode.add(rs.getString(2));
            }
            //System.out.println(coursecode);
            rs.beforeFirst();

            for (String xz :
                    coursecode) {
                rs2= stmt.executeQuery("SELECT c_name from course where c_id = '" + xz +"'");
                rs2.next();
                coursename.add(rs2.getString(1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            conn.close();
        }

        //initialize rows
        periodxday p1 = new periodxday();
        periodxday p2 = new periodxday();
        periodxday p3 = new periodxday();
        periodxday p4 = new periodxday();
        periodxday p5 = new periodxday();
        periodxday p6 = new periodxday();
        periodxday p7 = new periodxday();
        periodxday p8 = new periodxday();
        periodxday p9 = new periodxday();
        periodxday p10 = new periodxday();
        periodxday p11 = new periodxday();
        periodxday p12 = new periodxday();
        periodxday p13 = new periodxday();
        periodxday p14 = new periodxday();
        //periodxday p15 = new periodxday();

        //select row
        for (int i = 0; i<courselist.size(); i++){

            y=courselist.get(i)-1;
            dayvalue = y/14;
            periodvalue = y % 14;
            //System.out.println(y + " " + dayvalue + " " + periodvalue);
            switch (periodvalue){
                case 0: p1.setDay(dayvalue, coursename.get(i));
                    break;
                case 1: p2.setDay(dayvalue,coursename.get(i));
                    break;
                case 2:p3.setDay(dayvalue,coursename.get(i));
                    break;
                case 3:p4.setDay(dayvalue,coursename.get(i));
                    break;
                case 4:p5.setDay(dayvalue,coursename.get(i));
                    break;
                case 5:p6.setDay(dayvalue,coursename.get(i));
                    break;
                case 6:p7.setDay(dayvalue,coursename.get(i));
                    break;
                case 7:p8.setDay(dayvalue,coursename.get(i));
                    break;
                case 8:p9.setDay(dayvalue,coursename.get(i));
                    break;
                case 9:p10.setDay(dayvalue,coursename.get(i));
                    break;
                case 10:p11.setDay(dayvalue,coursename.get(i));
                    break;
                case 11:p12.setDay(dayvalue,coursename.get(i));
                    break;
                case 12:p13.setDay(dayvalue,coursename.get(i));
                    break;
                case 13:p14.setDay(dayvalue,coursename.get(i));
                    break;
                /**case 14:p15.setDay(dayvalue,coursename.get(i));
                 break;**/

            }
        }

        //initialize table
        TableView<periodxday> table = new TableView<periodxday>();
        final ObservableList<periodxday> data =
                FXCollections.observableArrayList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14);

        //insert columns and load data
        TableColumn mondayCol = new TableColumn("Mon");
        mondayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("monday")
        );

        TableColumn tuesdayCol = new TableColumn("Tues");
        tuesdayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("tuesday")
        );

        TableColumn wednesdayCol = new TableColumn("Wed");
        wednesdayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("wednesday")
        );

        TableColumn thursdayCol = new TableColumn("Thurs");
        thursdayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("thursday")
        );

        TableColumn fridayCol = new TableColumn("Fri");
        fridayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("friday")
        );

        TableColumn saturdayCol = new TableColumn("Sat");
        saturdayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("saturday")
        );

        TableColumn sundayCol = new TableColumn("Sun");
        sundayCol.setCellValueFactory(
                new PropertyValueFactory<periodxday,String>("sunday")
        );

        table.setItems(data);
        table.getColumns().addAll(mondayCol,tuesdayCol,wednesdayCol,thursdayCol,fridayCol,saturdayCol,sundayCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(30);
        table.setPrefSize(600,450);
        table.setEditable(false);


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.getChildren().add(table);
        return vbox;

    }

    public static class periodxday{

        SimpleStringProperty monday;
        SimpleStringProperty tuesday;
        SimpleStringProperty wednesday;
        SimpleStringProperty thursday;
        SimpleStringProperty friday;
        SimpleStringProperty saturday;
        SimpleStringProperty sunday;

        private periodxday(){
            this.monday = new SimpleStringProperty("");
            this.tuesday=  new SimpleStringProperty("");
            this.wednesday= new SimpleStringProperty("");
            this.thursday= new SimpleStringProperty("");
            this.friday= new SimpleStringProperty("");
            this.saturday= new SimpleStringProperty("");
            this.sunday= new SimpleStringProperty("");
        }

        public void setDay(int x, String course){
            switch (x){
                case 1:this.monday= new SimpleStringProperty(course);break;
                case 2:this.tuesday= new SimpleStringProperty(course);break;
                case 3:this.wednesday= new SimpleStringProperty(course);break;
                case 4:this.thursday= new SimpleStringProperty(course);break;
                case 5:this.friday= new SimpleStringProperty(course);break;
                case 6:this.saturday= new SimpleStringProperty(course);break;
                case 0:this.sunday= new SimpleStringProperty(course);break;
            }
        }

        public String getMonday() {
            return monday.get();
        }

        public SimpleStringProperty mondayProperty() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday.set(monday);
        }

        public String getTuesday() {
            return tuesday.get();
        }

        public SimpleStringProperty tuesdayProperty() {
            return tuesday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday.set(tuesday);
        }

        public String getWednesday() {
            return wednesday.get();
        }

        public SimpleStringProperty wednesdayProperty() {
            return wednesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday.set(wednesday);
        }

        public String getThursday() {
            return thursday.get();
        }

        public SimpleStringProperty thursdayProperty() {
            return thursday;
        }

        public void setThursday(String thursday) {
            this.thursday.set(thursday);
        }

        public String getFriday() {
            return friday.get();
        }

        public SimpleStringProperty fridayProperty() {
            return friday;
        }

        public void setFriday(String friday) {
            this.friday.set(friday);
        }

        public String getSaturday() {
            return saturday.get();
        }

        public SimpleStringProperty saturdayProperty() {
            return saturday;
        }

        public void setSaturday(String saturday) {
            this.saturday.set(saturday);
        }

        public String getSunday() {
            return sunday.get();
        }

        public SimpleStringProperty sundayProperty() {
            return sunday;
        }

        public void setSunday(String sunday) {
            this.sunday.set(sunday);
        }

    }
    public ArrayList<Integer> getCourselist() {
        return courselist;
    }

    public void setCourselist(ArrayList<Integer> courselist) {
        this.courselist = courselist;
    }

    public ArrayList<String> getCoursecode() {
        return coursecode;
    }

    public void setCoursecode(ArrayList<String> coursecode) {
        this.coursecode = coursecode;
    }

    public ArrayList<String> getCoursename() {
        return coursename;
    }

    public void setCoursename(ArrayList<String> coursename) {
        this.coursename = coursename;
    }

}
