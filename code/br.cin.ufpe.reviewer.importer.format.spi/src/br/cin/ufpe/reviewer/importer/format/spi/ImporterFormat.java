package br.cin.ufpe.reviewer.importer.format.spi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import br.cin.ufpe.reviewer.model.study.PrimaryStudy;

public interface ImporterFormat {
	
	public List<PrimaryStudy> importStudiesFromFile(String path) throws FileNotFoundException, IOException;

}
