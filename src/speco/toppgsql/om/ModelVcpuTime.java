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

class ModelVcpuTime{
	Tx tx = new Tx();
	List<VcpuTime> retriveAllVcpuTime(Integer id, String status) throws Exception	{
	VcpuTime vcputime = new VcpuTime();
	Object[] argumentos = {id};
	List<VcpuTime> listaVcpuTime = new ArrayList<VcpuTime>(15);
	if(status.equals(Tx.PAGINA_INICIO)){
		tx.begin();
		tx.setPagesize(vcputime,15);
		vcputime.setWhere("id > ? ");
		vcputime.setOrderBy(null);
		listaVcpuTime = tx.select(tx,vcputime,argumentos,listaVcpuTime);
	}
	
	if(status.equals(Tx.PAGINA_MAS))
		if(tx.hasFetch(vcputime))
			listaVcpuTime=tx.fetch(vcputime,listaVcpuTime);
		else listaVcpuTime=null;
	
	if(status.equals(Tx.PAGINA_FIN)){
		tx.end();
	}

	return listaVcpuTime;
	}
	VcpuTime retriveVcpuTime(Integer id) throws Exception	{
	VcpuTime vcputime = new VcpuTime();
	Object[] argumentos = {id};
		tx.begin();
		tx.setPagesize(vcputime,1);
		vcputime.setWhere(null);
		vcputime.setOrderBy(null);
		vcputime = (VcpuTime) tx.select(tx,vcputime,argumentos);
	
		tx.end();

	return vcputime;
	}
	VcpuTime addVcpuTime(VcpuTime vcputime) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.insert(tx,vcputime);
			Log.info(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		tx.commit();
		tx.end();
		return vcputime;
	}

	VcpuTime addVcpuTime(Tx tx, VcpuTime vcputime) throws Exception{
		int result = 0;
		try {
			result = tx.insert(tx,vcputime);
			Log.info(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		return vcputime;
	}

	Integer updVcpuTime(VcpuTime vcputime) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.update(tx,vcputime);
			Log.error(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		tx.commit();
		tx.end();
		return result;
	}
	
	Integer updVcpuTime(Tx tx, VcpuTime vcputime) throws Exception{
		int result = 0;
		try {
			result = tx.update(tx,vcputime);
			Log.error(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		return result;
	}
	
	Integer delVcpuTime(VcpuTime vcputime) throws Exception{
		int result = 0;
		tx.begin();
		try {
			result = tx.delete(tx,vcputime);
			Log.error(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		tx.commit();
		tx.end();
		return result;
	}

	Integer delVcpuTime(Tx tx, VcpuTime vcputime) throws Exception{
		int result = 0;
		try {
			result = tx.delete(tx,vcputime);
			Log.error(tx.getSqlError(vcputime));
		} catch (Exception ex) {Log.error(ex);}
			vcputime.setRowCount(result);
			vcputime.setSqlError(tx.getSqlError(vcputime));
		return result;
	}

}
