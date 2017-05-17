package hearthpwn_scrapper;

import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HearthPwn {

	/**
	 * Sends a get request to the server at URL and returns the request
	 * @param url
	 * @return Document - the HTML document returned by the get request
	 * @throws IOException
	 */
	
	public static Document getRequest(String url) throws IOException
	{
		
		Document doc = Jsoup.connect(url).get();
		String htmlString = doc.toString();
		
		return doc;
	}
	
	/**
	 * Gets and returns the first table from the document
	 * @param doc - document returned from a HTML get request
	 * @return the table body element
	 */
	
	public static Element getTableBody(Document doc)
	{
		Element toBody = doc.select("tbody").first();
		
		return toBody;
	}
	
	/**
	 * retrieves rows from a table body element
	 * @param Element tableBody
	 * @return
	 */
	public static Elements getRows(Element tableBody){
		Elements rows = tableBody.children();
		return rows;
	}
	
	
	/**
	 * prints a row from the table.
	 * @param rowElts a row with all the deck metadata
	 */
	public static void writeRow(Elements rowElts, PrintWriter file)
	{
		//System.out.println(rowElts);
		
		String link = "http://www.hearthpwn.com";
		String deckName = "";
		String deckType = "";
		String className = "";
		String standard = "";
		
		for(Element row: rowElts){
			
			if(row.className().equals("col-name")){
				
				Element deckNameElt = row.children().select("a").first();
				
				deckName = "\"" + deckNameElt.text() + "\"";
				link += deckNameElt.attr("href");
				
			}
			
			if(row.className().equals("col-deck-type")){
				deckType = row.text();
				
				String standardType = row.children().select("span").first().className();
				
				if(standardType.equals("is-std"))
				{
					standard = "standard";
				}
				
				else
				{
					standard = "wild";
				}
			}
			
			if(row.className().equals("col-class")){
				className = row.text();
			}
			
			
		}
		
		file.printf("%s,%s,%s,%s,%s\n", deckName, deckType, className, link, standard);
		
	}
	
	/**
	 * write every row into file
	 * @param rows - rows with all deck metadata
	 */
	public static void writeRows(Elements rows, PrintWriter file)
	{
		file.printf("deck_name,deck_type,class,link,standard\n");
		for(Element row: rows)
		{
			if(row.className().equals("even") || row.className().equals("odd"))
			{
				Elements eltsInRow = row.children();
				writeRow(eltsInRow, file);
			}
		}
	}
	
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub

		PrintWriter file = new PrintWriter("recent_decks.csv");
		
		Document doc = getRequest("http://www.hearthpwn.com/decks?filter-deck-tag=2");
		Element toBody = getTableBody(doc);
		
		Elements rows = getRows(toBody);

		writeRows(rows, file);
		
		
		file.flush();
		file.close();
	}

}
