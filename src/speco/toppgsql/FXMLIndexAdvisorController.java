package speco.toppgsql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import speco.cat.Tx;
import speco.toppgsql.om.ModelIndexAdvisor;
import speco.toppgsql.om.VIndexAdvisor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLIndexAdvisorController implements Initializable {

    @FXML private TableView<VIndexAdvisor> tblIndexAdvisor;
    @FXML private TableColumn<VIndexAdvisor, String> colTabla;
    @FXML private TableColumn<VIndexAdvisor, String> colTamanio;
    @FXML private TableColumn<VIndexAdvisor, Long> colSeqScan;
    @FXML private TableColumn<VIndexAdvisor, Float> colPctSeq;
    @FXML private TableColumn<VIndexAdvisor, Float> colTiempoMedio;
    @FXML private TableColumn<VIndexAdvisor, String> colConsulta;
    @FXML private TableColumn<VIndexAdvisor, String> colSugerencia;

    @FXML private Button btnRefrescar;
    @FXML private Button btnCopiarDDL;

    private final ObservableList<VIndexAdvisor> listaSugerencias = FXCollections.observableArrayList();
    private String base;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        configurarEventos();
        
    }

    private void configurarColumnas() {
        colTabla.setCellValueFactory(new PropertyValueFactory<>("tabla"));
        colTamanio.setCellValueFactory(new PropertyValueFactory<>("tamanio_tabla"));
        colSeqScan.setCellValueFactory(new PropertyValueFactory<>("seq_scan"));
        colPctSeq.setCellValueFactory(new PropertyValueFactory<>("pct_seq_scan"));
        colTiempoMedio.setCellValueFactory(new PropertyValueFactory<>("tiempo_medio_ms"));
        colConsulta.setCellValueFactory(new PropertyValueFactory<>("consulta_sql"));
        colSugerencia.setCellValueFactory(new PropertyValueFactory<>("sugerencia_ddl"));

        tblIndexAdvisor.setItems(listaSugerencias);
    }

    private void configurarEventos() {
        btnRefrescar.setOnAction(e -> cargarDatos(base));
        btnCopiarDDL.setOnAction(e -> copiarDDLAlPortapapeles());
    }

    @FXML
    public void cargarDatos(String base) {
        listaSugerencias.clear();
        try {
            Tx tx = new Tx(base);
            ModelIndexAdvisor modelia = new ModelIndexAdvisor();
            List<VIndexAdvisor> resultado = (List<VIndexAdvisor>) modelia.retriveAllVIndexAdvisor(tx);

            if (resultado != null) {
                listaSugerencias.addAll(resultado);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron obtener métricas de sugerencias: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void copiarDDLAlPortapapeles() {
        VIndexAdvisor seleccionado = tblIndexAdvisor.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona una fila para copiar la sugerencia DDL.", Alert.AlertType.WARNING);
            return;
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(seleccionado.getSugerencia_ddl());
        Clipboard.getSystemClipboard().setContent(content);

        mostrarAlerta("Copiado", "Script DDL copiado al portapapeles.", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    public void showIndexAdvisor(String base){
        this.base = base;
        cargarDatos(base);
    }
}
