package sample;

import javafx.scene.control.ChoiceBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class dayList extends ChoiceBox {
    Connection conn =null;
    Statement stmt = null;
    ResultSet dayrs = null;
    ArrayList<String> list = new ArrayList<>();

    public ChoiceBox dayList() throws SQLException {
        ChoiceBox choiceBox = new ChoiceBox();
        try{
            if (conn == null) {
                conn=ConnectionConstruct.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt=conn.createStatement();

        try{
            dayrs=stmt.executeQuery("Select day_name from days");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while(dayrs.next()){
            list.add(dayrs.getString(1));
        }

        for (String x :
                list) {
            choiceBox.getItems().add(x);
        }
        choiceBox.setValue("");

        return choiceBox;
    }
}
