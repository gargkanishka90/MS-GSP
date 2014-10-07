package ms_gsp_algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class ReadFiles {

	public ArrayList<Item> item = new ArrayList<Item>();
	private float sdc;
	Item newItem;
	ItemSet newItemSet;
	Sequence readSeq;
	int head = 0;
	private BufferedReader nextReader;
	
	public float getSDC(){
		try
		{
			String line;
			File para = new File("D:/para2-1.txt");
			Scanner scanline = new Scanner(para);
			while (scanline.hasNextLine()) { //check for existence of new line on the file
				line = scanline.nextLine();//read file line by line
				//Scanner scanItem = new Scanner(line);//reading line
				
				if(line.toUpperCase().contains("SDC")){
					
					Integer EqlIndx = line.indexOf("=");
					
					sdc = Float.parseFloat(line.substring(EqlIndx+1));
				}
			}// ending of the loops reading the next line.			
			scanline.close();
	}
		catch(Exception e){
			System.out.println("Error in reading the parameter file.");
		}
		
	return this.sdc;
	}
	
	public ArrayList<Item> getItemMISValues(){// Returns the list of item objects
		try
		{
			String line;
			File para = new File("D:/para2-1.txt");
			Scanner scanline = new Scanner(para);
			while (scanline.hasNextLine()) { //check for existence of new line on the file
				line = scanline.nextLine();//read file line by line
				//Scanner scanItem = new Scanner(line);//reading line
				if(line.toUpperCase().contains("MIS")){
					Integer IndexofStrtBrackt = line.indexOf("(");
					Integer IndexofEndBrackt = line.indexOf(")");
					Integer IndexofEqual = line.indexOf("=");
					
					Integer num = Integer.parseInt(line.substring(IndexofStrtBrackt+1,IndexofEndBrackt));
					Float mis = Float.parseFloat(line.substring(IndexofEqual+1));
					newItem=new Item();
					newItem.setItem(num);
					newItem.setMIS(mis);
					newItem.setsupport(0);
					item.add(newItem);
				}
				
				if(line.toUpperCase().contains("SDC")){
					
					Integer EqlIndx = line.indexOf("=");
					
					sdc = Float.parseFloat(line.substring(EqlIndx+1));
				}
			}// ending of the loops reading the next line.			
			scanline.close();
	}
		catch(Exception e){
			System.out.println("Error in reading the parameter file.");
		}
		
	return item;
}


	public Sequence readSequence(boolean flag, String dataPath){
	
		String currentLine;
		Sequence sequence = new Sequence();
		ItemSet itemset;
		ArrayList<Integer> items;
		
		try {	
		
			if(flag){
				head = 0;
				nextReader = new BufferedReader(new FileReader(dataPath));
			}
		
			if ((currentLine = nextReader.readLine()) != null) {
				
				sequence = new Sequence();
				
				currentLine = currentLine.replace("<", "");
				currentLine = currentLine.replace(">", "");
				currentLine = currentLine.replace("{", "");
				
				String[] splitItemSets = currentLine.split("}");
								
				for(String tempItemSet : splitItemSets){					
					itemset = new ItemSet();
					items = new ArrayList<Integer>();					
					String[] splitItems = tempItemSet.split(",");					
					for(String tempItem : splitItems){						
						items.add(Integer.parseInt(tempItem.trim()));						
					}					
					itemset.setItemSet(items);
					sequence.addItemSet(itemset);
				}
			 }
			else{
				nextReader.close();
				sequence = null;
			}
			head = head + 1;				
		} catch (FileNotFoundException e) {
			System.out.println(" Data file not found " + e);			
		} catch (Exception e){
			System.out.println("Error in Reading data " + e);
		}
		return sequence;
	}

}

