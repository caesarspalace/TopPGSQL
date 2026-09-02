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
