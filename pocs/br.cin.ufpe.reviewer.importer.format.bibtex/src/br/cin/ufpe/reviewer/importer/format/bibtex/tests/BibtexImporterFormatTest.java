package br.cin.ufpe.reviewer.importer.format.bibtex.tests;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import br.cin.ufpe.reviewer.importer.format.bibtex.BibtexImporterFormat;
import br.cin.ufpe.reviewer.model.study.PrimaryStudy;

public class BibtexImporterFormatTest {

	@Test
	public void testImport() throws FileNotFoundException, IOException {
		BibtexImporterFormat importer = new BibtexImporterFormat();

		List<PrimaryStudy> studies = importer.importStudiesFromFile("resources/test.bib");

		PrimaryStudy study = studies.get(0);

		assertEquals(study.getIssn(), "1938-6451");
		assertEquals(
				study.getPaperAbstract(),
				"Even though empirical research has grown in interest, "
						+ "techniques, methodologies and best practices are still in debate. In this context, test beds "
						+ "are effective when one needs to evaluate and compare technologies. The concept is well disseminated "
						+ "in other areas such as Computer Networks, but remains poorly explored in Software Engineering (SE). "
						+ "This paper presents a systematic mapping study on the SE test beds literature. From the initial set of 4239 studies, "
						+ "13 primary studies were selected and categorized. Based on that, we found that Software Architecture is the most "
						+ "investigated topic, controlled experiment is the most used method to evaluate such test beds, 20 benefits of"
						+ " using test beds in SE have been identified and that test beds comprise very heterogeneous structural elements.");
		assertEquals(study.getTitle(),
				"A Systematic Mapping Study on Software Engineering Testbeds");
		assertEquals(study.getUrl(), "");
		assertEquals(study.getYear(), 2011);

		assertEquals(study.getAuthors().get(0).getName(), "Barreiros, Emanoel");
		assertEquals(study.getAuthors().get(1).getName(), "Almeida, Adauto");
		assertEquals(study.getAuthors().get(2).getName(), "Saraiva, Juliana");
		assertEquals(study.getAuthors().get(3).getName(), "Soares, S.");
	}
}
