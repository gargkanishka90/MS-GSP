package ms_gsp_algo;

import java.util.ArrayList;

public class ItemSet {
	
	ArrayList<Integer> itemSet; //Ex. [1,2,3,4]
	
	public ItemSet(){ // Default Constructor.
		this.itemSet = new ArrayList<Integer>();
	}
	
	protected ItemSet(ArrayList<Integer> allItems){    	
    	this.itemSet = allItems;
    }
	
	public void setItemSet(ArrayList<Integer> items){
		this.itemSet=items;
	}
	
	public ArrayList<Integer> getItemSet(){
		return this.itemSet;
	}
	
	/**
	 * Checks if a subItemSet is contained in itemset.
	 * @param subItemset
	 * @return boolean
	 */
	public boolean subset(ItemSet subItemset){
		if(subItemset.getItemSet().size()> this.getItemSet().size())
			return false;
		
		int count = 0;
		
		for(int i=0, j=0; j < this.getItemSet().size() && i < subItemset.getItemSet().size(); j++ ){
			
			if(this.getItemSet().get(j).equals(subItemset.getItemSet().get(i))){
				count = count + 1;
				i++;
			}
		}
		
		if(count == subItemset.getItemSet().size())		
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if a subItemSet is equal to itemset.
	 * @param subItemset
	 * @return boolean
	 */
	
	public boolean isItEqual(ItemSet subItemset){
		if(subItemset.getItemSet().size() != this.getItemSet().size())
			return false;
		
		for(int i=0;i<subItemset.getItemSet().size();i++)
		{
			if(!this.getItemSet().get(i).equals(subItemset.getItemSet().get(i)))
				return false;
			else
				continue;
		}
		return true;
	}
	
	/**
	 * This function adds an item to the itemset.
	 * @param item_to_be_added
	 */
	public void add(int item_to_be_added){
		this.itemSet.add(item_to_be_added);
	}
}

