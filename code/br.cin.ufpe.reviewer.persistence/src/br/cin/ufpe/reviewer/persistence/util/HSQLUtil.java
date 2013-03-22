package br.cin.ufpe.reviewer.persistence.util;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.persistence.PersistenceException;

import br.cin.ufpe.reviewer.persistence.PersistenceConstants;

public class HSQLUtil {

	private static final String QUERY_REVIEWER_SCHEMA = "SELECT * FROM information_schema.schemata WHERE schema_name = 'REVIEWER';";

	public static void initDatabase() {
		try {
			Class.forName(PersistenceConstants.JDBC_DRIVER);
			Connection connection = DriverManager.getConnection(PersistenceConstants.DB_CONNECTION_STRING, PersistenceConstants.DB_USER, PersistenceConstants.DB_PASSWORD);
			
			if (!existDatabase(connection)) {
				createDatabase(connection);
			}
			
			connection.close();
		} catch (Exception e) {
			throw new PersistenceException("Error trying initialize the system database.", e);
		}
	}
	
	private static void createDatabase(Connection connection) {
		try {
			ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
			scriptRunner.runScript(new InputStreamReader(ClassLoader.getSystemResourceAsStream(PersistenceConstants.DDL_SCRIPT_FILE_NAME)));
		} catch (Exception e) {
			throw new PersistenceException("Error trying to create the system database.", e);
		}
	}
	
	private static boolean existDatabase(Connection connection) {
		boolean exist = false;
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(QUERY_REVIEWER_SCHEMA);
			
			exist = resultSet.next();
			
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			throw new PersistenceException("Error trying to verify the existence of the system database.", e);
		}
		
		return exist;
	}
	
}
