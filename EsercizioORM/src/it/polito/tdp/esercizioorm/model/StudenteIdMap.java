package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

/*
 * 	Questa IdMap viene creata nel MODEL ma popolata nel DAO*/

public class StudenteIdMap {
	
	private Map <Integer, Studente> map;
	
	public StudenteIdMap() {
		
		map = new HashMap<>();
		
	}
	
	public Studente get(Studente s) {
		
		//passo oggetto intero e non solo un codCorso perchè se non è presente il 
		//corso dovrò crearlo e poi aggiungerlo alla mappa
		
		Studente old = map.get(s.getMatricola());
		
		//in old viene salvato null se non c'è corrispondenza nella mappa o l'oggetto 
		//associato a quel codIns che prima qualcuno aveva creato
		
		if(old == null) {
			
			map.put(s.getMatricola(), s);
			
			return s;
		}
		
		return old;
	}
	
	public void put(Integer matricola,Studente s) {
		
		map.put(matricola, s);
		
	}

	public Studente get(int matricola) {
		
		return map.get(matricola);

	}
}
 