package hearthpwn_scrapper;

public class HpwnTableRow {

	
	String deckName = "";
	String deckType = "";
	String className = "";
	String link = "";
	String standard = "";
	
	public HpwnTableRow(String deckName, String deckType, String className, String link, String standard){
		
		this.deckName = deckName;
		this.deckType = deckType;
		this.className = className;
		this.link = link;
		this.standard = standard;
	
	}
	
	
	public String getDeckName(){
		return this.deckName;
	}
	public String getDeckType(){
		return this.deckType;
	}
	public String getClassName(){
		return this.className;
	}
	public String getlink(){
		return this.link;
	}
	public String getStandard(){
		return this.standard;
	}
	
	
	
	
	
}
