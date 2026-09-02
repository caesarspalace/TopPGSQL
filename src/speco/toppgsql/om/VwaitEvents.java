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
public class VwaitEvents extends Om implements Serializable{
	private Integer  pid= null;
	private String usuario= null;
	private String base_datos= null;
	private String  estado= null;
	private String  wait_event_type= null;
	private String  wait_event= null;
	private String  consulta_actual= null;
	private Integer  duracion= null;
	public VwaitEvents(){
		setPk(null);
		setSqlRoi("true");
		setWhere(null);
		setOrderBy(null);
	}
	public Integer getpid(){
		return this.pid;
	}

	public void setpid(Integer pid){
		this.pid = pid;
	}

	public String getusuario(){
		return this.usuario;
	}

	public void setusuario(String usuario){
		this.usuario = usuario;
	}

	public String getbase_datos(){
		return this.base_datos;
	}

	public void setbase_datos(String base_datos){
		this.base_datos = base_datos;
	}

	public String getestado(){
		return this.estado;
	}

	public void setestado(String estado){
		this.estado = estado;
	}

	public String getwait_event_type(){
		return this.wait_event_type;
	}

	public void setwait_event_type(String wait_event_type){
		this.wait_event_type = wait_event_type;
	}

	public String getwait_event(){
		return this.wait_event;
	}

	public void setwait_event(String wait_event){
		this.wait_event = wait_event;
	}

	public String getconsulta_actual(){
		return this.consulta_actual;
	}

	public void setconsulta_actual(String consulta_actual){
		this.consulta_actual = consulta_actual;
	}

	public Integer getduracion(){
		return this.duracion;
	}

	public void setduracion(Integer duracion){
		this.duracion = duracion;
	}

}