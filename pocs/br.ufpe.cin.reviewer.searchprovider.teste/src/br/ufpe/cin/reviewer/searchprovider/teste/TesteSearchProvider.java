package br.ufpe.cin.reviewer.searchprovider.teste;

import java.util.LinkedList;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.common.Study.StudyStatus;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

public class TesteSearchProvider implements SearchProvider {
	
	public static final String SEARCH_PROVIDER_NAME = "TESTE";

	public TesteSearchProvider() {
		
	}

	@Override
	public SearchProviderResult search(String searchString)
			throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
		LinkedList<Study> StudiesTeste = new LinkedList<Study>();
		
		for(int i = 0;i < 5;i++){
			Study study = new Study();
			study.setTitle("Esse é o estudo " + (i+1));
			study.setAbstract("bla bla bla bla bla bla bla bla bla");
			study.setUrl("http://www.algumacoisa.com");
			study.setStatus(StudyStatus.NOT_EVALUATED);
			study.setSource(SEARCH_PROVIDER_NAME);
			StudiesTeste.add(study);
		}
		
		result.getStudies().addAll(StudiesTeste);
		result.setTotalFetched(5);
		result.setTotalFound(10);
		
		return result;
	}

}
