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
    
  
    @FXML
    private LineChart<String, Number> lineChart;
    
    @FXML
    private CategoryAxis xAxis;
  
    /*@FXML
    private NumberAxis yAxis;*/

    private XYChart.Series<String, Number> series;
    private XYChart.Series<String, Number> series2;
    private Timeline timeline;
    private final int MAX_DATA_POINTS = 15; // Número de muestras visibles en pantalla
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");  
    private List<PgActivity> pgActivitys;
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
        series = new XYChart.Series<>();
        series2 = new XYChart.Series<>();
        series.setName("Postgres CPU Usage");
        series2.setName("Postgres IO Usage");
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        //yAxis.setTickUnit(20.0);

        // 2. Configurar el temporizador (ej. actualizar cada 1 segundo)
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            actualizarGrafico();
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
                pgActivitys = modelPg.getAllActivities(tx);
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
    
    private void actualizarGrafico() {
        // Obtener el valor actual (aquí harías tu SELECT a pg_stat_activity / pg_stat_kcache)
         
        Long nuevoValorCpu = obtenerMetricaCpu();
        Long nuevoValorIO = obtenerMetricaIo();
        String horaActual = LocalTime.now().format(formatter);

        // Agregar el nuevo punto a la serie
        series.getData().add(new XYChart.Data<>(horaActual, nuevoValorCpu));
        series2.getData().add(new XYChart.Data<>(horaActual, nuevoValorIO));

        // Eliminar puntos antiguos si superamos el límite visual (Ventana deslizante)
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0);
            series2.getData().remove(0);
        }
    }
    private Long obtenerMetricaCpu() {
        Long totalCpu=0L;
        for (PgActivity s: pgActivitys){
            totalCpu += s.getCpu();
        }
        return totalCpu;
    }
    private Long obtenerMetricaIo() {
        Long totalIo=0L;
        for (PgActivity s: pgActivitys){
            totalIo += (s.getioread() + s.getiowrite())/1024L;
        }
        return totalIo;
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
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
