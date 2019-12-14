package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TeacherMain {
    private String teacher_id;

    public void showWindow(String id) throws SQLException {
        this.teacher_id = id;
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        VBox menu = new VBox();
        BorderPane homescreen = new BorderPane();
        VBox borderbox =new VBox();
        borderbox.setPadding(new Insets(15));

        Hyperlink hlink1 = new Hyperlink("Courses"); hlink1.setFont(Font.font("Arial", 14));
        Hyperlink hlink2 = new Hyperlink("Student list"); hlink2.setFont(Font.font("Arial", 14));
        Hyperlink hlink3 = new Hyperlink("Application"); hlink3.setFont(Font.font("Arial", 14));
        Hyperlink hlink4 = new Hyperlink("Score"); hlink4.setFont(Font.font("Arial", 14));

        menu.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menu.setSpacing(10);
        menu.setPadding(new Insets(15));
        menu.getChildren().addAll(hlink1,hlink2,hlink3,hlink4);
        borderPane.setLeft(menu);

        teacherclasslist teacherclasslist = new teacherclasslist(teacher_id);
        borderPane.setCenter(teacherclasslist.showWindow());
//--------------------------------------------------------------------------------------------------------------------------------------------
        hlink1.setOnAction(e->{
            try {
                borderPane.setCenter(teacherclasslist.showWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        studentlist studentlist = new studentlist(teacher_id);
        hlink2.setOnAction(e->{
            try{
                borderPane.setCenter(studentlist.showWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        coursefullteacher coursefullteacher = new coursefullteacher(teacher_id);
        hlink3.setOnAction(e->{
            try {
                borderPane.setCenter(coursefullteacher.showWindow());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        setscore setscore = new setscore(teacher_id);
        hlink4.setOnAction(e->{
            try{
                borderPane.setCenter(setscore.scorelist());
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
