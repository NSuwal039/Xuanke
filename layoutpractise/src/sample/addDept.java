package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;

public class addDept {
    private Label dept_id = new Label("Department id: ");
    private Label dept_name = new Label("Department name: ");
    private TextField idfield = new TextField();
    private  TextField namefield = new TextField();
    private Button confirm = new Button("Confirm");

    Connection conn = null;
    Statement stmt = null;

    public VBox showWindow(){
        VBox vBox = new VBox();
        GridPane grid = new GridPane();
        HBox hBox = new HBox();

        grid.add(new Label("Enter department information:"),0,0);
        grid.add(dept_id,1, 0);
        grid.add(dept_name,1,1);
        grid.add(idfield,2,0);
        grid.add(namefield,2,1);
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

    public void confirmAction() throws SQLException {
        ResultSet checkrs = null;
        ArrayList<String> idlist= new ArrayList<>();
        ArrayList<String> namelist= new ArrayList<>();
        int iderror=0; int nameerror=0;
        int checkerrorbit =0;

        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt=conn.createStatement();

        try{
            checkrs = stmt.executeQuery("Select dept_id, dept_name from department");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(checkrs.next()){
            idlist.add(checkrs.getString(1));
            namelist.add(checkrs.getString(2));
        }

        for (String x :
                idlist) {
            if (idfield.getText() == x){
                iderror++;
            }
        }

        for (String x :
                namelist) {
            if(namefield.getText() == x){
                nameerror++;
            }
        }

        checkerrorbit=iderror+nameerror;

        if(checkerrorbit==0){
            String sql = "Insert into department(dept_id, dept_name) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, idfield.getText());
            preparedStatement.setString(2, namefield.getText());

            preparedStatement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Department added");
            alert.showAndWait();

            idfield.clear();
            namefield.clear();
        }else if (iderror>0){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("There is already a department with the same department code.");
            alert1.showAndWait();
        }else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("There is already a department with the same department name.");
            alert2.showAndWait();
        }
    }
}
