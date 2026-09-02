 /*   Copyright (C) 2026  Adrian Tabak

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo
    bajo los términos de la Licencia Pública General GNU publicada por
    la Fundación para el Software Libre, ya sea la versión 3 de la Licencia,
    o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil,
    pero SIN NINGUNA GARANTÍA; ni siquiera la garantía implícita de
    COMERCIABILIDAD o IDONEIDAD PARA UN PROPÓSITO PARTICULAR.
    Vea la Licencia Pública General GNU para más detalles.

    Debería haber recibido una copia de la Licencia Pública General GNU
    junto con este programa. En caso contrario, consulte
    <https://gnu.org>.
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.ModelPgStatActivityHistory;
import speco.toppgsql.om.PgStatActivityHistory;
import java.sql.Timestamp;
import speco.toppgsql.om.ModelPg;
import speco.toppgsql.om.PgActivity;

/**
 *
 * @author Adrian Tabak
 */
public class FXMLHistoryController implements Initializable{

    @FXML
    private TableView<PgStatActivityHistory> tableview2;
    @FXML
    private TableColumn<PgStatActivityHistory, Timestamp> ts;
    @FXML
    private TableColumn<PgStatActivityHistory, Integer> pid2;
    @FXML
    private TableColumn<PgStatActivityHistory, String> rolname2;
    @FXML
    private TableColumn<PgStatActivityHistory, String> waitEventType2;
    @FXML
    private TableColumn<PgStatActivityHistory, String> waitEvent2;
    @FXML
    private TableColumn<PgStatActivityHistory, String> state2;
    @FXML
    private TableColumn<PgStatActivityHistory, String> query2;
    @FXML
    private TableColumn<PgStatActivityHistory, Float> cpu2;
    @FXML
    private TableColumn<PgStatActivityHistory, Float> ioread;
    @FXML
    private TableColumn<PgStatActivityHistory, Float> iowrite;
    @FXML
    private TextField fechadesde;
    @FXML
    private TextField fechahasta;
    @FXML 
    private Button query;
    
    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xAxis;
    private String base;
    /*@FXML
    private NumberAxis yAxis;*/
    private XYChart.Series<String, Number> series;
    private Tx tx;
    private ModelPgStatActivityHistory modelPg;
    private ModelPg modelPg2;
    private List<PgStatActivityHistory> pgActivitys;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modelPg = new ModelPgStatActivityHistory();
        modelPg2 = new ModelPg();
        series = new XYChart.Series<>();
        series.setName("CPU Usage");
        lineChart.getData().add(series);
        xAxis.setTickLabelRotation(45);   
    }

    public void buscarActividades(String base) {
        try {
                
                tx = new Tx(base);
                pgActivitys = modelPg.retriveAllPgStatActivityHistory(tx);
                cargatabla();
                obtenerMetricaCpu();
        } catch (Exception ex) {
            Log.error(ex);
        }

    }
    
    private void cargatabla(){
                 ObservableList<PgStatActivityHistory> observableList = FXCollections.observableArrayList(pgActivitys);
                ts.setCellValueFactory(new PropertyValueFactory<>("snapshot_time"));
                pid2.setCellValueFactory(new PropertyValueFactory<>("pid"));
                query2.setCellValueFactory(new PropertyValueFactory<>("query"));
                rolname2.setCellValueFactory(new PropertyValueFactory<>("usename"));
                waitEvent2.setCellValueFactory(new PropertyValueFactory<>("wait_event"));
                waitEventType2.setCellValueFactory(new PropertyValueFactory<>("wait_event_type"));
                state2.setCellValueFactory(new PropertyValueFactory<>("state"));
                cpu2.setCellValueFactory(new PropertyValueFactory<>("cpu"));
                ioread.setCellValueFactory(new PropertyValueFactory<>("io_reads_bytes"));
                iowrite.setCellValueFactory(new PropertyValueFactory<>("io_writes_bytes"));
                
                tableview2.setRowFactory(tv -> {
                    TableRow<PgStatActivityHistory> row = new TableRow<>();

                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            mostrarActivity(row.getItem().getPid(),
                                    base,
                                    row.getItem().getUsename(),
                                    row.getItem().getWait_event(),
                                    row.getItem().getWait_event_type(),
                                    row.getItem().getQuery(),
                                    modelPg2);
                        }
                    });
                    return row;
                });
                tableview2.setItems(observableList);
    }
    private void mostrarActivity(Integer pid, String base, 
            String usename, String wait_event_type, 
            String wait_event, String query,
            ModelPg modelPg)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TopPGSQL.class.getResource("FXMLExplainHistory.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            FXMLExplainHistoryController fXMLexplainHistoryController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Activity");
            stage.show();
            fXMLexplainHistoryController.showActivity(pid, base,usename,wait_event_type, 
            wait_event, query,
             modelPg);
        } catch (IOException e) {
        }
    }
    
    public void buscarTsActividades(Timestamp tsd, Timestamp tsh, String base) {
        try {
                
                tx = new Tx(base);
                pgActivitys = modelPg.retriveTsPgStatActivityHistory(tsd,tsh,tx);
                cargatabla();
                obtenerMetricaCpu();
        } catch (Exception ex) {
            Log.error(ex);
        }

    }
 
    private Float obtenerMetricaCpu() {
        Float totalCpu=0F;
        for (int i=0; i < series.getData().size(); i++){
            series.getData().remove(0);
        }
        for (PgStatActivityHistory s: pgActivitys){
            totalCpu += s.getCpu();
            series.getData().add(new XYChart.Data<>(s.getSnapshot_time().toString(), totalCpu));
        }
        return totalCpu;
    }
    
    public void showActivity(String base){
        this.base=base;
        buscarActividades(base);
    }
    
    @FXML
    private void handlerquery(ActionEvent event){
        Timestamp tsd = Timestamp.valueOf(fechadesde.getText());
        Timestamp tsh = Timestamp.valueOf(fechahasta.getText());
        buscarTsActividades(tsd, tsh, this.base);
        
    }
    
}
