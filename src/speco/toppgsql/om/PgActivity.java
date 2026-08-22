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

    public PgActivity() {
            setSelect("usename rolname, pid, substr(query,1,34) query,wait_event_type,wait_event,state");
            setFrom("pg_stat_activity");
            setWhere(null);
            setOrderBy("state");
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
