package br.cin.ufpe.reviewer.persistence.util;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.persistence.PersistenceException;

public class HSQLUtil {

	public static void initDatabase() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:data/br.cin.ufpe.reviewer.db", "sa", "");
			
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
			scriptRunner.runScript(new InputStreamReader(ClassLoader.getSystemResourceAsStream("create.database.script.sql")));
		} catch (Exception e) {
			throw new PersistenceException("Error trying to create the system database.", e);
		}
	}
	
	private static boolean existDatabase(Connection connection) {
		boolean exist = false;
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_name = 'literature_review'");
			
			exist = resultSet.next();
			
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			throw new PersistenceException("Error trying to verify the existence of the system database.", e);
		}
		
		return exist;
	}
	
}
