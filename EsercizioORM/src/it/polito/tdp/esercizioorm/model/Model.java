package it.polito.tdp.esercizioorm.model;

import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {
	
	//non ho bisogno di caricare tutti i dati dal DB, ho bisogno solo di sapere dato uno stud 
	// a quali corsi è iscritto e non viceversa

	private List <Corso> corsi;
	private List <Studente> students;
	
	private CorsoDAO cdao;
	private StudenteDAO sdao;
	
	private CorsoIdMap corsoMap;
	
	
	public Model() {
		
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsoMap = new CorsoIdMap();
		
		corsi= cdao.getTuttiCorsi(corsoMap); //cosi quando creo COrsi li inserisco in automatico nella Mappa
		
		System.out.println(corsi.size());		
	
		
		//dovrei fare la stessa cosa per Studenti ma per come strutturato il programma non voglio lavorare su 
		//studenti e non sarò interessato a fare operazioni su collection di studenti mi basta memorizzarli
		
		
		students=sdao.getTuttiStudenti();
		
		System.out.println(students.size());
		
		
		//aggiungo i riferimenti incrociati
		
		for(Studente s : students) {
			
			cdao.getCorsiFromStudente(s,corsoMap);
			
		}
	}
	
	public int getTotCreditiFromStud(int matricola) {

		int sum =0;
		
		for(Studente s : students) {
						
			if(s.getMatricola() == matricola) {
				
				for(Corso c : s.getCorsi())
					
					sum+= c.getCrediti();
				
				return sum;

			}
		}
		
		return -1; // se non trovo matricola torno -1
	}

}
