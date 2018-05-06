package it.polito.tdp.esercizioorm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectDBCP {

	private static final String jdbcURL = "jdbc:mysql://localhost/iscritticorsi";

	private static HikariDataSource ds;
	
	public static Connection getConnection() {
		
		if(ds==null) {
			//controllo se è la prima volta che chiamo tale metodoo
		
		HikariConfig config = new HikariConfig();
		
		//config per accedere al DB
		
		config.setJdbcUrl(jdbcURL);
		config.setUsername("root");
		config.setPassword("");
		
		//configurazione per property del drivere di MySQL
		
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		//creo data Source
		
		 ds = new HikariDataSource(config);
		
		}
		
		try {

			return ds.getConnection();
		

		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}
	}

}