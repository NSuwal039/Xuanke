package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {
    Connection conn=null;
    Statement stmt = null;
    Label Username = new Label("ID: ");
    Label Password = new Label("Password: ");
    Label Account = new Label("Account: ");
    TextField UName = new TextField();
    PasswordField PWord = new PasswordField();
    ChoiceBox<String> AccountBox = new ChoiceBox<>();

    private Boolean close = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
       Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Button button = new Button("Login");

        AccountBox.getItems().add("Student");
        AccountBox.getItems().add("Teacher");
        AccountBox.getItems().add("Admin");
        AccountBox.setValue(AccountBox.getItems().get(0));

        GridPane grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.add(Username,0,0);
        grid.add(Password,0,1);
        grid.add(Account,0,2);
        grid.add(UName,1,0);
        grid.add(PWord,1,1);
        grid.add(AccountBox,1,2);


        button.setOnAction(e->{
            int index = AccountBox.getSelectionModel().getSelectedIndex();
            switch (index){
                case 0: try{
                    close=StudentLogin();
                    if (close){
                        primaryStage.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                    break;
                case 1:
                    try{
                        close=TeacherLogin();
                        if (close){
                            primaryStage.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        close = AdminLogin();
                        if (close){
                            primaryStage.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        });


        try{
            if (conn == null) {
                conn = ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stmt = conn.createStatement();

        vBox.getChildren().addAll(grid, hBox);
        hBox.getChildren().add(button);
        hBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(200, 50,50,50));
        vBox.setSpacing(25);
        vBox.setPrefHeight(600);
        vBox.setPrefWidth(350);
        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    private boolean AdminLogin() throws SQLException {
        Boolean value = false;
        int accountcount =0;
        ResultSet Adminrs= stmt.executeQuery("select count(*) from adminlist where admin_name='" +UName.getText() +"' and admin_password ='"+PWord.getText()+"'");

        while (Adminrs.next()){
            accountcount=Integer.parseInt(Adminrs.getString(1));
        }

        if(accountcount == 1){
            adminMain adminMain = new adminMain();
            adminMain.showWindow(UName.getText());
            value=true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Authentication failed.\nPlease check your ID or password and try again.");
            alert.showAndWait();
        }

        return value;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    private boolean TeacherLogin() throws SQLException {
        Boolean value = false;
        int accountcount =0;
        ResultSet Teacherrs= stmt.executeQuery("select count(*) from teacher where t_id='" +UName.getText() +"' and t_password ='"+PWord.getText()+"'");

        while (Teacherrs.next()){
            accountcount=Integer.parseInt(Teacherrs.getString(1));
        }

        if(accountcount == 1){
            TeacherMain teacherMain = new TeacherMain();
            teacherMain.showWindow(UName.getText());
            value=true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Authentication failed.\nPlease check your ID or password and try again.");
            alert.showAndWait();
        }

        return value;
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------
    private boolean StudentLogin() throws SQLException {
        Boolean value = false;
        int accountcount =0;
        ResultSet Studentrs= stmt.executeQuery("select count(*) from student where s_id='" +UName.getText() +"' and s_password ='"+PWord.getText()+"'");

        while (Studentrs.next()){
            accountcount=Integer.parseInt(Studentrs.getString(1));
        }

        if(accountcount == 1){
            studentMain studentMain = new studentMain();
            studentMain.showWindow(UName.getText());
            value=true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Authentication failed.\nPlease check your ID or password and try again.");
            alert.showAndWait();
        }

        return value;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        launch(args);
    }
}
