package hearthpwn_scrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HearthPwn {
	
	private static ArrayList<HpwnTableRow> listOfRows = new ArrayList<HpwnTableRow>();
	private static ArrayList<HpwnCard> listOfCards;
	
	/**
	 * displays a given table, formated nicely
	 * @param the list that wants to be displayed
	 */
	public static void displayRows(ArrayList<HpwnTableRow> list){
		
		int i = 1;
		for(HpwnTableRow row: list){
			
			System.out.printf(i + " %-50.50s%-30.30s%-15.15s%-15.15s%-70.70s\n", row.getDeckName(), row.getDeckType(), row.getClassName(), row.getStandard(), row.getlink());
			i++;
		}
		
	}
	
	/**
	 * display all the cards in the list
	 * @param list - the deck list
	 */
	public static void displayCards(ArrayList<HpwnCard> list){
		
		int i = 1;
		for(HpwnCard card: list){
			
			System.out.printf("%-30.30s%-15.15s%-15.15s\n", card.getName(), "x" + card.getNumInDeck(), card.getCost() + " mana.");
			i++;
		}
		
	}
		
	/**
	 * Selects the entries from the listOfRows that have a specific classname.
	 * @param className - the class name to select on
	 * @return an array list of rows that all share the specific class.
	 */
	public static ArrayList<HpwnTableRow> getAllRowsByClass(String className, ArrayList<HpwnTableRow> list){
		
		ArrayList<HpwnTableRow> returnList = new ArrayList<HpwnTableRow>();
		
		for(HpwnTableRow row: list){
			
			
			if(className.equals(row.getClassName())){
			
				returnList.add(row);
			}
		}
	
		return returnList;
		
	}
	

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
			
			if(row.className().equals("col-name") || row.className().equals("col-name t-arena-cell")){
				
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
				className = row.text().toLowerCase();
			}
			
			
		}
		
		file.printf("%s,%s,%s,%s,%s\n", deckName, deckType, className, link, standard);
		listOfRows.add(new HpwnTableRow(deckName, deckType, className, link, standard));
		
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
	
	/**
	 * write a row in the deckm save the card
	 * @param rowElts - the rows of the deck
	 */
	public static void writeDeckRow(Elements rowElts){
		
		String cardName = "";
		int num = 0;
		int cost = 0;
		
		for(Element row: rowElts){
			
			if(row.className().equals("col-name")){
				
				Element cardNameElt = row.children().select("a").first();
				
				cardName = cardNameElt.text();
				
				num = Integer.parseInt(cardNameElt.attr("data-count"));
				
			}
			
			else if(row.className().equals("col-cost")){
				cost = Integer.parseInt(row.text());
			}
			
			
			
			
		}
		listOfCards.add(new HpwnCard(cardName, cost, num));
		
	}
	
	/**
	 * get table for the deck.
	 * @param url - the url link to the deck
	 * @throws IOException
	 */
	public static void getDeckTable(String url) throws IOException{
		
		
		Document doc = Jsoup.connect(url).get();
		String htmlString = doc.toString();

		//need a loop to do this twice, once for class cards, another time for nutral cards, because the html use two tables.
		for(int i = 0; i <= 1; i++){
			
			Element Body = doc.select("tbody").get(i);
			Elements Rows = Body.children();
			
			for(Element row : Rows){
				
				if(row.className().equals("even") || row.className().equals("odd")){
					
					Elements eltsInRow = row.children();
					writeDeckRow(eltsInRow);
				}
			}
		}
	}
	
	/**
	 * retrieves and displays the decklist
	 * @param url - the url link to the deck
	 * @throws IOException
	 */
	public static void handleGetDeckList(String url) throws IOException{
		
		
		listOfCards = new ArrayList<HpwnCard>();
	
		getDeckTable(url);
		
		displayCards(listOfCards);
		
	}
	
	/**
	 * does the scrapping for the hearthpwn website
	 * @throws IOException
	 */
	public static void scrapper() throws IOException{
		PrintWriter file = new PrintWriter("recent_decks.csv");
		System.out.println("<--Starting HearthPwn Scraping-->");
		
		for(int i = 1; i <= 15; i++){
			
			System.out.println("Getting page " + i);
			
			Document doc = getRequest("http://www.hearthpwn.com/decks?filter-deck-tag=2&page=" + i);
			Element toBody = getTableBody(doc);
		
			Elements rows = getRows(toBody);

			writeRows(rows, file);
		}
		
		System.out.println("<--CSV file created secusfully-->");
		file.flush();
		file.close();
	}
	
	
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub
		
		if(!(args.length == 0 || args.length ==2)){
			System.err.println("args = " + args.length + " Incorret Args Length");
			System.exit(1);
		}
		
		//display all 
		if(args.length == 0){
			scrapper();
			displayRows(listOfRows);
		}
		else if(args.length == 2){
			
			if(!(args[0].equals("-c") || args[0].equals("-l"))){
				System.err.println("unknown command.\nuse -c to choose class\nuse -l to choose deck");
			}
			
			//display by class:
			else if(args[0].equals("-c")){
				scrapper();
				
				String className = args[1].toLowerCase();
				displayRows(getAllRowsByClass(className, listOfRows));
			}
			else if(args[0].equals("-l")){
				
				String link = args[1];
				handleGetDeckList(link);
			}
		}
	}

}
