/**
 * 
 *@author Anusha Guntakandla and Kanishka Garg
 *CS 583 : MS-GSP Algorithm Implementation, Fall 2014
 *Project 1
 */


package ms_gsp_algo;

import java.util.*;



public class MultiSupGSP  {
	
	public ArrayList<Item> totalItems; //M Array
	public String dataPath;
	
	public MultiSupGSP(ArrayList<Item> allItems, String dataPath){
		this.totalItems = allItems;
		this.dataPath = dataPath;
	}
	ReadFiles forSDC=new ReadFiles();
	float actualSDC = forSDC.getSDC();

	/** This is the Array M (just before the first-pass)
        Items are sorted based on the value of their MIS.
        line in the algorithm: M <- sort(I,MS)
	* */
	public void MinSupSorter(){	
	Collections.sort(this.totalItems, new Item());	
	}
	
	/**
     * MSGSP algorithm implementation
     * @return list of association rule(frequent) sequences
     */
    public ArrayList<Sequence> control(){
    	ReadFiles rdfiles=new ReadFiles();
    	ArrayList<Integer> seed;
    	ArrayList<Sequence> f_1;
    	ArrayList<Sequence> finalSeq = new ArrayList<>();
    	ArrayList<Sequence> f_k_1 = new ArrayList<>();
    	ArrayList<Sequence> candidate_k = new ArrayList<>();
    	
    	//L <- init-pass(M,S)
    	seed = initPass(); 
    	
    	// F_1 <- {<{l}>| l is in L, l.count/n >= MIS(l)}; // n is the size of S
    	f_1 = findF_1(seed);
    	finalSeq.addAll(f_1);
    	
    	for(int p = 2 ; (f_k_1 = Sequence.getOfSize(finalSeq, p-1)).size()>0; p++){
    		if(p==2)
    			candidate_k = level2_candidate_genSPM(seed,finalSeq);
    		else{
    			candidate_k = MScandidate_genSPM(f_k_1,(p-1));
    		}
 
    	Sequence seq = rdfiles.readSequence(true,dataPath);

		int seqCount = 0;
		
		while(seq != null){
			
			seqCount = seqCount + 1;
											
			for(Sequence candidate : candidate_k){ 
			
				if(seq.contains(candidate)){
					candidate.increaseCountByOne();
				}
			}		
			
			seq = rdfiles.readSequence(false, null);
		}
		
		ArrayList<Sequence> frequentSet_k = new ArrayList<Sequence>();
		
		for(Sequence tmpseq : candidate_k){
			
			if(((float)tmpseq.getCount()/seqCount) >= tmpseq.getMinimumMIS(this.totalItems))
				frequentSet_k.add(tmpseq);
		}
		
		finalSeq.addAll(frequentSet_k);
    	}
		return finalSeq;
    }
	
    //general candidate generation other than level-2
	private ArrayList<Sequence> MScandidate_genSPM(ArrayList<Sequence> ofSize,int size) {
		ArrayList<Sequence> candidates = new ArrayList<Sequence>();
    	ArrayList<Sequence> afterPruning = new ArrayList<Sequence>();
		Sequence candidate = new Sequence();
		ArrayList<Integer> t1 = new ArrayList<Integer>();
		ArrayList<Integer> t2 = new ArrayList<Integer>();
		
		for(Integer l=0; l < ofSize.size(); l++ ){
			
			for(Integer h=0; h < ofSize.size(); h++){
				
				//if(l != h){
				
					Sequence a = ofSize.get(l);
					Sequence b = ofSize.get(h);
					
					t1 = new ArrayList<Integer>(a.getItemsinSequence());
					t2 = new ArrayList<Integer>(b.getItemsinSequence());
					
					if(a.isFirstLowest_MIS(this.totalItems)){						
						
						candidates.addAll(firstLowestjoin(a, b, size));						
					}
					else if(b.isLastLowest_MIS(this.totalItems)){
						
						candidates.addAll(lastLowestjoin(a, b, size));	
					}
					else{	
						
						if((candidate = generalJoin(a, b)) != null)
							candidates.add(candidate);
					}										
			}			
		}
		
		afterPruning = prune(candidates, ofSize);
    	
    	return afterPruning;
	}


	//pruning the unwanted candidates
	private ArrayList<Sequence> prune(ArrayList<Sequence> candidates,ArrayList<Sequence> ofSize) {
		ArrayList<Sequence> prunedSeq = new ArrayList<Sequence>();
    	ArrayList<Sequence> subSeq =  new ArrayList<Sequence>();    	
    	
    	int size = 0;
    	
    	if(ofSize.size() > 0){
    		size = ofSize.get(0).getSequence().size();
    	}
    	
    	for(Sequence seq : candidates){
    		    		
    		if(seq.getDistinctItemsinSequence().contains(this.totalItems.get(0))){
    			
    			ArrayList<ItemSet> seeds = getsubSequences(seq);     			
    			
    			for(int i=0; i < seeds.size(); i++){
    				
    				int top = 0;
    				
    				if(i != top){
    				
	    				Sequence newseq = new Sequence();
	    				
	    				while(newseq.getItemsinSequence().size() < size && top < seeds.size()){
	    					
	    					ItemSet is = new ItemSet();
	    					is.setItemSet(new ArrayList<Integer>(seeds.get(top).getItemSet()));
	    					newseq.addItemSet(is);   
	    					
	    					top = top + 1;
	    					
	    					if(newseq.getItemsinSequence().size() == size){
	    						subSeq.add(newseq);
	    					}
	    				} 
    				}
    			}

    			int count = 0;
    			
    			for(int j=0; j<subSeq.size() ; j++){
    				
    				if(ListContains(ofSize, subSeq.get(j))){    				
    					count = count++;
    				}    				
    			}
    			
    			if(count == subSeq.size()){
    				if(!ListHas(prunedSeq, seq))
    					prunedSeq.add(seq);
    			}
    		}
    		else{
    			if(!ListHas(prunedSeq, seq))
    				prunedSeq.add(seq);
    		}    		
    	}
    	
		return prunedSeq;
	}
	
	/**
	 * checks if a list of sequences contain a sequence
	 * @param sequences - list of sequences
	 * @param seq - sequence to be checked
	 * @return true or false
	 */
	public static boolean ListContains(ArrayList<Sequence> sequences, Sequence sequence) {

		boolean contains = false;

		for(Sequence s : sequences){

			if(s.contains(sequence)){
				
				contains = true;
				break;				
			}				
		}

		return contains;
	}	
	
	/**
	 * checks if a list of sequences contains (exact match) a sequence
	 * @param sequences - list of sequences
	 * @param seq - sequence to be checked
	 * @return true or false
	 */
	public static boolean ListHas(ArrayList<Sequence> sequences, Sequence sequence) {

		boolean contains = false;

		for(Sequence s : sequences){

			if(s.isEqualTo(sequence)){
				
				contains = true;
				break;				
			}				
		}

		return contains;
	}	
	
	/**
	 * get all k-1 candidate sequences
	 * @param seq - parent sequence
	 * @return a list of sequences
	 */
	public static ArrayList<ItemSet> getsubSequences (Sequence sequence){		
		
		int n = sequence.getItemsinSequence().size();
		int[] masks = new int[n];
		ArrayList<ItemSet> subSequences = new ArrayList<ItemSet>();
		
		for(int i=0; i<n;i++){
			masks[i] = (1<<i);
		}
		
		for(int i=0;i<(1<<n);i++){
				
			ArrayList<Integer> newList = new ArrayList<Integer>();
				
			for(int j=0; j<n;j++){
				if((masks[j] & i) != 0){
					newList.add(sequence.getItemsinSequence().get(j));					
				}
			}
			
			if(newList.size() == n-1){				
				
				ItemSet is = new ItemSet();
				
				is.setItemSet(newList);				
				
				subSequences.add(is);
			}			
		}
		
		return subSequences;
	}


	@SuppressWarnings("null")
	private ArrayList<Sequence> level2_candidate_genSPM(ArrayList<Integer> seed,ArrayList<Sequence> finalSeq) {
		/**
		 * Function level2-candidate-gen(L):
			1 C2 <- null ; // initialize the set of candidates 
			2 for each item l in L in the same order do
			3 	if l.count/n >= MIS(l) then
			4 		for each item h in L that is after l do
			5 			if h.count/n >= MIS(l) and |sup(h) - sup(l)| <= SDC then
			6 C2 <- C2 union {{l, h}}; // insert the candidate {l, h} into C2
		 * 
		 */
		ArrayList<Sequence> candidate2 = new ArrayList<Sequence>();
		for (int i=0; i<seed.size();i++){    
        	
            for (int j=i;j<seed.size();j++){
           	 
           	 if(i != j){
           		 
	            	 Item itemi = Sequence.extractItem(seed.get(i),this.totalItems);
	            	 Item itemj = Sequence.extractItem(seed.get(j),this.totalItems);   
	            	 	
	            	float supportDifference =(itemj.getActualMIS() - itemi.getActualMIS());
	            	 
	            	 Item minItem;
	            	 
	            	 if(itemi.getMIS() <= itemj.getMIS())
	            		 minItem = itemi;
	            	 else
	            		 minItem = itemj;
	            	 
	            	 
	            	 if( minItem.getActualMIS() >= minItem.getMIS() && Math.abs(supportDifference) <= actualSDC ) {
	                	                		 
	                	 Sequence seq1 = new Sequence();         
		                 ItemSet is1 = new ItemSet();         
		                 is1.add(itemi.getItem()); 	                     
		                 seq1.addItemSet(is1);                
		                     
		                 ItemSet is2 = new ItemSet();         
		                 is2.add(itemj.getItem());     
		                 seq1.addItemSet(is2);                
		                        
		                 candidate2.add(seq1);                        
	
	                     Sequence seq2 = new Sequence();         
		                 ItemSet is3 = new ItemSet();        
		                 is3.add(itemi.getItem()); 
		                 is3.add(itemj.getItem());     
		                 seq2.addItemSet(is3);                
		
		                 candidate2.add(seq2); 
	            	 }
	             }
            }        
       }
       
      candidate2 = prune(candidate2,finalSeq);
       
   	return candidate2;
   }


	//length 1 frequency items
	private ArrayList<Sequence> findF_1(ArrayList<Integer> seed) {
		
		ArrayList<Sequence> freq_1 = new ArrayList<Sequence>();
		Sequence seq;
		ItemSet items;		
		Item tmp;
		
		for(Integer item : seed){
		
			tmp = Sequence.extractItem(item,totalItems);
			
			if(tmp.getActualMIS() >= tmp.getMIS()){
				
				items = new ItemSet();
				items.add(item);						
				
				seq = new Sequence();
				seq.addItemSet(items);
				
				freq_1.add(seq);				
			}			
		}
		
		Sequence sequence; 
		ReadFiles rd = new ReadFiles();		
		Integer seqCount = 0;
		
		sequence = rd.readSequence(true,dataPath);

		while(sequence != null){	
			seqCount = seqCount + 1;
			for(Sequence candidate : freq_1){ 
				if(candidate != null && sequence != null){
					if(sequence.getItemsinSequence().containsAll(candidate.getItemsinSequence())){
						candidate.increaseCountByOne();
					}					
				}
			}
			sequence = rd.readSequence(false, null);
		}
		return freq_1;
	}

	
	/**
     * Compute the actual item supports from the data
     */
    public void computeActualItemSupport(){		
    	Integer seqCnt = 0;		
		ReadFiles r = new ReadFiles();
		Sequence s1 = r.readSequence(true,dataPath);
		while(s1 != null){
			seqCnt = seqCnt + 1;
			for(Integer item : s1.getDistinctItemsinSequence()){
				for(Item itm : this.totalItems){
					if(itm.getItem() == item){	
						itm.setsupport(itm.getsupport() + 1);
					}
				}
			}
			s1 = r.readSequence(false, null);
		}
		
		for(Item itm : this.totalItems){
			itm.setActualMIS((float)itm.getsupport()/seqCnt);
		}
	}
    
    
    /**
     * Initial pass over the data to generate the seed set L
     * L <- init-pass(M,S)
     * @return list of item numbers
     */
    public ArrayList<Integer> initPass(){	
    	
    	/**
    	 *  init-pass() has two steps: 
		    1. It first scans the data once to record the support count of each item. 
			2. It then follows the sorted order to find the first item i in M that meets 
			MIS(i). i is inserted into L. For each subsequent item j in M after i, if 
			j.count/n > MIS(i), then j is also inserted into L, where j.count is the 
			support count of j, and n is the total number of transactions in T.
    	 */
		
		boolean firstFound = false;
		Float minMIS = 0.0f;
		ArrayList<Integer> seedSet = new ArrayList<Integer>(); 
		//Compute actual item support by scanning the data once.
		computeActualItemSupport();
		
		for(Item itm : this.totalItems){
			
			if(!firstFound){
				if(itm.getActualMIS() >= itm.getMIS()){
					firstFound = true;
					minMIS = itm.getMIS();
					seedSet.add(itm.getItem());
				}		
			}
			else{
				if(itm.getActualMIS() >= minMIS){
					seedSet.add(itm.getItem());
				}
			}	
		}
		return seedSet;
	}
    
    /**
     * Condition 1 in join step (Sequence Equality - when MIS of first item in a sequence has lowestMIS)
     * 
     */
    private boolean condition1(Sequence a, Sequence b) {
		
		boolean flag = false;
		
		ArrayList<Integer> t1 = new ArrayList<Integer>(a.getItemsinSequence());
		ArrayList<Integer> t2 = new ArrayList<Integer>(b.getItemsinSequence());
				
		if(t1.size() > 1 && t2.size() > 0){
			
			t1.remove(1);
			t2.remove(t2.size() - 1);
			
			if(t1.equals(t2))
				flag = true;
			else
				flag = false;			
		}
		
		return flag;
	}
    
    /**
     * Condition 2 in join step (MIS of last item in s2 is greater then MIS of first item in s1.)
     * 
     */
    private boolean condition2(Sequence a, Sequence b) {
		
		boolean flag = false;
		
		ArrayList<Integer> t1 = new ArrayList<Integer>(a.getItemsinSequence());
		ArrayList<Integer> t2 = new ArrayList<Integer>(b.getItemsinSequence());
		
		int size_b = t2.size();
		int size_a = t1.size();
		int x=t1.get(0);
		Item first = Sequence.extractItem(x, this.totalItems);
		if(size_a > 0 && size_b > 0){
			int y = t2.get(size_b-1);
			Item it = Sequence.extractItem(y, this.totalItems);
	
			if(it.getMIS() > first.getMIS())
				flag = true;
			else
				flag = false;
		}
		return flag;
	}
    
    /**
     * Condition 3 in join step (Sequence Equality - when MIS of Last item in a sequence has lowest MIS)
     * 
     */
    private boolean condition3(Sequence a, Sequence b) {
		
		boolean flag = false;
		
		ArrayList<Integer> t1 = new ArrayList<Integer>(a.getItemsinSequence());
		ArrayList<Integer> t2 = new ArrayList<Integer>(b.getItemsinSequence());
				
		if(t1.size() > 0 && t2.size() > 1){
			
			t1.remove(0);
			t2.remove(t2.size() - 2);
			
			if(t1.equals(t2))
				flag = true;
			else
				flag = false;			
		}
		
		return flag;
	}
    
    /**
     * Condition 4 in join step (MIS of last item in s2 is less then MIS of first item in s1.)
     * 
     */
    private boolean condition4(Sequence a, Sequence b) {
		
		boolean flag = false;
		
		ArrayList<Integer> t1 = new ArrayList<Integer>(a.getItemsinSequence());
		ArrayList<Integer> t2 = new ArrayList<Integer>(b.getItemsinSequence());
		
		int size_b = t2.size();
		int size_a = t1.size();
		int x=t1.get(0);
		Item first = Sequence.extractItem(x, this.totalItems);
		if(size_a > 0 && size_b > 0){
			int y = t2.get(size_b-1);
			Item it = Sequence.extractItem(y, this.totalItems);
	
			if(it.getMIS() < first.getMIS())
				flag = true;
			else
				flag = false;
		}
		return flag;
	}
    
    /**
     * join step when the first item in the sequence 1 has the lowest minimum item support
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return candidate
     */
    private ArrayList<Sequence> firstLowestjoin(Sequence a, Sequence b, int size){
    	
    	ArrayList<Integer> alls1 = a.getItemsinSequence();
    	ArrayList<Integer> alls2 = b.getItemsinSequence();
    	Sequence candidate;
		ArrayList<Sequence> candidates = new ArrayList<Sequence>();
		float supDiff = 0.0f;
		//int a_size = a.getSequenceSize();
		int b_size = b.getSequenceSize();
		
		int b_last_size = b.getSequence().get(b_size-1).getItemSet().size();
		
		if(condition1(a,b) &&  condition2(a,b)){
			if(b_last_size == 1){
				candidate = new Sequence();
				for(ItemSet x : a.getSequence()){
					ItemSet newItemSet = new ItemSet();
					ArrayList<Integer> newItems = new ArrayList<Integer>(x.getItemSet());
					newItemSet.setItemSet(newItems);
					candidate.addItemSet(newItemSet);
				}
				
				ItemSet lastItemSet = new ItemSet();
				ArrayList<Integer> newLastItem = new ArrayList<Integer>();
				newLastItem.add(new Integer(b.extractLastItem()));
				lastItemSet.setItemSet(newLastItem);
				
				candidate.addItemSet(lastItemSet);
				
				supDiff = Sequence.getSupportDifference(lastItemSet, this.totalItems);
				
				if(supDiff <= (new ReadFiles().getSDC())){
						candidates.add(candidate);
				}				
				
				if((a.getSequenceSize() == 2 && a.getSequenceLength() == 2) &&
						(Sequence.extractItem(alls2.get(alls2.size()-1), this.totalItems).getMIS() > Sequence.extractItem(alls1.get(alls1.size()-1), this.totalItems).getMIS())) {
					
					candidate = new Sequence();
					
					for(ItemSet it : a.getSequence()){
						ItemSet newItemSet = new ItemSet();
						ArrayList<Integer> newItems = new ArrayList<Integer>(it.getItemSet());
						newItemSet.setItemSet(newItems);
						candidate.addItemSet(newItemSet);
					}
					
					candidate.getSequence().get(candidate.getSequence().size() - 1).add(new Integer(b.extractLastItem()));
					
					supDiff = Sequence.getSupportDifference(candidate.getSequence().get(candidate.getSequence().size() - 1), this.totalItems);
														
					if(supDiff <= (new ReadFiles().getSDC())){
							candidates.add(candidate);
					}
				}
			} else {
				if(((a.getSequenceLength() == 2 && a.getSequenceSize() == 1) &&
						(Sequence.extractItem(alls2.get(alls2.size()-1), this.totalItems).getMIS() > Sequence.extractItem(alls1.get(alls1.size()-1), this.totalItems).getMIS()))  
						|| (a.getSequenceLength() > 2)){  //|| (a.getSequence().size() > 2)){
					
					candidate = new Sequence();
					
					for(ItemSet it : a.getSequence()){
						ItemSet newItemSet = new ItemSet();
						ArrayList<Integer> newItems = new ArrayList<Integer>(it.getItemSet());
						newItemSet.setItemSet(newItems);
						candidate.addItemSet(newItemSet);
					}
					
					candidate.getSequence().get(candidate.getSequence().size() - 1).add(new Integer(b.extractLastItem()));
						
					supDiff = Sequence.getSupportDifference(candidate.getSequence().get(candidate.getSequence().size() - 1), this.totalItems);
					
					if(supDiff <= (new ReadFiles().getSDC())){
						candidates.add(candidate);
				}
				}
			}
			}// end of b_last_size == 1
		return candidates;
    }

    /**
     * join step when the last item in the sequence 2 has the lowest minimum item support
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return candidate
     */
    private ArrayList<Sequence> lastLowestjoin(Sequence a, Sequence b, int size){
    	
    	ArrayList<Integer> alls1 = a.getItemsinSequence();
    	ArrayList<Integer> alls2 = b.getItemsinSequence();
    	Sequence candidate;
		ArrayList<Sequence> candidates = new ArrayList<Sequence>();
		float supDiff = 0.0f;
		//int a_size = a.getSequenceSize();
		//int b_size = b.getSequenceSize();
		
		int a_first_size = a.getSequence().get(0).getItemSet().size();
		
		if(condition3(a,b) &&  condition4(a,b)){
			if(a_first_size == 1){
				candidate = new Sequence();
								
				ItemSet firstItemSet = new ItemSet();
				ArrayList<Integer> newFirstItem = new ArrayList<Integer>();
				newFirstItem.add(new Integer(a.extractFirstItem()));
				firstItemSet.setItemSet(newFirstItem);
				
				candidate.addItemSet(firstItemSet);
				
				for(ItemSet x : b.getSequence()){
					ItemSet newItemSet = new ItemSet();
					ArrayList<Integer> newItems = new ArrayList<Integer>(x.getItemSet());
					newItemSet.setItemSet(newItems);
					candidate.addItemSet(newItemSet);
				}
				
				
				supDiff = Sequence.getSupportDifference(firstItemSet, this.totalItems);
				
				if(supDiff <= (new ReadFiles().getSDC())){
						candidates.add(candidate);
				}				
				
				if((b.getSequenceSize() == 2 && b .getSequenceLength() == 2) &&
						(Sequence.extractItem(alls1.get(0), this.totalItems).getMIS() > Sequence.extractItem(alls2.get(0), this.totalItems).getMIS())) {
					
					candidate = new Sequence();
					
					for(int i=0; i<b.getSequence().size();i++){
						ItemSet newItemSet = new ItemSet();
						
						ArrayList<Integer> newItems = new ArrayList<Integer>();
						
						if(i == 0){
							newItems = new ArrayList<Integer>();
							newItems.add(new Integer(a.getItemsinSequence().get(0)));
							newItems.addAll(b.getSequence().get(i).getItemSet());
						}
						else{
							newItems = new ArrayList<Integer>(b.getSequence().get(i).getItemSet());
						}
							
						newItemSet.setItemSet(newItems);
						candidate.addItemSet(newItemSet);
					}
					if(supDiff <= (new ReadFiles().getSDC())){
							candidates.add(candidate);
					}
				}
			} else {
				if(((b.getSequenceLength() == 2 && b.getSequenceSize() == 1) &&
						(Sequence.extractItem(alls1.get(0), this.totalItems).getMIS() > Sequence.extractItem(alls2.get(0), this.totalItems).getMIS()))  
						|| (b.getSequenceLength() > 2)){  //|| (a.getSequence().size() > 2)){
					
					candidate = new Sequence();
					for(int i=0; i<b.getSequence().size();i++){
						ItemSet newItemSet = new ItemSet();
						
						ArrayList<Integer> newItems = new ArrayList<Integer>();
						
						if(i == 0){
							newItems = new ArrayList<Integer>();
							newItems.add(new Integer(a.getItemsinSequence().get(0)));
							newItems.addAll(b.getSequence().get(i).getItemSet());
						}
						else{
							newItems = new ArrayList<Integer>(b.getSequence().get(i).getItemSet());
						}
							
						newItemSet.setItemSet(newItems);
						candidate.addItemSet(newItemSet);
					}
					
					
					if(supDiff <= (new ReadFiles().getSDC())){
						candidates.add(candidate);
				}
				}
			}
			}// end of b_last_size == 1
		return candidates;
    }

    /**
     * Generic join step
     * @param s1 - sequence 1
     * @param s2 - sequence 2
     * @return candidate
     */
    private Sequence generalJoin(Sequence a, Sequence b){
    	
		ArrayList<Integer> t1;
		ArrayList<Integer> t2;
		Sequence candidate = new Sequence();
		float supDiff = 0.0f;
    	
    	t1 = new ArrayList<Integer>(a.getItemsinSequence());
		t2 = new ArrayList<Integer>(b.getItemsinSequence());
		
		if(t1.size() > 0 && t2.size()>0){
			
			t1.remove(0);
			t2.remove(t2.size() - 1);
			
			if(t1 == t2){
				
				if(b.isLastSeparate(t2)){
					
					candidate = new Sequence();
					
					candidate.setSequence(a.getSequence());
					ItemSet lastItemSet = new ItemSet();
					lastItemSet.add(b.extractLastItem());
					
					candidate.addItemSet(lastItemSet);
					
					supDiff = Sequence.getSupportDifference(lastItemSet, this.totalItems);
				}
				else{
					candidate = new Sequence();
					
					for(Integer i=0; i < a.getSequenceSize(); i++){
						
						if(i == a.getSequenceSize() - 1){
							
							ItemSet lastItemSet = a.getSequence().get(i);
							lastItemSet.add(b.extractLastItem());
							
							candidate.addItemSet(lastItemSet);
							
							supDiff = Sequence.getSupportDifference(lastItemSet, this.totalItems);
						}
						else{
							candidate.addItemSet(a.getSequence().get(i));
						}										
					}					
				}
				
				if(supDiff <= (new ReadFiles().getSDC())){
					candidate = null;
			}			
		}
    }
		return candidate;
}
    
}

