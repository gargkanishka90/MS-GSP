package ms_gsp_algo;

import java.util.ArrayList;
import java.util.HashSet;

public class Sequence {
	
	private ArrayList<ItemSet> seq;
	private int count;
	
	public Item min_item = null;
	
	public Sequence(){ // Default Constructor
		this.seq = new ArrayList<ItemSet>();
		this.count = 0;
	}
	
	public Sequence(Sequence a){
		ItemSet it;
		ArrayList<Integer> itemset;
		
		for(ItemSet itmset : a.getSequence()){
			
			it=new ItemSet();
			itemset=new ArrayList<Integer>(itmset.getItemSet());
			it.setItemSet(itemset);
			this.addItemSet(it);
		}
	}

	/**
	 * This functions returns all the items (Even if repeated) in a sequence
	 * @return - List of Integers
	 */
	public ArrayList<Integer> getItemsinSequence(){
		ArrayList<Integer> result = new ArrayList<>();
		
		for(ItemSet tmp: this.seq){
			result.addAll(tmp.getItemSet());
		}
		return result;
	}

	//size of Sequence
	public int getSequenceSize(){
		return this.getSequence().size();
	}
	
	public int getSequenceLength(){
		return this.getItemsinSequence().size();
	}
	
	public void setCount(int cd){
		this.count = cd;
	} 
	
	public int getCount(){
		return this.count;
	}
	
	public void increaseCountByOne(){
		this.count++;
	}
	
	public void setSequence(ArrayList<ItemSet> tmp){
		this.seq = tmp;
	}
	
	public ArrayList<ItemSet> getSequence(){
		return this.seq;
	}

	public ArrayList<Integer> getDistinctItemsinSequence(){
		ArrayList<Integer> result = new ArrayList<>();
		
		for(ItemSet tmp: this.seq){
			result.addAll(tmp.getItemSet());
		}
		return new ArrayList<Integer>(new HashSet<Integer>(result));
	}
	
	public Float getMinimumMIS(ArrayList<Item> seq){
		
		Float minMIS = seq.get(seq.size() - 1).getMIS();
		Float tmp = 0.0f;
		
		for(Integer item : this.getItemsinSequence()){
			
			if((tmp = extractItem(item,seq).getMIS()) < minMIS)
				minMIS = tmp;			
		}	
		
		return minMIS;				
	}
	
	public static Item extractItem(Integer i, ArrayList<Item> seq2) {
		Item res = null;
		
		for(Item f: seq2){
			if(f.getItem().equals(i))
			{
			res = f;
			break;
			} 
		}
		return res;
	}

	//finds wheather first item in sequence has lowest MIS
	public boolean isFirstLowest_MIS(ArrayList<Item> allItems) {
	
		if(this.getItemsinSequence().size() <= 0)
			return false;
		
		Integer first = this.getItemsinSequence().get(0);
		Item firstItem = extractItem(first,allItems);
		Float firstMIS = firstItem.getMIS();
		Float minMISVal = extractItem(allItems.get(allItems.size()-1).getItem(), allItems).getMIS();
		Float tmpMISVal = 0.0f;
		
		for(int i=0; i < this.getItemsinSequence().size() - 1; i++){
			if(minMISVal > (tmpMISVal = extractItem(this.getItemsinSequence().get(i), allItems).getMIS())){
				minMISVal = tmpMISVal;
			}
		}
		
			if(minMISVal<firstMIS){
				return false;
			}
		return true;
	}
	
	//finds wheather last item in a sequence has lowest MIS
	public boolean isLastLowest_MIS(ArrayList<Item> allItems) {
		
		if(this.getItemsinSequence().size() <= 0)
			return false;
		
		Integer last = this.getItemsinSequence().get(this.getItemsinSequence().size()-1);
		Item lastItem = extractItem(last,allItems);
		Float minMISVal = extractItem(allItems.get(allItems.size()-1).getItem(), allItems).getMIS();
		Float tmpMISVal = 0.0f;
		Float lastMIS = lastItem.getMIS();
		
		
		for(int i=0; i < this.getItemsinSequence().size() - 1; i++){
			if(minMISVal > (tmpMISVal = extractItem(this.getItemsinSequence().get(i), allItems).getMIS())){
				minMISVal = tmpMISVal;
			}
		}
			if(minMISVal<lastMIS)
				return false;
				
		return true;
	}

	public boolean isLastSeparate(ArrayList<Integer> t2){
		boolean isSeperateItemSet = false;
		
		ItemSet lastItemSet = this.getSequence().get(this.getSequenceSize() - 1);
		
		if(lastItemSet.getItemSet().size() == 1)
			isSeperateItemSet = true;
		else 
			isSeperateItemSet = false;
		return isSeperateItemSet;
	}
	
	public Integer extractLastItem(){
		return this.getItemsinSequence().get(this.getItemsinSequence().size()-1);
	}
	
	public Integer extractFirstItem(){
		return this.getItemsinSequence().get(0);
	}
	
	public void addItemSet(ItemSet itemset){
		this.seq.add(itemset);
	}

	public boolean contains(Sequence sub){
		
		if(sub.getSequence().size() > this.getSequence().size())
			return false;
		
		int count = 0;
		
		for(int i=0, j=0; j < this.getSequence().size() && i < sub.getSequence().size(); j++ ){
			
			if(this.getSequence().get(j).subset(sub.getSequence().get(i))){
				count = count + 1;
				i++;
			}
		}
		
		if(count == sub.getSequence().size())		
			return true;
		else
			return false;
	}// end of contains function.
	
	public boolean isEqualTo(Sequence sub){
		
		if(sub.getSequence().size() != this.getSequenceSize())
			return false;
		
		int count = 0;
		
		for(int i=0; i < this.getSequenceSize(); i++ ){
			
			if(this.getSequence().get(i).isItEqual(sub.getSequence().get(i))){
				count = count + 1;
			}
		}
		
		if(count == sub.getSequence().size())		
			return true;
		else
			return false;
	}
	
	/**
	 
	 */
	public static ArrayList<Sequence> getOfSize(ArrayList<Sequence> fS, int size) {
		
		ArrayList<Sequence> filteredSet = new ArrayList<Sequence>();
		
		for(Sequence s : fS){		
			
			if(s.getItemsinSequence().size() == size){
				filteredSet.add(s);
			}
		}
		
		return filteredSet;
	}

	/**
	
	 */
	public static float getSupportDifference(ItemSet q, ArrayList<Item> allItems) {
		
		Float minMIS = allItems.get(allItems.size() - 1).getMIS();
		Float maxMIS = 0.0f;
		Float tmp = 0.0f;
		
		for(Integer item : q.getItemSet()){
			if((tmp = extractItem(item,allItems).getMIS()) < minMIS)
				minMIS = tmp;
			
			if((tmp = extractItem(item,allItems).getMIS()) > maxMIS)
				maxMIS = tmp;
		}
		
		return (maxMIS - minMIS);		
	}
}
