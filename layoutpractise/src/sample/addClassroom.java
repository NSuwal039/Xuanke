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

public class addClassroom {
    private Label classroom = new Label("Classroom; ");
    private Label capacity = new Label("Capacity: ");
    private TextField classfield = new TextField();
    private  TextField capfield = new TextField();
    private Button confirm = new Button("Confirm");

    Connection conn = null;
    Statement stmt = null;

    public VBox showWindow(){
        VBox vBox = new VBox();
        GridPane grid = new GridPane();
        HBox hBox = new HBox();

        grid.add(new Label("Enter classroom information:"),0,0);
        grid.add(classroom,1, 0);
        grid.add(capacity,1,1);
        grid.add(classfield,2,0);
        grid.add(capfield,2,1);
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
        ArrayList<String> classlist= new ArrayList<>();
        int errorbit = 0;

        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt=conn.createStatement();

        try {
            checkrs=stmt.executeQuery("Select place from classroom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (checkrs.next()){
            classlist.add(checkrs.getString(1));
        }

        for (String x:
             classlist) {
            if(classfield.getText()==x){
                errorbit++;
            }
        }

        if(errorbit==0){
            String sql= "Insert into classroom (place, capacity) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, classfield.getText());
            preparedStatement.setInt(2, Integer.parseInt(capfield.getText()));

            preparedStatement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Classroom added");
            alert.showAndWait();

            classfield.clear();
            capfield.clear();
        }else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Classroom "+ classfield.getText() +" is already in the database");
            alert1.showAndWait();
        }
    }
}
