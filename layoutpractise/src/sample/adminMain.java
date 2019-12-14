package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class adminMain {
    private String admin_id;

    public void showWindow(String id) throws SQLException {
        this.admin_id=id;
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        HBox horizon = new HBox();
        VBox menu = new VBox();
        VBox borderbox =new VBox();
        borderbox.setPadding(new Insets(15));

        Hyperlink hlink1 = new Hyperlink("Add Course"); hlink1.setFont(Font.font("Arial", 14));
        Hyperlink hlink2 = new Hyperlink("Add Professor"); hlink2.setFont(Font.font("Arial", 14));
        Hyperlink hlink3 = new Hyperlink("Add Classroom"); hlink3.setFont(Font.font("Arial", 14));
        Hyperlink hlink4 = new Hyperlink("Add Student"); hlink4.setFont(Font.font("Arial", 14));
        Hyperlink hlink5 = new Hyperlink("Add Department"); hlink5.setFont(Font.font("Arial", 14));
        Hyperlink hlink6 = new Hyperlink("Delete Course"); hlink6.setFont(Font.font("Arial", 14));

        menu.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menu.setSpacing(10);
        menu.setPadding(new Insets(15));
        menu.getChildren().addAll(hlink1,hlink2,hlink3,hlink4,hlink5,hlink6);
        borderPane.setTop(horizon);
        borderPane.setLeft(menu);
//--------------------------------------------------------------------------------------------------------------------------------------------
        addCourse addCourse = new addCourse();
        borderPane.setCenter(addCourse.getWindow());
        hlink1.setOnAction(e->{
            try {
                borderPane.setCenter(addCourse.getWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        addTeacher addTeacher = new addTeacher();
        hlink2.setOnAction(e->{
            try {
                borderPane.setCenter(addTeacher.showWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        addClassroom addClassroom = new addClassroom();
        hlink3.setOnAction(e->{
            try {
                borderPane.setCenter(addClassroom.showWindow());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        addStudent addStudent = new addStudent();
        hlink4.setOnAction(e->{
            try{
                borderPane.setCenter(addStudent.showWindow());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        addDept addDept = new addDept();
        hlink5.setOnAction(e->{
            try{
                borderPane.setCenter(addDept.showWindow());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        deleteCourse deleteCourse = new deleteCourse();
        hlink6.setOnAction(e->{
            try {
                borderPane.setCenter(deleteCourse.showWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

//--------------------------------------------------------------------------------------------------------------------------------------------
        borderbox.getChildren().add(borderPane);
        Scene scene = new Scene(borderbox);
        stage.setScene(scene);
        stage.show();
    }

}
