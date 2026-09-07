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
/**
 *
 * @author Adrian Tabak
 */
import java.util.ArrayList;
import java.util.List;
import speco.cat.Rdbms;
import speco.cat.Tx;
import speco.cat.util.Log;
import java.sql.Timestamp;

public class ModelPgStatActivityHistory{
	
	public List<PgStatActivityHistory> retriveAllPgStatActivityHistory(Tx tx) throws Exception	{
	PgStatActivityHistory pgstatactivityhistory = new PgStatActivityHistory();
	Object[] argumentos = null;
	List<PgStatActivityHistory> listaPgStatActivityHistory = new ArrayList<PgStatActivityHistory>(15);
	tx.begin();
	tx.setPagesize(pgstatactivityhistory,150);
	pgstatactivityhistory.setWhere(null);
	pgstatactivityhistory.setOrderBy("snapshot_time desc");
	listaPgStatActivityHistory = tx.select(tx,pgstatactivityhistory,argumentos,listaPgStatActivityHistory);
	tx.end();
        return listaPgStatActivityHistory;
	}

      	public List<PgStatActivityHistory> retriveTsPgStatActivityHistory(Timestamp tsd, Timestamp tsh, Tx tx) throws Exception	{
	PgStatActivityHistory pgstatactivityhistory = new PgStatActivityHistory();
	Object[] argumentos = {tsd,tsh};
	List<PgStatActivityHistory> listaPgStatActivityHistory = new ArrayList<PgStatActivityHistory>(15);
	tx.begin();
	tx.setPagesize(pgstatactivityhistory,150);
	pgstatactivityhistory.setWhere("snapshot_time > ? and snapshot_time <= ?");
	pgstatactivityhistory.setOrderBy("snapshot_time desc");
	listaPgStatActivityHistory = tx.select(tx,pgstatactivityhistory,argumentos,listaPgStatActivityHistory);
	tx.end();
        return listaPgStatActivityHistory;
	}

/*	
	PgStatActivityHistory retrivePgStatActivityHistory(Integer id) throws Exception	{
	PgStatActivityHistory pgstatactivityhistory = new PgStatActivityHistory();
	Object[] argumentos = {id};
		tx.begin();
		tx.setPagesize(pgstatactivityhistory,1);
		pgstatactivityhistory.setWhere(null);
		pgstatactivityhistory.setOrderBy(null);
		pgstatactivityhistory = (PgStatActivityHistory) tx.select(tx,pgstatactivityhistory,argumentos);
	
		tx.end();

	return pgstatactivityhistory;
	}
	PgStatActivityHistory addPgStatActivityHistory(PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.insert(tx,pgstatactivityhistory);
			Log.info(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		tx.commit();
		tx.end();
		return pgstatactivityhistory;
	}

	PgStatActivityHistory addPgStatActivityHistory(Tx tx, PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		try {
			result = tx.insert(tx,pgstatactivityhistory);
			Log.info(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		return pgstatactivityhistory;
	}

	Integer updPgStatActivityHistory(PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.update(tx,pgstatactivityhistory);
			Log.error(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		tx.commit();
		tx.end();
		return result;
	}
	
	Integer updPgStatActivityHistory(Tx tx, PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		try {
			result = tx.update(tx,pgstatactivityhistory);
			Log.error(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		return result;
	}
	
	Integer delPgStatActivityHistory(PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.delete(tx,pgstatactivityhistory);
			Log.error(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		tx.commit();
		tx.end();
		return result;
	}

	Integer delPgStatActivityHistory(Tx tx, PgStatActivityHistory pgstatactivityhistory) throws Exception{
		int result = 0;
		try {
			result = tx.delete(tx,pgstatactivityhistory);
			Log.error(tx.getSqlError(pgstatactivityhistory));
		} catch (Exception ex) {Log.error(ex);}
			pgstatactivityhistory.setRowCount(result);
			pgstatactivityhistory.setSqlError(tx.getSqlError(pgstatactivityhistory));
		return result;
	}
*/
}
