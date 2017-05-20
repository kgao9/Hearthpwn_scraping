package hearthpwn_scrapper;

public class HpwnCard {

	String name;
	int cost;
	int numInDeck;
	
	public HpwnCard(String name, int cost, int numInDeck){
		
		this.name = name;
		this.cost = cost;
		this.numInDeck = numInDeck;
	
	}
	

	public String getName(){
		return this.name;
	}
	public int getCost(){
		return this.cost;
	}
	public int getNumInDeck(){
		return this.numInDeck;
	}
	
	
	
}
