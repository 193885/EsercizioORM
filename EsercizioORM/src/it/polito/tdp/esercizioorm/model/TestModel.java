package it.polito.tdp.esercizioorm.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		int matricola= 146101;
		
		int result = m.getTotCreditiFromStud(matricola);
		
		System.out.println("tot credditi: "+ result);
		
		List <Studente> resultS = m.getStudentiFromCorso("01NBAPG");
		
		for (Studente s : resultS) {
			
			System.out.println(s); 
		}
		
		List <Corso> resultC = m.getCorsiFromStud(matricola);
			
		for (Corso c : resultC) {
			
			System.out.println(c);
		}
	}
}
