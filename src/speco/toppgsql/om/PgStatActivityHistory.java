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


import speco.cat.om.Vom;
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
public class PgStatActivityHistory extends Vom implements Serializable {

    private Timestamp snapshot_time = null;
    private Integer pid = null;
    private String usename = null;
    private String datname = null;
    private String state = null;
    private String wait_event_type = null;
    private String wait_event = null;
    private Float cpu_user_seconds = null;
    private Float cpu_system_seconds = null;
    private Long query_id = null;
    private String query = null;

    public PgStatActivityHistory() {
        setSelect("min(snapshot_time) as snapshot_time,pid,usename,datname,state,wait_event_type,wait_event,((sum(cpu_system_seconds)/count(*))+(sum(cpu_user_seconds)/count(*)))/(count(*)*60) as cpu, sum(cpu_user_seconds)/count(*) as cpu_user_seconds,sum(cpu_system_seconds)/count(*) as cpu_system_seconds, query_id,query");
        setPk(null);
        setFrom("pg_stat_activity_history");
        setWhere(null);
        setOrderBy("snapshot_time desc");
        setGroupBy("pid,usename,datname,state,wait_event_type,wait_event,query_id,query");
    }

    public Timestamp getSnapshot_time() {
        return snapshot_time;
    }

    public void setSnapshot_time(Timestamp snapshot_time) {
        this.snapshot_time = snapshot_time;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getDatname() {
        return datname;
    }

    public void setDatname(String datname) {
        this.datname = datname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Float getCpu_user_seconds() {
        return cpu_user_seconds;
    }

    public void setCpu_user_seconds(Float cpu_user_seconds) {
        this.cpu_user_seconds = cpu_user_seconds;
    }
    public Float getCpu() {
        return cpu_user_seconds;
    }

    public void setCpu(Float cpu_user_seconds) {
        this.cpu_user_seconds = cpu_user_seconds;
    }
    public Float getcpu() {
        return cpu_user_seconds;
    }

    public void setcpu(Float cpu_user_seconds) {
        this.cpu_user_seconds = cpu_user_seconds;
    }

    public Float getCpu_system_seconds() {
        return cpu_system_seconds;
    }

    public void setCpu_system_seconds(Float cpu_system_seconds) {
        this.cpu_system_seconds = cpu_system_seconds;
    }

    public Long getQuery_id() {
        return query_id;
    }

    public void setQuery_id(Long query_id) {
        this.query_id = query_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Timestamp getsnapshot_time() {
        return this.snapshot_time;
    }

    public void setsnapshot_time(Timestamp snapshot_time) {
        this.snapshot_time = snapshot_time;
    }

    public Integer getpid() {
        return this.pid;
    }

    public void setpid(Integer pid) {
        this.pid = pid;
    }

    public String getusename() {
        return this.usename;
    }

    public void setusename(String usename) {
        this.usename = usename;
    }

    public String getdatname() {
        return this.datname;
    }

    public void setdatname(String datname) {
        this.datname = datname;
    }

    public String getstate() {
        return this.state;
    }

    public void setstate(String state) {
        this.state = state;
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

    public Float getcpu_user_seconds() {
        return this.cpu_user_seconds;
    }

    public void setcpu_user_seconds(Float cpu_user_seconds) {
        this.cpu_user_seconds = cpu_user_seconds;
    }

    public Float getcpu_system_seconds() {
        return this.cpu_system_seconds;
    }

    public void setcpu_system_seconds(Float cpu_system_seconds) {
        this.cpu_system_seconds = cpu_system_seconds;
    }

    public Long getquery_id() {
        return this.query_id;
    }

    public void setquery_id(Long query_id) {
        this.query_id = query_id;
    }

    public String getquery() {
        return this.query;
    }

    public void setquery(String query) {
        this.query = query;
    }

}
