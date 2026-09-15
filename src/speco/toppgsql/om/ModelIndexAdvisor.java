package speco.toppgsql.om;
/*
*
*
* Copyright (c) 2026-09-14 08:27:53.979, Speco
*
*/
/**
* Model del om VIndexAdvisor
*
* @author  Adrianto
* @version $Id$
*/
import java.util.ArrayList;
import java.util.List;
import speco.cat.Rdbms;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.VIndexAdvisor;
public class ModelIndexAdvisor{
	public List<VIndexAdvisor> retriveAllVIndexAdvisor(Tx tx) throws Exception	{
	VIndexAdvisor vindexAdvisor = new VIndexAdvisor();
	Object[] argumentos = null;
	List<VIndexAdvisor> listaVIndexAdvisor = new ArrayList<VIndexAdvisor>(150);
	tx.begin();
	tx.setPagesize(vindexAdvisor,150);
	vindexAdvisor.setWhere(null);
	vindexAdvisor.setOrderBy(null);
	listaVIndexAdvisor = tx.select(tx,vindexAdvisor,argumentos,listaVIndexAdvisor);
	tx.end();
	
	return listaVIndexAdvisor;
	}
	
}
