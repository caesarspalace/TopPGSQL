package speco.toppgsql.om;

import speco.cat.om.Vom;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Clob;
import java.sql.Blob;

/**
 * TYPEADO POR EL GOMO
 */
public class VLockRecursive extends Vom implements Serializable {

    private String visual_tree = null;
    private Integer blocking_pid = null;
    private Integer blocked_pid = null;
    private String usuario_bloqueador = null;
    private String usuario_bloqueado = null;
    private String wait_event_type = null;
    private String wait_event = null;
    private Float tiempo_espera_seg = null;
    private String consulta_bloqueadora = null;
    private String consulta_bloqueada = null;
    
    public VLockRecursive() {
        setSelect("visual_tree,blocking_pid,blocked_pid,usuario_bloqueador,usuario_bloqueado,wait_event_type,wait_event,tiempo_espera_seg,consulta_bloqueadora,consulta_bloqueada"  );
        setPk(null);
        setFrom("v_lock_recursive");
        setWhere(null);
        setOrderBy(null);
    }

    public String getVisual_tree() {
        return visual_tree;
    }

    public void setVisual_tree(String visual_tree) {
        this.visual_tree = visual_tree;
    }

    public Integer getBlocking_pid() {
        return blocking_pid;
    }

    public void setBlocking_pid(Integer blocking_pid) {
        this.blocking_pid = blocking_pid;
    }

    public Integer getBlocked_pid() {
        return blocked_pid;
    }

    public void setBlocked_pid(Integer blocked_pid) {
        this.blocked_pid = blocked_pid;
    }

    public String getUsuario_bloqueador() {
        return usuario_bloqueador;
    }

    public void setUsuario_bloqueador(String usuario_bloqueador) {
        this.usuario_bloqueador = usuario_bloqueador;
    }

    public String getUsuario_bloqueado() {
        return usuario_bloqueado;
    }

    public void setUsuario_bloqueado(String usuario_bloqueado) {
        this.usuario_bloqueado = usuario_bloqueado;
    }

    public String getWait_event_type() {
        return wait_event_type;
    }

    public void setWait_event_type(String wait_event_type) {
        this.wait_event_type = wait_event_type;
    }

    public String getWait_event() {
        return wait_event;
    }

    public void setWait_event(String wait_event) {
        this.wait_event = wait_event;
    }

    public Float getTiempo_espera_seg() {
        return tiempo_espera_seg;
    }

    public void setTiempo_espera_seg(Float tiempo_espera_seg) {
        this.tiempo_espera_seg = tiempo_espera_seg;
    }

    public String getConsulta_bloqueadora() {
        return consulta_bloqueadora;
    }

    public void setConsulta_bloqueadora(String consulta_bloqueadora) {
        this.consulta_bloqueadora = consulta_bloqueadora;
    }

    public String getConsulta_bloqueada() {
        return consulta_bloqueada;
    }

    public void setConsulta_bloqueada(String consulta_bloqueada) {
        this.consulta_bloqueada = consulta_bloqueada;
    }

 
    public String getvisual_tree() {
        return this.visual_tree;
    }

    public void setvisual_tree(String visual_tree) {
        this.visual_tree = visual_tree;
    }

    public Integer getblocking_pid() {
        return this.blocking_pid;
    }

    public void setblocking_pid(Integer blocking_pid) {
        this.blocking_pid = blocking_pid;
    }

    public Integer getblocked_pid() {
        return this.blocked_pid;
    }

    public void setblocked_pid(Integer blocked_pid) {
        this.blocked_pid = blocked_pid;
    }

    public String getusuario_bloqueador() {
        return this.usuario_bloqueador;
    }

    public void setusuario_bloqueador(String usuario_bloqueador) {
        this.usuario_bloqueador = usuario_bloqueador;
    }

    public String getusuario_bloqueado() {
        return this.usuario_bloqueado;
    }

    public void setusuario_bloqueado(String usuario_bloqueado) {
        this.usuario_bloqueado = usuario_bloqueado;
    }

    public String getwait_event_type() {
        return this.wait_event_type;
    }

    public void setwait_event_type(String wait_event_type) {
        this.wait_event_type = wait_event_type;
    }

    public String getwait_event() {
        return this.wait_event;
    }

    public void setwait_event(String wait_event) {
        this.wait_event = wait_event;
    }

    public Float gettiempo_espera_seg() {
        return this.tiempo_espera_seg;
    }

    public void settiempo_espera_seg(Float tiempo_espera_seg) {
        this.tiempo_espera_seg = tiempo_espera_seg;
    }

    public String getconsulta_bloqueadora() {
        return this.consulta_bloqueadora;
    }

    public void setconsulta_bloqueadora(String consulta_bloqueadora) {
        this.consulta_bloqueadora = consulta_bloqueadora;
    }

    public String getconsulta_bloqueada() {
        return this.consulta_bloqueada;
    }

    public void setconsulta_bloqueada(String consulta_bloqueada) {
        this.consulta_bloqueada = consulta_bloqueada;
    }

}
