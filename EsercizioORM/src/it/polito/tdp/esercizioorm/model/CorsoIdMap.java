package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

/*
 * 	Questa IdMap viene creata nel MODEL ma popolata nel DAO*/

public class CorsoIdMap {
	
	private Map <String, Corso> map;
	
	public CorsoIdMap() {
		
		map = new HashMap<>();
		
	}
	
	public Corso get(Corso c) {
		
		//passo oggetto intero e non solo un codCorso perchè se non è presente il 
		//corso dovrò crearlo e poi aggiungerlo alla mappa
		
		Corso old = map.get(c.getCodIns());
		
		//in old viene salvato null se non c'è corrispondenza nella mappa o l'oggetto 
		//associato a quel codIns che prima qualcuno aveva creato
		
		if(old == null) {
			
			map.put(c.getCodIns(), c);
			
			return c;
		}
		
		return old;
	}
	
	public void put(String codIns,Corso corso) {
		
		map.put(codIns, corso);
		
	}
}
 