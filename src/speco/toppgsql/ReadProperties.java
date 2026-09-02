/*
 * @autor: Adrian Tabak
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package speco.toppgsql;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import speco.cat.util.Log;

/**
 *
 * @author Adrian Tabak
 */
public class ReadProperties {

    List<PgBases> bases = new ArrayList<PgBases>();

    private static String archivoPropiedades = System.getProperty("cat.file");

    ReadProperties(String url, String user, String rdbms, String nombre){
    }
    public ReadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(archivoPropiedades));
            Integer pool = Integer.valueOf(properties.getProperty("pool"));
            for (int i = 1; i <= pool; i++) {
                String urlt = properties.getProperty("url_" + i);
                String usert = properties.getProperty("user_" + i);
                String rdbmst = properties.getProperty("rdbms_" + i);
                String nombret = properties.getProperty("nombre_" + i);
                if (nombret != null){
                    bases.add(new PgBases(urlt, usert, rdbmst, nombret));
                }
            }
        } catch (IOException ioexception) {
            Log.error(ioexception.toString());
        }

    }

    public List<PgBases> getBases() {
        return bases;
    }

}
