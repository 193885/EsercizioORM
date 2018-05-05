package it.polito.tdp.esercizioorm.model;

public class TestModel {

	public static void main(String[] args) {


		Model m= new Model();
		
		int matricola= 146101;
		
		int result = m.getTotCreditiFromStud(matricola);
		
		System.out.println("tot credditi: "+ result);

	}

}
