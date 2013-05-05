package br.ufpe.cin.reviewer.persistence.util;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import br.ufpe.cin.reviewer.persistence.PersistenceConstants;

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
			throw new RuntimeException("Error trying initialize the system database.", e);
		}
	}
	
	private static void createDatabase(Connection connection) {
		try {
			ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
			scriptRunner.runScript(new InputStreamReader(HSQLUtil.class.getClassLoader().getResourceAsStream(PersistenceConstants.DDL_SCRIPT_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException("Error trying to create the system database.", e);
		}
	}
	
	private static boolean existDatabase(Connection connection) {
		boolean exist = false;
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(QUERY_REVIEWER_SCHEMA);
			
			exist = resultSet.next(); //&& resultSet.getString("schema_name").equals("REVIEWER");
			
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			throw new RuntimeException("Error trying to verify the existence of the system database.", e);
		}
		
		return exist;
	}
	
//	public static void main(String[] args) {
//		HSQLUtil.initDatabase();
//		
//		Study study = new Study();
//		study.setCode("PS1");
//		study.setTitle("ESEML: empirical software engineering modeling language");
//		study.setSource("ACM");
//		study.setAbstract("New processes, patterns, structures, tools, languages, and practices are being proposed for software development, but technology transfer is hard to achieve. One of the objectives of empirical studies is easing technology transfer from academy to industry. On the other hand, there are a number of issues that hinder the application of empirical studies, more specifically, controlled experiments. This paper defines a visual DSL for modeling controlled experiments supporting researchers that are not experts in such study. By using the language, the researcher is guided to define the elements of an experimental plan and connections, which is automatically generated, resulting a complete document of experimental plan. The proposed environment assists the definition of controlled experiments, increasing empirical evaluation of the proposed technologies. More specifically, the current version of our proposal generates the experimental plan from the experiment model defined using the DSL.");
//		study.setYear("2012");
//		study.setUrl("http://dl.acm.org/citation.cfm?id=2420933");
//		study.addAuthor("Bruno Cartaxo");
//		study.addAuthor("Italo Costa");
//		study.addAuthor("Dhiego Abrantes");
//		study.addAuthor("Andre Santos");
//		study.addAuthor("Sergio Soares");
//		study.addAuthor("Vinicius Garcia");
//		study.addInstitution("Informatics Center - Federal University of Pernambuco");
//		study.addCountry("Brazil");
//		
//		LiteratureReview literatureReview = new LiteratureReview();
//		literatureReview.setTitle("How to characterize context of empirical software engineering studies?");
//		literatureReview.addStudy(study);
//		
//		EntityTransaction transaction = JPAEntityManager.ENTITY_MANAGER.getTransaction();
//		transaction.begin();
////		JPAEntityManager.ENTITY_MANAGER.persist(study);
//		JPAEntityManager.ENTITY_MANAGER.persist(literatureReview);
//		transaction.commit();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
}
