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
	
	public static void writeRow(Elements rowElts)
	{
		System.out.println(rowElts);
	}
	
	/**
	 * write every row into file
	 * @param rows - rows with all deck metadata
	 */
	public static void writeRows(Elements rows)
	{
		for(Element row: rows)
		{
			if(row.className().equals("even") || row.className().equals("odd"))
			{
				Elements eltsInRow = row.children();
				writeRow(eltsInRow);
				return;
			}
		}
	}
	
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub

		Document doc = getRequest("http://www.hearthpwn.com/decks?filter-deck-tag=2");
		Element toBody = getTableBody(doc);
		
		Elements rows = getRows(toBody);

		writeRows(rows);
	}

}

