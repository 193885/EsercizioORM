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
			Connection conn = ConnectDB.getConnection();
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
			
			Connection conn = ConnectDB.getConnection();
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
}
