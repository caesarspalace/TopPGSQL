/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
