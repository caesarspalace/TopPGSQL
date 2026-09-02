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
package speco.toppgsql.om;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import speco.cat.Tx;
import speco.cat.util.Log;

/**
 *
 * @author adrian
 */
public class ModelPg {

    public List<PgActivity> getAllActivities(Tx tx) throws Exception {
        List<PgActivity> listActivity = new ArrayList<PgActivity>(1024);
        PgActivity pgActivity = new PgActivity();
        tx.begin();
        tx.setPagesize(pgActivity, 1024);
        listActivity = tx.select(tx, pgActivity, listActivity);
        tx.commit();
        tx.end();
        return listActivity;
    }

    public PgActivityFull getAllActivitiesFull(Tx tx, Integer pid) throws Exception {
        PgActivityFull pgActivityFull = new PgActivityFull();
        Object[] argumentos = {pid};
        tx.begin();
        tx.setPagesize(pgActivityFull, 1);
        pgActivityFull = (PgActivityFull) tx.select(tx, pgActivityFull, argumentos);
        tx.commit();
        tx.end();
        return pgActivityFull;
    }

    public Object getExplain(Tx tx, String sql) {
        Object respuesta = new Object();
        try {
            Object[] argumentos = {sql};
            tx.begin();
            respuesta =  tx.call(tx, "{ ? = call explain(?) }", argumentos, respuesta);
            tx.commit();
        } catch (Exception ex) {
            Log.error(ex);
        } finally {
            try {
                tx.end();
            } catch (Exception ex) {
                Log.error(ex);
            }
        }
        return respuesta;
    }

}
