package br.cin.ufpe.reviewer.search.provider.acm;

import java.net.URL;
import java.util.Set;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public class AcmSearchProvider implements SearchProvider {

	public Set<Study> search(String searchString) {
		return null;
	}
	
	public static void main(String[] args) {
		try {
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode node = cleaner.clean(new URL("http://dl.acm.org/results.cfm?within=%22software+engineering%22"));
			
			SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(cleaner.getProperties());
			serializer.writeToFile(node, "C:/Documents and Settings/Bruno Cartaxo/Desktop/search.html");
			
			node.traverse(new TitleVisitor());
			node.traverse(new UrlVisitor());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private static class TitleVisitor implements TagNodeVisitor {

		public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
	        if (htmlNode instanceof TagNode) {
	            TagNode tag = (TagNode) htmlNode;
	            String tagName = tag.getName();
	            if ("a".equals(tagName)) {
	            	String tagClass = tag.getAttributeByName("class");
	            	
	            	if (tagClass != null && tagClass.equalsIgnoreCase("medium-text")) {
	            		String title = tag.getText().toString();
		                
		                if (title != null) {
		                    System.out.println(title);
		                }
	                }
	            }
	        }
	        
	        return true;
	    }
	}
	
	private static class UrlVisitor implements TagNodeVisitor {

		public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
	        if (htmlNode instanceof TagNode) {
	            TagNode tag = (TagNode) htmlNode;
	            String tagName = tag.getName();
	            if ("a".equals(tagName)) {
	            	String tagClass = tag.getAttributeByName("class");
	            	
	            	if (tagClass != null && tagClass.equalsIgnoreCase("medium-text")) {
	            		String href = tag.getAttributeByName("href");
		                
		                if (href != null) {
		                    System.out.println("http://dl.acm.org/" + href);
		                }
	                }
	            }
	        }
	        
	        return true;
	    }
	}

//	public static void main(String[] args) {
//		String html = null;
//		
//		try {
//			HttpClient httpclient = new DefaultHttpClient();
//			
//			HttpGet httpget = new HttpGet("http://dl.acm.org/results.cfm?within=%22software+engineering%22");
//			HttpResponse response = httpclient.execute(httpget);
//			
//			HttpEntity entity = response.getEntity();
//			
//			if (entity != null) {
//				StringBuilder builder = new StringBuilder();
//			    InputStream instream = entity.getContent();
//
//			    byte[] buffer = new byte[1024];
//		        int bytesRead = -1;
//		        
//		        while ((bytesRead = instream.read(buffer)) != -1)
//		        {
//		            if (bytesRead == 1024) {
//						builder.append(new String(buffer, "UTF-8"));
//					} else {
//						byte[] array = new byte[bytesRead];
//						System.arraycopy(buffer, 0, array, 0, bytesRead);
//						builder.append(new String(array, "UTF-8"));
//					}
//		        }
//		        
//		        html = builder.toString();
//			}
//			
//
//			Lexer lexer = new Lexer(html);
////			StringBuilder tokens = new StringBuilder();
////			
////			for (Node node = lexer.nextNode(); node != null; node = lexer.nextNode()) {
////				if (node instanceof Tag) {
////					Tag tag = (Tag) node;
////					
////					if (tag instanceof LinkTag) {
////						LinkTag linkTag = (LinkTag) tag;
////						String tagClass = linkTag.getAttribute("CLASS");
////						
////						if (tagClass!= null && tagClass.equalsIgnoreCase("medium-text")) {
////							tokens.append(linkTag.getLinkText() + "\n");
////							tokens.append(linkTag.getLink() + "\n");
////						}
////					} else if (tag instanceof Div) {
////						Div divTag = (Div) tag;
////						String tagClass = divTag.getAttribute("CLASS");
////						
////						if (tagClass!= null && tagClass.equalsIgnoreCase("abstract2")) {
////							tokens.append(divTag.toHtml() + "\n");
////						}
////					}
////				}
////			}
////			
////			FileWriter writer = new FileWriter("C:/Documents and Settings/Bruno Cartaxo/Desktop/tokens.txt");
////			writer.write(tokens.toString());
////			writer.flush();
////			writer.close();
//			
//			
////			Parser parser = new Parser("http://dl.acm.org/results.cfm?within=%22software+engineering%22");
//			Parser parser = new Parser(lexer);
//			NodeList nodes = parser.parse(null);
//			SimpleNodeIterator elements = nodes.elements();
//			
//			Node node = null;
//			while (elements.hasMoreNodes()) {
//				node = elements.nextNode();
//				if (node instanceof Tag && ((Tag) node).getTagName().equals("HTML")) {
//					Tag tag = (Tag) node;
//					String title = extractTitle(tag);
//				}
//			}
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//	}
//
//	private static String extractTitle(Tag tag) {
//		String title = null;
//		
//		String tagClass = tag.getAttribute("CLASS");
//		
//		if (tag instanceof LinkTag && tagClass!= null && tagClass.equalsIgnoreCase("medium-text")) {
//			LinkTag linkTag = (LinkTag) tag;
//			System.out.println(linkTag.getLinkText());
//		} else if (tag instanceof LinkTag && tagClass!= null && tagClass.equalsIgnoreCase("medium-text")) {
//			LinkTag linkTag = (LinkTag) tag;
//			System.out.println(linkTag.getLink());
//		} else if (tag instanceof Div && tagClass!= null && tagClass.equalsIgnoreCase("abstract2")) {
//			Div divTag = (Div) tag;
//			System.out.println(divTag.toHtml());
//		} else {
//			NodeList children = tag.getChildren();
//			if (children != null) {
//				SimpleNodeIterator elements = children.elements();
//				Node node = null;
//				while (elements.hasMoreNodes()) {
//					node = elements.nextNode();
//					if (node instanceof Tag) {
//						Tag childTag = (Tag) node;
//						title = extractTitle(childTag);
//					}
//
//				}
//			}
//		}
//			
//		return title;
//	}

	
}
