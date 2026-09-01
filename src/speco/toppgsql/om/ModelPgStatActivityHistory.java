package speco.toppgsql.om;
/*
*
*
* Copyright (c) 2026-08-31 18:07:23.603, Speco
*
*/
/**
* Model del om PgStatActivityHistory
*
* @author  Adrianto
* @version $Id$
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
	pgstatactivityhistory.setOrderBy(null);
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
	pgstatactivityhistory.setOrderBy(null);
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
