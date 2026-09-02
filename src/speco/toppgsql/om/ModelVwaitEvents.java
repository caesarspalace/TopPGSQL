package speco.toppgsql.om;
/*
*
*
* Copyright (c) 2026-08-22 05:24:20.475, Speco
*
*/
/**
 *
 * @author Adrian Tabak
 */
import java.util.ArrayList;
import java.util.List;
import speco.cat.Rdbms;
import speco.cat.Tx;
import speco.cat.util.Log;
class ModelVwaitEvents{
	Tx tx = new Tx();
	List<VwaitEvents> retriveAllVwaitEvents(Integer id, String status) throws Exception	{
	VwaitEvents vwaitevents = new VwaitEvents();
	Object[] argumentos = {id};
	List<VwaitEvents> listaVwaitEvents = new ArrayList<VwaitEvents>(15);
	if(status.equals(Tx.PAGINA_INICIO)){
		tx.begin();
		tx.setPagesize(vwaitevents,15);
		vwaitevents.setWhere("id > ? ");
		vwaitevents.setOrderBy(null);
		listaVwaitEvents = tx.select(tx,vwaitevents,argumentos,listaVwaitEvents);
	}
	
	if(status.equals(Tx.PAGINA_MAS))
		if(tx.hasFetch(vwaitevents))
			listaVwaitEvents=tx.fetch(vwaitevents,listaVwaitEvents);
		else listaVwaitEvents=null;
	
	if(status.equals(Tx.PAGINA_FIN)){
		tx.end();
	}

	return listaVwaitEvents;
	}
	VwaitEvents retriveVwaitEvents(Integer id) throws Exception	{
	VwaitEvents vwaitevents = new VwaitEvents();
	Object[] argumentos = {id};
		tx.begin();
		tx.setPagesize(vwaitevents,1);
		vwaitevents.setWhere(null);
		vwaitevents.setOrderBy(null);
		vwaitevents = (VwaitEvents) tx.select(tx,vwaitevents,argumentos);
	
		tx.end();

	return vwaitevents;
	}
	VwaitEvents addVwaitEvents(VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.insert(tx,vwaitevents);
			Log.info(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		tx.commit();
		tx.end();
		return vwaitevents;
	}

	VwaitEvents addVwaitEvents(Tx tx, VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		try {
			result = tx.insert(tx,vwaitevents);
			Log.info(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		return vwaitevents;
	}

	Integer updVwaitEvents(VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.update(tx,vwaitevents);
			Log.error(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		tx.commit();
		tx.end();
		return result;
	}
	
	Integer updVwaitEvents(Tx tx, VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		try {
			result = tx.update(tx,vwaitevents);
			Log.error(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		return result;
	}
	
	Integer delVwaitEvents(VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.delete(tx,vwaitevents);
			Log.error(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		tx.commit();
		tx.end();
		return result;
	}

	Integer delVwaitEvents(Tx tx, VwaitEvents vwaitevents) throws Exception{
		int result = 0;
		try {
			result = tx.delete(tx,vwaitevents);
			Log.error(tx.getSqlError(vwaitevents));
		} catch (Exception ex) {Log.error(ex);}
			vwaitevents.setRowCount(result);
			vwaitevents.setSqlError(tx.getSqlError(vwaitevents));
		return result;
	}

}
