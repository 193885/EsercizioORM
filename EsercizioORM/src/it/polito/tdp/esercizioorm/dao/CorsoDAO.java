package it.polito.tdp.esercizioorm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.model.Corso;
import it.polito.tdp.esercizioorm.model.CorsoIdMap;
import it.polito.tdp.esercizioorm.model.Studente;

public class CorsoDAO {
	
	public List<Corso> getTuttiCorsi(CorsoIdMap corsoMap) {
		
		String sql = "SELECT codins, crediti, nome, pd FROM corso" ;
		
		List<Corso> result = new ArrayList<>() ;
		
		try {
			
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Corso c = new Corso(res.getString("codins"),
						res.getInt("crediti"),
						res.getString("nome"),
						res.getInt("pd") ) ;
				
				result.add(corsoMap.get(c));
				
				//aggiungo al risultato il valore tornato dalla mappa cioè l'oggetto che ho precedentemente creato
				//se non era presente nella mappa o l'oggetto presente nella mappa se esiste tale oggetto				
				
			}
			
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e) ;
		}
		
		return result;
	}

	public void getCorsiFromStudente(Studente s, CorsoIdMap corsoMap) {
	
		String sql = "SELECT c.codins, crediti, nome, pd FROM corso as c, iscrizione as i WHERE c.codins = i.codins and i.matricola= ? " ;

		try {
			
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt( 1, s.getMatricola() );
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Corso c = new Corso(res.getString("codins"), res.getInt("crediti"), res.getString("nome"), res.getInt("pd") ) ;
				
				s.getCorsi().add(corsoMap.get(c));
				
				//aggiungo al risultato il valore tornato dalla mappa cioè l'oggetto che ho precedentemente creato
				//se non era presente nella mappa o l'oggetto presente nella mappa se esiste tale oggetto				
				
			}
			
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e) ;
		}
	}
}
