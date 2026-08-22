/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package speco.toppgsql;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.ModelPg;
import speco.toppgsql.om.PgActivityFull;

/**
 * FXML Controller class
 *
 * @author adrian
 */
public class FXMLPgActivityFullController implements Initializable {

    @FXML
    private VBox Vbox;
    @FXML
    private TextField client_addr;
    @FXML
    private TextField client_hostname;
    @FXML
    private TextField client_port;
    @FXML
    private TextField usename;
    @FXML
    private TextField Pid;
    @FXML
    private TextField application_name;
    @FXML
    private TextField query_start;
    @FXML
    private TextField wait_event_type;
    @FXML
    private TextField wait_event;
    @FXML
    private TextField state;
    @FXML
    private TextArea query;
    @FXML
    private TextArea explain;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void showActivity(Integer pid, String base, ModelPg modelPg) {
        try {
            Tx tx = new Tx(base);
            PgActivityFull activityFull = modelPg.getAllActivitiesFull(tx, pid);
            if (activityFull != null && activityFull.getPid() != null) {
                client_addr.setText(activityFull.getClient_addr());
                client_hostname.setText(activityFull.getClient_hostname());
                client_port.setText(String.valueOf(activityFull.getClient_port()));
                usename.setText(activityFull.getUsename());
                Pid.setText(String.valueOf(activityFull.getPid()));
                application_name.setText(activityFull.getApplication_name());
                query_start.setText(String.valueOf(activityFull.getQuery_start()));
                wait_event_type.setText(activityFull.getWait_event_type());
                wait_event.setText(activityFull.getWait_event());
                state.setText(activityFull.getState());
                query.setWrapText(true);
                query.setText(activityFull.getQuery());
                if (activityFull.getQuery() != null && !activityFull.getQuery().equals("")) {
                    explain.setText(String.valueOf(modelPg.getExplain(tx, activityFull.getQuery())));
                }
            }
        } catch (Exception ex) {
            Log.error(ex);
        }
    }
}
