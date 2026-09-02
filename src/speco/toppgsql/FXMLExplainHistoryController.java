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
 *
 * @author Adrian Tabak
 */
public class FXMLExplainHistoryController implements Initializable {

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

    public void showActivity(Integer pid, String base, 
            String usename, String wait_event_type, 
            String wait_event, String query,
            ModelPg modelPg) {
        try {
            Tx tx = new Tx(base);
            this.usename.setText(usename);
            this.Pid.setText(pid.toString());
            this.wait_event_type.setText(wait_event_type);
            this.wait_event.setText(wait_event);
            this.query.setWrapText(true);
            this.query.setText(query);
            this.explain.setText(String.valueOf(modelPg.getExplain(tx, query)));
            
        } catch (Exception ex) {
            Log.error(ex);
        }
    }
}
