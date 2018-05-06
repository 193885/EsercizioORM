package it.polito.tdp.esercizioorm.model;

import java.util.ArrayList;
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
	private StudenteIdMap studMap;
	
	public Model() {
		
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsoMap = new CorsoIdMap();
		studMap = new StudenteIdMap();
		
		corsi= cdao.getTuttiCorsi(corsoMap); //cosi quando creo Corsi li inserisco in automatico nella Mappa
		
	//	System.out.println(corsi.size());		
	
		//dovrei fare la stessa cosa per Studenti ma per come strutturato il programma non voglio lavorare su 
		//studenti e non sarò interessato a fare operazioni su collection di studenti mi basta memorizzarli
		
		
		students=sdao.getTuttiStudenti(studMap);
		
	//	System.out.println(students.size());
		
		
		//aggiungo i riferimenti incrociati
		
		for(Studente s : students) {
			
			cdao.getCorsiFromStudente(s,corsoMap);
		}
			
		//devo fare la stessa operazione sui corsi per poter sapere dato un corso quali stud sono iscritti	
			
			
		for	(Corso c : corsi) {
		
			sdao.getStudentiFromCorso(c,studMap);
		}
	}
	
	public List <Studente> getStudentiFromCorso(String codins){
		
		//potrei fare una ricerca lineare nella lista di corsi come fatto per studenti con la matricola (sotto)
		//oppure posso sfruttare identity map visto che sono sicuro che tutti i corsi presenti nella lista dei corsi 
		//sono presenti nell'identity map.
		
		Corso c = corsoMap.get(codins);
		
		//ho bisogno di un oggetto corso per il get dell'idmap, o costruisco un oggetto corso con solo codins settato
		// e poi get userà metodo equals che è definito su codins oppure devo cambiare la signature di get
	
		
		if(c == null) //l'utente potrebbe inserire un corso non presente nel DB
			
			return new ArrayList<Studente>(); //nel caso ritorno una lista vuota così programma non crasha 
		
		return c.getStudenti();
	}
	
	public List <Corso> getCorsiFromStud(int matricola){
		
		//potrei fare una ricerca lineare nella lista degli studenti 
		//oppure posso sfruttare identity map visto che sono sicuro che tutti gli studenti presenti nella lista degli studenti 
		//su cui farei la ricerca lineare sono presenti nell'identity map.
				
		Studente s = studMap.get(matricola);
				
		//ho bisogno di un oggetto studente per il get dell'identitymap, o costruisco un oggetto studente con solo il campo matricola settato
		// e poi get userà metodo equals che è definito su matricola oppure devo cambiare la signature di get dell'identitymap
			
		
		if(s == null) //l'utente potrebbe anche insere uno studente non presente nel db
			
			return new ArrayList <Corso>(); //creo una lista vuota da tornare per non fare crashare programma
											//potrei tornare NULL ma dovrei fare controllo sul model che sia != null,visto che devo tornare una
											//lista tanto vale tornare una lista vuota a men che non mi venga chiesto diversamente
		return s.getCorsi();			
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
	
	public boolean iscriviStudenteACorso(int matricola, String codins) {
		
		Corso corso = corsoMap.get(codins);
		
		Studente studente = studMap.get(matricola);
		
		
		if(studente == null || corso == null) {
			
			return false;
			
			//uno dei due non esiste e quindi non posso fare iscrizione
		}
		
		//devo aggiornare prima il db e poi la memoria perchè se la prima fallisse non avrei piu' la consistenza dei dati
		
		boolean iscrizione = sdao.iscriviStudenteACorso(studente, corso);
		
		//meglio se al DAO passo sempre degli oggetti,il model non ha visione su come questi oggetti siano salvati nel db

		if( iscrizione ) {
			 //aggiornamento DB andato a buon fine, aggiorno riferimenti in memoria
		
			//conviene inserire una if di controllo sulla presenza di duplicati (sto usando liste e non set) perchè in base
			//a come scrivo le query i duplicati potrebbero non essere ignorati
			
			if(!studente.getCorsi().contains(corso))
			
				studente.getCorsi().add(corso);
			
			if(!corso.getStudenti().contains(studente))
		
				corso.getStudenti().add(studente);
			
			return true;
		}
		
		return false; // inserimento nel DAO fallito 
	}
	
	
	/*
	 * Questo metodo viene utilizzato solo per testare le performance di ConnectDBCP.
	 */
	public void testCP() {
		double avgTime = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			List<Studente> studenti = sdao.getTuttiStudenti(new StudenteIdMap());
			for (Studente s : studenti) {
				sdao.studenteIscrittoACorso(s.getMatricola(), "01NBAPG");
			}
			double tt = (System.nanoTime() - start) / 10e9;
			System.out.println(tt);
			avgTime += tt;
		}
		System.out.println("AvgTime (mean on 10 loops): " + avgTime/10);
}
	
	
}
