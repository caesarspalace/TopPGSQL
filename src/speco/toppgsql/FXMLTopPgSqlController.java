/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package speco.toppgsql;

import com.sun.javafx.scene.control.Properties;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.ModelPg;
import speco.toppgsql.om.PgActivity;

/**
 * FXML Controller class
 *
 * @author adrian
 */
public class FXMLTopPgSqlController implements Initializable, Runnable {

    @FXML
    private ComboBox<PgBases> baseId;
    @FXML
    private TableView<PgActivity> tableView;
    @FXML
    private TableColumn<PgActivity, Integer> pid;
    @FXML
    private TableColumn<PgActivity, String> rolname;
    @FXML
    private TableColumn<PgActivity, String> waitEventType;
    @FXML
    private TableColumn<PgActivity, String> waitEvent;
    @FXML
    private TableColumn<PgActivity, String> state;
    @FXML
    private TableColumn<PgActivity, String> query;
    @FXML
    private TableColumn<PgActivity, Long> cpu;
    @FXML
    private TableColumn<PgActivity, Long> ioread;
    @FXML
    private TableColumn<PgActivity, Long> iowrite;
    
    Thread t = null;
    private boolean flag = true;
    private Tx tx;
    private ModelPg modelPg;
    private String baseAnt;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReadProperties readProperties = new ReadProperties();
        ObservableList<PgBases> observableList = FXCollections.observableArrayList(readProperties.getBases());
        baseId.setItems(observableList);
        baseId.getSelectionModel().selectFirst();
        closeEvent();
        baseAnt = baseId.getSelectionModel().getSelectedItem().getNombre();
        tx = new Tx(baseId.getSelectionModel().getSelectedItem().getNombre());
        modelPg = new ModelPg();
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
        // TODO
    }

    private void buscarActividades() {
        try {
            PgBases pgBases = baseId.getSelectionModel().getSelectedItem();
            if (pgBases != null) {
                if (!baseAnt.equals(pgBases))
                    tx = new Tx(pgBases.getNombre());
//                      ModelPg modelPg = new ModelPg();
                List<PgActivity> pgActivitys = modelPg.getAllActivities(tx);
                ObservableList<PgActivity> observableList = FXCollections.observableArrayList(pgActivitys);
                pid.setCellValueFactory(new PropertyValueFactory<>("pid"));
                query.setCellValueFactory(new PropertyValueFactory<>("query"));
                rolname.setCellValueFactory(new PropertyValueFactory<>("rolname"));
                waitEvent.setCellValueFactory(new PropertyValueFactory<>("wait_event"));
                waitEventType.setCellValueFactory(new PropertyValueFactory<>("wait_event_type"));
                state.setCellValueFactory(new PropertyValueFactory<>("state"));
                cpu.setCellValueFactory(new PropertyValueFactory<>("cpu"));
                ioread.setCellValueFactory(new PropertyValueFactory<>("ioread"));
                iowrite.setCellValueFactory(new PropertyValueFactory<>("iowrite"));
                tableView.setRowFactory(tv -> {
                    TableRow<PgActivity> row = new TableRow<>();

                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            mostrarActivity(row.getItem().getPid(),baseId.getSelectionModel().getSelectedItem().getNombre(),modelPg);
                        }
                    });
                    return row;
                });
                if (observableList != null && observableList.size() > 0)
                    tableView.setItems(observableList);
                else 
                    tableView.setItems(null);
            }
        } catch (Exception ex) {
            Log.error(ex);
        }

    }

    @FXML
    private void handlerBaseChoice(ActionEvent event) {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        while (flag) {
            buscarActividades();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Log.error(ex);
            }
        }

    }

    private void closeEvent() {

        TopPGSQL.owner.setOnCloseRequest((WindowEvent t) -> {
            salir();
            t.consume();
        });

    }

    private void salir() {
        flag = false;
        Platform.exit();
        System.exit(0);
    }

    private void mostrarActivity(Integer pid, String base, ModelPg modelPg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TopPGSQL.class.getResource("FXMLPgActivityFull.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
           FXMLPgActivityFullController fXMLPgActivityFullController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Activity");
            stage.show();
            fXMLPgActivityFullController.showActivity(pid, base, modelPg);
        } catch (IOException e) {
        }

    }
}
