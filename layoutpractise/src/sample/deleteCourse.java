package sample;


import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.*;

public class deleteCourse {
    private String admin_id;
    Connection conn= null;
    Statement stmt = null;

    private Label courseID = new Label("Course ID:");
    private TextField idfield = new TextField();
    private Button button = new Button("Delete");

    public VBox showWindow() throws SQLException {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(10);
        GridPane grid = new GridPane();

        grid.add(courseID, 0 ,0);
        grid.add(idfield,0,1);
        grid.add(button,1,1);
        grid.setHgap(10);

        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stmt = conn.createStatement();

        button.setOnAction(e->{
            try {
                deleteAction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        vBox.getChildren().add(grid);
        return vBox;
    }

    private void deleteAction() throws SQLException {
        ResultSet rs = stmt.executeQuery("select count(*) from course where c_id ='"+idfield.getText()+"'");
        int count = 0;

        while(rs.next()){
            count = Integer.parseInt(rs.getString(1));
        }

        if(count==1){
            String sql = "Delete from course where c_id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, idfield.getText());

            preparedStatement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Course deleted");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Course not found");
            alert.showAndWait();
        }
    }
}
