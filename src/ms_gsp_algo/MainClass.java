package ms_gsp_algo;

import java.util.ArrayList;

public class MainClass {
	public static String dataPath = "D:/data2.txt";
	public static String outputFilePath = "D:/output2-2.txt";
	
	public static void main(String[] args) {
		ArrayList<Item> allitems;
		//float sdcVal;
		MultiSupGSP msgsp;
		ArrayList<ItemSet> allItemSets; // allItemSets contains all the items.
		ArrayList<Sequence> finalSeq; // list of all the frequent sequences.
		
		
		
		allitems = new ArrayList<Item>();
		ReadFiles r1= new ReadFiles();
		//ItemSets itmset=new ItemSets();
		allitems=r1.getItemMISValues();
		//sdcVal = r1.getSDC();
		//System.out.println(sdcVal);
		
		msgsp=new MultiSupGSP(allitems,dataPath);
		
		/** This is the Array M (just before the first-pass)
		    Items are sorted based on the value of their MIS.
		    line in the algorithm: M <- sort(I,MS)
		* */
		msgsp.MinSupSorter(); 
		finalSeq = msgsp.control();
		
		(new Display(finalSeq,outputFilePath)).doDisplay();
		
		System.out.println("Executed");
	}

}
