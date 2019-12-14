package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class addTeacher {
    private Label ID = new Label("Professor id: ");
    private Label Name = new Label("Professor name: ");
    private Label Password = new Label("Password: ");
    private Label Department = new Label("Department: ");
    private Label Gender = new Label("Gender: ");
    private TextField idfield = new TextField();
    private  TextField namefield = new TextField();
    private PasswordField passfield = new PasswordField();
    private ChoiceBox<String> deptfield = new ChoiceBox<>();
    private ChoiceBox<String> genderfield = new ChoiceBox<>();
    private Button confirm = new Button("Confirm");
    ArrayList<String> deptid = new ArrayList<>();
    ArrayList<String> deptname = new ArrayList<>();

    Connection conn = null;
    Statement stmt = null;

    public VBox showWindow() throws SQLException {
        VBox vBox = new VBox();
        GridPane grid = new GridPane();
        HBox hBox = new HBox();

        ResultSet deptrs = null;
        deptid.clear();
        deptname.clear();

        try{
            if (conn == null) {
                conn=ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stmt =conn.createStatement();

        try{
            deptrs=stmt.executeQuery("select dept_id, dept_name from department");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (deptrs.next()){
            deptid.add(deptrs.getString(1));
            deptname.add(deptrs.getString(2));
        }

        for (String x :
                deptname) {
            deptfield.getItems().add(x);
        }
        deptfield.setValue("");

        genderfield.getItems().add("M");
        genderfield.getItems().add("F");

        grid.add(new Label("Enter professor information:"),0,0);
        grid.add(ID,1, 0);
        grid.add(Name,1,1);
        grid.add(Password,1,2);
        grid.add(Department,1,3);
        grid.add(Gender,1,4);
        grid.add(idfield,2,0);
        grid.add(namefield,2,1);
        grid.add(passfield,2,2);
        grid.add(deptfield,2,3);
        grid.add(genderfield,2,4);
        hBox.getChildren().add(confirm);
        hBox.setAlignment(Pos.CENTER);

        confirm.setOnAction(e->{
            try {
                confirmAction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        vBox.getChildren().addAll(grid, hBox);
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(10);
        grid.setVgap(10);
        grid.setHgap(10);

        return vBox;
    }

    private void confirmAction() throws SQLException {
        ResultSet teacherrs = null;
        int errorbit = 0;
        ArrayList<String> idlist = new ArrayList<>();

        try{
            teacherrs =stmt.executeQuery("select t_id from teacher");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (teacherrs.next()){
            idlist.add(teacherrs.getString(1));
        }

        for (String x :
                idlist) {
            if(idfield.getText()==x){
                errorbit++;
            }
        }

        if(errorbit==0){
            String sql = "Insert into teacher (t_id, t_name, t_password, t_dept_id, gender) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, idfield.getText());
            preparedStatement.setString(2, namefield.getText());
            preparedStatement.setString(3, passfield.getAccessibleHelp());
            preparedStatement.setString(4, deptid.get(deptfield.getSelectionModel().getSelectedIndex()));
            preparedStatement.setString(5, genderfield.getSelectionModel().getSelectedItem());

            preparedStatement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Professor record added.");
            alert.showAndWait();
            idfield.clear();
            namefield.clear();
            passfield.clear();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Teacher with id " + idfield + " already exists.");
            alert.showAndWait();
        }
    }
}
