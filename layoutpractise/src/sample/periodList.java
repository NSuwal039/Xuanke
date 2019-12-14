package sample;

import javafx.scene.control.ChoiceBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class periodList extends ChoiceBox {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ArrayList<String> list = new ArrayList<>();

    public ChoiceBox periodList() throws SQLException {
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
            rs=stmt.executeQuery("Select period_start, period_end, period_id from period");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(rs.next()){
            list.add(rs.getString(1) + " - " + rs.getString(2) + " ("+rs.getString(3)+")");
        }

        for (String x :
                list) {
            choiceBox.getItems().add(x);
        }
        choiceBox.setValue("");


        return choiceBox;
    }
}
