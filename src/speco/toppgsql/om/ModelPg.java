/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
