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