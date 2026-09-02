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

import speco.cat.Tx;
/**
 *
 * @author Adrian Tabak
 */
public class KillSession {
   
   public String callKillSession(Integer pid, String base){
   Object[] aobj = {pid};
   Object o = new String("0");

   Tx tx = new Tx(base);
   try{
   tx.begin();
   o = tx.call(tx,"{ ? = call pg_terminate_backend(?) }",aobj,o);
   tx.commit();
   tx.end();
   } catch (Exception ex){System.out.println(ex);}

   
   
   return o.toString();
   }
}
