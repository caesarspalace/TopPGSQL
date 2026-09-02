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
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.ModelPg;
import speco.toppgsql.om.PgActivity;
import speco.toppgsql.om.KillSession;

/**
 *
 * @author Adrian Tabak
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
    @FXML
    private Button killSession;
    @FXML
    private Button ASH;
    @FXML
    private TextField spid;

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
        series.setName("CPU Usage");
        series2.setName("IO Usage");
        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        xAxis.setTickLabelRotation(45);
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
                if (!baseAnt.equals(pgBases)) {
                    tx = new Tx(pgBases.getNombre());
                }
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
                            mostrarActivity(row.getItem().getPid(), baseId.getSelectionModel().getSelectedItem().getNombre(), modelPg);
                        }
                    });
                    return row;
                });
                tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        spid.setText(String.valueOf(newSelection.getPid()));
                    }
                });
                if (observableList != null && observableList.size() > 0) {
                    tableView.setItems(observableList);
                } else {
                    tableView.setItems(null);
                }
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
        Long totalCpu = 0L;
        for (PgActivity s : pgActivitys) {
            totalCpu += s.getCpu();
        }
        return totalCpu;
    }

    private Long obtenerMetricaIo() {
        Long totalIo = 0L;
        for (PgActivity s : pgActivitys) {
            totalIo += (s.getioread() + s.getiowrite()) / 1024L;
        }
        return totalIo;
    }

    public void stop() {
        try {
        if (timeline != null) {
            timeline.stop();
            //t.interrupt();
        }
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    @FXML
    private void handlerBaseChoice(ActionEvent event) {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    @FXML
    void handlerKillSession(ActionEvent event) {
        KillSession ks = new KillSession();
        PgBases pgBases = baseId.getSelectionModel().getSelectedItem();
        Integer pid = 0;
        if (!spid.getText().equals("")) {
            pid = Integer.valueOf(spid.getText());
        } else {
            pid = tableView.getSelectionModel().getSelectedItem().getPid();
            spid.setText(pid.toString());
        }
        ks.callKillSession(pid, pgBases.getNombre());
    }

    @FXML
    void handlerASH(ActionEvent event){
        PgBases pgBases = baseId.getSelectionModel().getSelectedItem();
        mostrarActivityHistory(pgBases.getNombre());
    }
    
    public void run() {
        while (flag) {
            buscarActividades();
            try {
                Thread.sleep(5000);
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
    private void mostrarActivityHistory(String base) {
    try {
        // Opción A: Usar ruta absoluta desde la raíz de resources / classpath
        URL fxmlUrl = getClass().getResource("/speco/toppgsql/FXMLActivityHistory.fxml");
        
        // Validación de seguridad para detectar si el recurso no existe antes de instanciar
        if (fxmlUrl == null) {
            System.err.println("Error: No se encontró FXMLActivityHistory.fxml en /speco/toppgsql/");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        // Opcional: Obtener el controlador si necesitas pasarle datos
        FXMLHistoryController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("History - Active Session History");
        stage.setScene(new Scene(root));
        stage.show();
        controller.showActivity(base);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
