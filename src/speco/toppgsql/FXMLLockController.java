package speco.toppgsql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import speco.cat.Tx;
import speco.toppgsql.om.VLockRecursive;
import speco.toppgsql.om.ModelVLockRecursive;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import speco.toppgsql.om.KillSession;

public class FXMLLockController implements Initializable {

    @FXML private TableView<VLockRecursive> tblLockTree;
    @FXML private TableColumn<VLockRecursive, String> colVisualTree;
    @FXML private TableColumn<VLockRecursive, Integer> colBlockingPid;
    @FXML private TableColumn<VLockRecursive, Integer> colBlockedPid;
    @FXML private TableColumn<VLockRecursive, String> colUserBlocker;
    @FXML private TableColumn<VLockRecursive, String> colUserBlocked;
    @FXML private TableColumn<VLockRecursive, String> colWaitEventType;
    @FXML private TableColumn<VLockRecursive, String> colWaitEvent;
    @FXML private TableColumn<VLockRecursive, Float> colTiempoEspera;
    @FXML private TableColumn<VLockRecursive, String> colQueryBlocker;
    @FXML private TableColumn<VLockRecursive, String> colQueryBlocked;

    @FXML private Button btnRefrescar;
    @FXML private Button btnKillProcess;

    private final ObservableList<VLockRecursive> listaBloqueos = FXCollections.observableArrayList();
    private String base;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        configurarEventos();
    }

    private void configurarColumnas() {
        // Enlace con las propiedades del DAO VLockRecursive
        colVisualTree.setCellValueFactory(new PropertyValueFactory<>("visual_tree"));
        colBlockingPid.setCellValueFactory(new PropertyValueFactory<>("blocking_pid"));
        colBlockedPid.setCellValueFactory(new PropertyValueFactory<>("blocked_pid"));
        colUserBlocker.setCellValueFactory(new PropertyValueFactory<>("usuario_bloqueador"));
        colUserBlocked.setCellValueFactory(new PropertyValueFactory<>("usuario_bloqueado"));
        colWaitEventType.setCellValueFactory(new PropertyValueFactory<>("wait_event_type"));
        colWaitEvent.setCellValueFactory(new PropertyValueFactory<>("wait_event"));
        colTiempoEspera.setCellValueFactory(new PropertyValueFactory<>("tiempo_espera_seg"));
        colQueryBlocker.setCellValueFactory(new PropertyValueFactory<>("consulta_bloqueadora"));
        colQueryBlocked.setCellValueFactory(new PropertyValueFactory<>("consulta_bloqueada"));

        tblLockTree.setItems(listaBloqueos);
    }

    private void configurarEventos() {
        btnRefrescar.setOnAction(event -> cargarDatos(base));
        btnKillProcess.setOnAction(event -> terminarProcesoBloqueador());
    }

    @FXML
    public void cargarDatos(String base) {
        listaBloqueos.clear();
        try {
            Tx tx = new Tx(base);
            ModelVLockRecursive mr = new ModelVLockRecursive();
            // Consulta todos los registros presentes en la vista v_lock_recursive
            List<VLockRecursive> resultado = (List<VLockRecursive>) mr.retriveAllVLockRecursive(tx);
            
            if (resultado != null && !resultado.isEmpty()) {
                listaBloqueos.addAll(resultado);
            }
        } catch (Exception e) {
            mostrarAlerta("Error de Conexión", "No se pudieron obtener los bloqueos activos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void terminarProcesoBloqueador() {
        VLockRecursive seleccionado = tblLockTree.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor, selecciona una fila de la tabla para cancelar el proceso.", Alert.AlertType.WARNING);
            return;
        }

        Integer pidABloquear = seleccionado.getBlocking_pid();
        if (pidABloquear == null || pidABloquear == 0) {
            mostrarAlerta("PID Inválido", "No se encontró un PID bloqueador válido para este registro.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación de Proceso");
        confirmacion.setHeaderText("¿Deseas cancelar la consulta del PID " + pidABloquear + "?");
        confirmacion.setContentText("Se ejecutará pg_cancel_backend(" + pidABloquear + ").");

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ejecutarCancelBackend(pidABloquear);
        }
    }

    private void ejecutarCancelBackend(int pid) {
        try {
             KillSession ks = new KillSession();
             ks.callKillSession(pid, base);
            
            mostrarAlerta("Operación Exitosa", "Se envió la señal de cancelación al PID " + pid, Alert.AlertType.INFORMATION);
            cargarDatos(base); // Recargar la lista para verificar si el bloqueo se liberó
        } catch (Exception e) {
            mostrarAlerta("Error al cancelar", "No se pudo cancelar el proceso: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public void setBase(String base){
        this.base = base;
        cargarDatos(base);
    }
}