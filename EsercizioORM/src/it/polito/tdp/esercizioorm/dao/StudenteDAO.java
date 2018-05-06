package it.polito.tdp.esercizioorm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.model.Corso;
import it.polito.tdp.esercizioorm.model.Studente;
import it.polito.tdp.esercizioorm.model.StudenteIdMap;

public class StudenteDAO {

	public List<Studente> getTuttiStudenti(StudenteIdMap studMap)
	{

		String sql = "SELECT matricola, nome, cognome, cds FROM studente";

		List<Studente> result = new ArrayList<Studente>();

		try {
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				Studente s= new Studente(res.getInt("matricola"), res.getString("nome"), res.getString("cognome"),res.getString("cds"));
				
				result.add(studMap.get(s));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	public void getStudentiFromCorso(Corso c, StudenteIdMap studMap) {
	
		String sql = "SELECT s.matricola, cognome, nome, cds FROM studente as s, iscrizione as i WHERE s.matricola = i.matricola and i.codins= ? " ;

		try {
			
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1,c.getCodIns());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Studente s = new Studente(res.getInt("matricola"), res.getString("nome"), res.getString("cognome"), res.getString("cds"));
				
				c.getStudenti().add(studMap.get(s));
				
				//aggiungo al risultato il valore tornato dalla mappa cioè l'oggetto che ho precedentemente creato
				//se non era presente nella mappa o l'oggetto presente nella mappa se esiste tale oggetto				
				
			}
			
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e) ;
		}
	}

	
	/*vediamo ora come il pattern ORM gestisce l'aggiornamento di informazioni nel database: le info vanno salvate sia in memoria che nel db 
	 * per mantenere la consistenza sui dati*/
	
	public boolean iscriviStudenteACorso(Studente studente, Corso corso) {

		String sql = "INSERT IGNORE INTO `iscritticorsi`.`iscrizione` (`matricola`, `codins`) VALUES(?,?)";
		boolean returnValue = false;
		
		try {
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, studente.getMatricola());
			st.setString(2, corso.getCodIns());
			
			int res = st.executeUpdate();	

			if (res == 1)
				returnValue = true;

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
		return returnValue;
}
	
	
	/*
	 * Questo metodo viene utilizzato solo per testare le performance di ConnectDBCP.
	 */
	public boolean studenteIscrittoACorso(int matricola, String codins) {
		String sql = "Select matricola, codins from iscrizione where matricola = ? and codins = ?";
		boolean result = false;
		
		try {
			
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matricola);
			st.setString(2, codins);
			ResultSet res = st.executeQuery();

			if (res.next()) {
				result = true;
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
}





}
