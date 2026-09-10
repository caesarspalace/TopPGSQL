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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.ModelPg;
import speco.toppgsql.om.PgActivityFull;

/**
 *
 * @author Adrian Tabak
 */
public class FXMLExplainController implements Initializable {

    @FXML
    private VBox Vbox;
    @FXML
    private TextArea query;
    @FXML
    private TextArea explain;
    @FXML
    private TextArea index;
    @FXML
    private Button botonIdx;
    private ModelPg modelPg;
    private String base;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void showActivity(String base,ModelPg modelPg) {
        try {
            this.base = base;
            this.modelPg  = modelPg;
        } catch (Exception ex) {
            Log.error(ex);
        }
    }
    public void handlerbotonIdx(ActionEvent event) {
        try {
            Tx tx = new Tx(base);
            Long respuesta;
            if (index.getText() !=  null ||!index.getText().equals("")) 
                respuesta = modelPg.getHypopg(tx, index.getText());
            if (query.getText() != null || !query.getText().equals(""))
                this.explain.setText(String.valueOf(modelPg.getExplain(tx, query.getText())));
            if (index.getText() !=  null ||!index.getText().equals("")) 
                modelPg.getHypopgReset(tx);
        } catch (Exception ex) {
            Log.error(ex);
        }
    }

}
