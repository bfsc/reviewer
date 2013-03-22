package br.cin.ufpe.reviewer.persistence;

public class PersistenceConstants {

public static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

	public static final String DB_USER = "sa";
	public static final String DB_PASSWORD = "";
	public static final String DB_CONNECTION_STRING = "jdbc:hsqldb:file:data/br.cin.ufpe.reviewer.db";
	
	public static final String DDL_SCRIPT_FILE_NAME = "reviewer.ddl.sql";
	public static final String QUERIES_FILE_NAME = "reviewer.jpql.queries.properties";
	
}
