package br.ufpe.cin.reviewer.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.reviewer.logger.*;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.study.Study;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LiteratureReviewExport {

	public static void exportLR(LiteratureReview review) {

		XStream stream = new XStream(new DomDriver());

		try {
			FileOutputStream file = new FileOutputStream(new File(
					review.getTitle() + ".xml"));

			stream.toXML(review, file);
		} catch (FileNotFoundException e) {
			ReviewerLogger.error(e.getMessage());
		}

	}

	public static LiteratureReview importLR(String filename) {

		LiteratureReview lr = null;
		try {
			XStream stream = new XStream(new DomDriver());
			stream.alias(LiteratureReview.class.getName(),
					LiteratureReview.class);

			FileInputStream file = new FileInputStream(new File(filename));

			lr = (LiteratureReview) stream.fromXML(file);

		} catch (FileNotFoundException e) {
			ReviewerLogger.error(e.getMessage());
		}
		return lr;

	}

	public static void main(String[] args) {
		LiteratureReview review = new LiteratureReview();
		review.setTitle("Teste");
		review.setQueryString("QueryTest");

		List<Study> studies = new ArrayList<Study>();

		Study s = new Study();

		s.setTitle("Study Title");

		studies.add(s);
		review.setStudies(studies);

		//LiteratureReviewExport.exportLR(review);
		
		LiteratureReview review2 = LiteratureReviewExport.importLR(review.getTitle()+".xml");
		
		System.out.println(review2.getStudies().get(0).getTitle());
		
	}
}
