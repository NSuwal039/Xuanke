package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class addStudent {
    private Label ID = new Label("Student id: ");
    private Label Name = new Label("Student name: ");
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

        grid.add(new Label("Enter student information:"),0,0);
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
        ResultSet checkrs = null;
        int errorbit=0;
        ArrayList<String> list = new ArrayList<>();

        try {
            checkrs = stmt.executeQuery("Select s_id from student");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (checkrs.next()){
            list.add(checkrs.getString(1));
        }

        for (String x :
                list) {
            if (namefield.getText() == x) {
                    errorbit++;
                }
            }

        if (errorbit==0){
            String sql = "Insert into student (s_id, s_name, s_password, s_gender, s_dept_id, s_gpa) values" +
                    "(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, idfield.getText());
            preparedStatement.setString(2, namefield.getText());
            preparedStatement.setString(3, passfield.getText());
            preparedStatement.setString(4, genderfield.getSelectionModel().getSelectedItem());
            preparedStatement.setString(5, deptname.get(deptfield.getSelectionModel().getSelectedIndex()));
            preparedStatement.setInt(6, 0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Student record added.");
            alert.showAndWait();
            idfield.clear();
            namefield.clear();
            passfield.clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Student code" + idfield.getText()+"already exists.");
            alert.showAndWait();
        }

    }
}
