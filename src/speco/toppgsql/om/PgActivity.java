/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package speco.toppgsql.om;

import speco.cat.om.Vom;

/**
 *
 * @author adrian
 */
public class PgActivity extends Vom {
    String rolname;
    Integer pid;
    String query;
    String wait_event_type;
    String wait_event;
    String state;
    Long cpu;

    public PgActivity() {
            setSelect("usename rolname, "
                    + "pid, substr(query,1,34) query,"
                    + "COALESCE(wait_event_type,'CPU') as wait_event_type,"
                    + "COALESCE(wait_event,'CPU EXEC') as wait_event,"
                    + "state,"
                    + "ROUND(CAST((k.exec_user_time + k.exec_system_time) AS numeric), 2) AS cpu "
                         //       + "ROUND(CAST(k.exec_user_time AS numeric), 2) AS cpu_user_seconds,"
             //       + "ROUND(CAST(k.exec_system_time AS numeric), 2) AS cpu_system_seconds,"
    
                    //       + "pg_size_pretty(k.exec_reads) AS disco_lectura_real," 
             //       + "pg_size_pretty(k.exec_writes) AS disco_escritura_real"
                    );
            setFrom("pg_stat_activity a"
                    + " LEFT JOIN pg_stat_kcache() k " +
"  ON a.query_id = k.queryid " +
" AND a.usesysid = k.userid " +
" AND a.datid = k.dbid");
            setWhere("a.state = 'active' and a.pid != pg_backend_pid()");
          /*  setWhere(" --"
                    + " a.query_id = k.queryid " +
                     " AND a.usesysid = k.userid " +
                     " AND a.datid = k.dbid"
                    + " and a.state = 'active'"
                    );*/
            setOrderBy("cpu DESC");
    }
        public String getRolname() {
        return rolname;
    }

    public void setRolname(String rolname) {
        this.rolname = rolname;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
     public Long getCpu() {
        return cpu;
    }

    public void setCpu(Long cpu) {
        this.cpu = cpu;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getrolname() {
        return rolname;
    }

    public void setrolname(String rolname) {
        this.rolname = rolname;
    }

    public Integer getpid() {
        return pid;
    }

    public void setpid(Integer pid) {
        this.pid = pid;
    }

    public String getquery() {
        return query;
    }

    public void setquery(String query) {
        this.query = query;
    }

    public Long getcpu() {
        return cpu;
    }

    public void setcpu(Long cpu) {
        this.cpu = cpu;
    }

    public String getwait_event_type() {
        return wait_event_type;
    }

    public void setwait_event_type(String wait_event_type) {
        this.wait_event_type = wait_event_type;
    }

    public String getwait_event() {
        return wait_event;
    }

    public void setwait_event(String wait_event) {
        this.wait_event = wait_event;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

}
