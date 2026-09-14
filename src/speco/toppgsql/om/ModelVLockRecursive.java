package speco.toppgsql.om;
/*
*
*
* Copyright (c) 2026-09-14 08:27:53.979, Speco
*
*/
/**
* Model del om VLockRecursive
*
* @author  Adrianto
* @version $Id$
*/
import java.util.ArrayList;
import java.util.List;
import speco.cat.Rdbms;
import speco.cat.Tx;
import speco.cat.util.Log;
import speco.toppgsql.om.VLockRecursive;
public class ModelVLockRecursive{
	public List<VLockRecursive> retriveAllVLockRecursive(Tx tx) throws Exception	{
	VLockRecursive vlockrecursive = new VLockRecursive();
	Object[] argumentos = null;
	List<VLockRecursive> listaVLockRecursive = new ArrayList<VLockRecursive>(150);
	tx.begin();
	tx.setPagesize(vlockrecursive,150);
	vlockrecursive.setWhere(null);
	vlockrecursive.setOrderBy(null);
	listaVLockRecursive = tx.select(tx,vlockrecursive,argumentos,listaVLockRecursive);
	tx.end();
	
	return listaVLockRecursive;
	}
	
}
