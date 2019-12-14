package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class studentMain {
    private String Stu_id;

    public void showWindow(String id) throws SQLException {
        BorderPane borderPane = new BorderPane();
        this.Stu_id=id;
        VBox menu = new VBox();
        VBox borderbox =new VBox();
        borderbox.setPadding(new Insets(15));

        BorderPane homescreen = new BorderPane();

        TableView centreTable = new TableView();
        TableColumn fname = new TableColumn("First Name");
        TableColumn lname = new TableColumn("Last Name");
        centreTable.getColumns().addAll(fname, lname);

        Hyperlink vlabel1 = new Hyperlink("Routine"); vlabel1.setFont(Font.font("Arial", 14));
        Hyperlink vlabel2 = new Hyperlink("Xuan ke");   vlabel2.setFont(Font.font("Arial", 14));
        Hyperlink vlabel3 = new Hyperlink("Shenqing");  vlabel3.setFont(Font.font("Arial", 14));
        Hyperlink vlabel4 = new Hyperlink("Tui ke");    vlabel4.setFont(Font.font("Arial", 14));
        Hyperlink vlabel5 = new Hyperlink("Chengji");   vlabel5.setFont(Font.font("Arial", 14));
        Hyperlink vlabel6 = new Hyperlink("Kaoshi");    vlabel6.setFont(Font.font("Arial", 14));

        menu.setPadding(new Insets(15));
        menu.setSpacing(10);

        /**String cssLayout = "-fx-border-color: red;\n" +
         "-fx-border-insets: 5;\n" +
         "-fx-border-width: 3;\n" +
         "-fx-border-style: dashed;\n";
         menu.setStyle(cssLayout);
         */

        menu.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        menu.setSpacing(10);
        menu.setPadding(new Insets(15));
        menu.getChildren().addAll(vlabel1,vlabel2,vlabel3,vlabel4,vlabel5,vlabel6);
        borderPane.setLeft(menu);


        studentinfo studentinfo = new studentinfo(Stu_id);
        borderPane.setCenter(studentinfo.hometable());
//--------------------------------------------------------------------------------------------------------------------------------------------
        vlabel1.setOnAction(e->{
            try {
                borderPane.setCenter(studentinfo.hometable());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        xuanke xuankepage = new xuanke(Stu_id);

        vlabel2.setOnAction(e->{
            try {
                borderPane.setCenter(xuankepage.xuankeLayout());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        coursefullstudent coursefullstudent = new coursefullstudent(Stu_id);
        vlabel3.setOnAction(e->{
            try{
                borderPane.setCenter(coursefullstudent.showWindow());}
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        tuike tuikepage = new tuike(Stu_id);
        VBox tuikelayout = new VBox();
        HBox hBox = new HBox();
        Button tui = new Button("Select");
        hBox.setPadding(new Insets(10));
        hBox.getChildren().add(tui);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        tui.setOnAction(e->{
            try {
                tuikepage.deleteCourse();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            borderPane.setCenter(tuikelayout);
        });

        tuikelayout.getChildren().addAll(tuikepage.tuikepage(),hBox);

        vlabel4.setOnAction(e->{
            borderPane.setCenter(tuikelayout);
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        chengji chengjipage = new chengji(Stu_id);
        vlabel5.setOnAction(e->{
            try {
                borderPane.setCenter(chengjipage.chengjipage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------
        kaoshi kaoshipage = new kaoshi(Stu_id);
        vlabel6.setOnAction(e->{
            try {
                borderPane.setCenter(kaoshipage.kaoshipage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

//--------------------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------------------
        Label hello = new Label("Hello user");
        homescreen.setTop(hello);
        homescreen.setAlignment(hello,Pos.BOTTOM_RIGHT);
        homescreen.setCenter(centreTable);
//--------------------------------------------------------------------------------------------------------------------------------------------
        borderbox.getChildren().add(borderPane);
        Scene scene = new Scene(borderbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
