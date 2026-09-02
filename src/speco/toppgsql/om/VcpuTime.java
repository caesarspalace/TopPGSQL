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


import speco.cat.om.Om;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Clob;
import java.sql.Blob;
/** TYPEADO POR EL GOMO */
/**
 *
 * @author Adrian Tabak
 */
public class VcpuTime extends Om implements Serializable{
	private String  tipo_espera= null;
	private Integer  total_sesiones= null;
	public VcpuTime(){
		setPk(null);
		setSqlRoi("true");
		setWhere(null);
		setOrderBy(null);
	}
	public String gettipo_espera(){
		return this.tipo_espera;
	}

	public void settipo_espera(String tipo_espera){
		this.tipo_espera = tipo_espera;
	}

	public Integer gettotal_sesiones(){
		return this.total_sesiones;
	}

	public void settotal_sesiones(Integer total_sesiones){
		this.total_sesiones = total_sesiones;
	}

}