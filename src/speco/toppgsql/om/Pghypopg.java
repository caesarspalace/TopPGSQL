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
public class Pghypopg extends Vom implements Serializable {
    private Long indexrelid;
    private String indexname;

    public Long getIndexrelid() {
        return indexrelid;
    }

    public void setIndexrelid(Long indexrelid) {
        this.indexrelid = indexrelid;
    }

    public String getIndexname() {
        return indexname;
    }

    public void setIndexname(String indexname) {
        this.indexname = indexname;
    }
    public Long getindexrelid() {
        return indexrelid;
    }

    public void setindexrelid(Long indexrelid) {
        this.indexrelid = indexrelid;
    }

    public String getindexname() {
        return indexname;
    }

    public void setindexname(String indexname) {
        this.indexname = indexname;
    }
    
    public Pghypopg() {
        setSelect("indexrelid,indexname");
        setPk(null);
        setFrom("hypopg_create_index($1)");
        setWhere(null);
        setOrderBy(null);
        setGroupBy(null);
    }

}
