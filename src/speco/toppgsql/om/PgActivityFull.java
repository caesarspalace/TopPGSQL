/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package speco.toppgsql.om;

import java.sql.Timestamp;
import speco.cat.om.Vom;

/**
 *
 * @author adrian
 */
public class PgActivityFull extends Vom {

    Integer pid;
    String usename;
    String application_name;
    String client_addr;
    String client_hostname;
    Integer client_port;
    Timestamp query_start;
    String wait_event_type;
    String wait_event;
    String state;
    String query;

    public PgActivityFull() {
        setSelect(" pid,usename,application_name,client_addr::text  client_addr,client_hostname,client_port,query_start,wait_event_type,wait_event,state,query");
        setFrom("pg_stat_activity");
        setWhere("pid = ?");
        setOrderBy(null);
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

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getClient_addr() {
        return client_addr;
    }

    public void setClient_addr(String client_addr) {
        this.client_addr = client_addr;
    }

    public String getClient_hostname() {
        return client_hostname;
    }

    public void setClient_hostname(String client_hostname) {
        this.client_hostname = client_hostname;
    }

    public Integer getClient_port() {
        return client_port;
    }

    public void setClient_port(Integer client_port) {
        this.client_port = client_port;
    }

    public Timestamp getQuery_start() {
        return query_start;
    }

    public void setQuery_start(Timestamp query_start) {
        this.query_start = query_start;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getpid() {
        return pid;
    }

    public void setpid(Integer pid) {
        this.pid = pid;
    }

    public String getusename() {
        return usename;
    }

    public void setusename(String usename) {
        this.usename = usename;
    }

    public String getapplication_name() {
        return application_name;
    }

    public void setapplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getclient_addr() {
        return client_addr;
    }

    public void setclient_addr(String client_addr) {
        this.client_addr = client_addr;
    }

    public String getclient_hostname() {
        return client_hostname;
    }

    public void setclient_hostname(String client_hostname) {
        this.client_hostname = client_hostname;
    }

    public Integer getclient_port() {
        return client_port;
    }

    public void setclient_port(Integer client_port) {
        this.client_port = client_port;
    }

    public Timestamp getquery_start() {
        return query_start;
    }

    public void setquery_start(Timestamp query_start) {
        this.query_start = query_start;
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

    public String getquery() {
        return query;
    }

    public void setquery(String query) {
        this.query = query;
    }

}
