package speco.toppgsql.om;

import speco.cat.om.Vom;
import java.io.Serializable;

public class VIndexAdvisor extends Vom implements Serializable {

    private String tabla;
    private Long seq_scan;
    private Long seq_tup_read;
    private Long idx_scan;
    private String tamanio_tabla;
    private Float pct_seq_scan;
    private Long queryid;
    private String consulta_sql;
    private Long ejecuciones;
    private Float tiempo_medio_ms;
    private String sugerencia_ddl;

    public VIndexAdvisor() {
        setSelect("*");
        setPk(null);
        setFrom("v_index_advisor");
        setWhere(null);
        setOrderBy(null);
    }

    public String getTabla() { return tabla; }
    public void setTabla(String tabla) { this.tabla = tabla; }

    public Long getSeq_scan() { return seq_scan; }
    public void setSeq_scan(Long seq_scan) { this.seq_scan = seq_scan; }

    public Long getSeq_tup_read() { return seq_tup_read; }
    public void setSeq_tup_read(Long seq_tup_read) { this.seq_tup_read = seq_tup_read; }

    public Long getIdx_scan() { return idx_scan; }
    public void setIdx_scan(Long idx_scan) { this.idx_scan = idx_scan; }

    public String getTamanio_tabla() { return tamanio_tabla; }
    public void setTamanio_tabla(String tamanio_tabla) { this.tamanio_tabla = tamanio_tabla; }

    public Float getPct_seq_scan() { return pct_seq_scan; }
    public void setPct_seq_scan(Float pct_seq_scan) { this.pct_seq_scan = pct_seq_scan; }

    public Long getQueryid() { return queryid; }
    public void setQueryid(Long queryid) { this.queryid = queryid; }

    public String getConsulta_sql() { return consulta_sql; }
    public void setConsulta_sql(String consulta_sql) { this.consulta_sql = consulta_sql; }

    public Long getEjecuciones() { return ejecuciones; }
    public void setEjecuciones(Long ejecuciones) { this.ejecuciones = ejecuciones; }

    public Float getTiempo_medio_ms() { return tiempo_medio_ms; }
    public void setTiempo_medio_ms(Float tiempo_medio_ms) { this.tiempo_medio_ms = tiempo_medio_ms; }

    public String getSugerencia_ddl() { return sugerencia_ddl; }
    public void setSugerencia_ddl(String sugerencia_ddl) { this.sugerencia_ddl = sugerencia_ddl; }
    public String gettabla() { return tabla; }
    public void settabla(String tabla) { this.tabla = tabla; }

    public Long getseq_scan() { return seq_scan; }
    public void setseq_scan(Long seq_scan) { this.seq_scan = seq_scan; }

    public Long getseq_tup_read() { return seq_tup_read; }
    public void setseq_tup_read(Long seq_tup_read) { this.seq_tup_read = seq_tup_read; }

    public Long getidx_scan() { return idx_scan; }
    public void setidx_scan(Long idx_scan) { this.idx_scan = idx_scan; }

    public String gettamanio_tabla() { return tamanio_tabla; }
    public void settamanio_tabla(String tamanio_tabla) { this.tamanio_tabla = tamanio_tabla; }

    public Float getpct_seq_scan() { return pct_seq_scan; }
    public void setpct_seq_scan(Float pct_seq_scan) { this.pct_seq_scan = pct_seq_scan; }

    public Long getqueryid() { return queryid; }
    public void setqueryid(Long queryid) { this.queryid = queryid; }

    public String getconsulta_sql() { return consulta_sql; }
    public void setconsulta_sql(String consulta_sql) { this.consulta_sql = consulta_sql; }

    public Long getejecuciones() { return ejecuciones; }
    public void setejecuciones(Long ejecuciones) { this.ejecuciones = ejecuciones; }

    public Float gettiempo_medio_ms() { return tiempo_medio_ms; }
    public void settiempo_medio_ms(Float tiempo_medio_ms) { this.tiempo_medio_ms = tiempo_medio_ms; }

    public String getsugerencia_ddl() { return sugerencia_ddl; }
    public void setsugerencia_ddl(String sugerencia_ddl) { this.sugerencia_ddl = sugerencia_ddl; }
}
